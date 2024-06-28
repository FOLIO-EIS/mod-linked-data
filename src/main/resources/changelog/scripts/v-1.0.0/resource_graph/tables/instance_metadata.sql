create table if not exists instance_metadata (
     id serial not null primary key,
     inventory_id uuid,
     srs_id uuid,
     source resource_source
  );

comment on table instance_metadata is 'Metadata for an instance resource';
comment on column instance_metadata.id is 'Unique identifier for the instance metadata';
comment on column instance_metadata.inventory_id is 'ID of the instance in FOLIO inventory application';
comment on column instance_metadata.srs_id is 'ID of the instance in FOLIO SRS application';
comment on column instance_metadata.source is 'Source of the instance resource';
