--liquibase formatted sql

--changeset pavel_bobylev:check_unique_active_srs_id dbms:postgresql splitStatements:false
do $do$
BEGIN
  EXECUTE format($format$
    CREATE OR REPLACE FUNCTION %1$I.check_unique_active_srs_id() RETURNS TRIGGER AS
    $function$
      BEGIN
        IF EXISTS (
          SELECT 1
          FROM %1$I.resources r
          WHERE r.active = true AND r.resource_hash IN (
              SELECT fm.resource_hash
              FROM %1$I.folio_metadata fm
              WHERE fm.srs_id = NEW.srs_id
            )
        )
        THEN
          RAISE EXCEPTION 'There should be only one active resource per unique srs_id [%%]', NEW.srs_id;
        END IF;
        RETURN NEW;
      END
    $function$
    LANGUAGE plpgsql;
  $format$, CURRENT_SCHEMA);
END;
$do$;
comment on function check_unique_active_srs_id() is 'Check for unique active SRS ID';

--rollback drop function if exists check_unique_active_srs_id()
