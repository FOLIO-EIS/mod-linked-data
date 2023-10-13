package org.folio.linked.data.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BibframeConstants {

  // Types
  public static final String INSTANCE = "http://bibfra.me/vocab/lite/Instance";
  public static final String ITEM = "http://bibfra.me/vocab/lite/Item";
  public static final String WORK = "http://bibfra.me/vocab/lite/Work";
  public static final String INSTANCE_TITLE = "http://bibfra.me/vocab/marc/Title";
  public static final String PARALLEL_TITLE = "http://bibfra.me/vocab/marc/ParallelTitle";
  public static final String VARIANT_TITLE = "http://bibfra.me/vocab/marc/VariantTitle";
  public static final String PROVIDER_EVENT = "http://bibfra.me/vocab/lite/ProviderEvent";
  public static final String PLACE = "http://bibfra.me/vocab/lite/Place";
  public static final String ANNOTATION = "http://bibfra.me/vocab/lite/Annotation";
  public static final String LCCN = "http://library.link/identifier/LCCN";
  public static final String ISBN = "http://library.link/identifier/ISBN";
  public static final String EAN = "http://bibfra.me/vocab/identifier/Ean";
  public static final String OTHER_ID = "http://library.link/identifier/UNKNOWN";
  public static final String LOCAL_ID = "http://bibfra.me/vocab/lite/LocalId";
  public static final String STATUS = "http://bibfra.me/vocab/marc/Status";
  public static final String CATEGORY = "http://bibfra.me/vocab/lite/Category";
  public static final String COPYRIGHT_EVENT = "http://bibfra.me/vocab/lite/CopyrightEvent";
  public static final String PERSON = "http://bibfra.me/vocab/lite/Person";
  public static final String FAMILY = "http://bibfra.me/vocab/lite/Family";
  public static final String ORGANIZATION = "http://bibfra.me/vocab/lite/Organization";
  public static final String MEETING = "http://bibfra.me/vocab/lite/Meeting";

  // Predicates
  public static final String RELATION_PREDICATE_PREFIX = "http://bibfra.me/vocab/relation/";
  public static final String INSTANCE_TITLE_PRED = "http://bibfra.me/vocab/marc/title";
  public static final String PRODUCTION_PRED = "http://bibfra.me/vocab/marc/production";
  public static final String PUBLICATION_PRED = "http://bibfra.me/vocab/marc/publication";
  public static final String DISTRIBUTION_PRED = "http://bibfra.me/vocab/marc/distribution";
  public static final String MANUFACTURE_PRED = "http://bibfra.me/vocab/marc/manufacture";
  public static final String PROVIDER_PLACE_PRED = "http://bibfra.me/vocab/lite/providerPlace";
  public static final String ACCESS_LOCATION_PRED = "http://bibfra.me/vocab/marc/accessLocation";
  public static final String MAP_PRED = "http://library.link/vocab/map";
  public static final String STATUS_PRED = "http://bibfra.me/vocab/marc/status";
  public static final String MEDIA_PRED = "http://bibfra.me/vocab/marc/media";
  public static final String CARRIER_PRED = "http://bibfra.me/vocab/marc/carrier";
  public static final String COPYRIGHT_PRED = "http://bibfra.me/vocab/marc/copyright";
  public static final String INSTANTIATES_PRED = "http://bibfra.me/vocab/lite/instantiates";
  public static final String CLASSIFICATION_PRED = "http://bibfra.me/vocab/lite/classification";
  public static final String CREATOR_PRED = "http://bibfra.me/vocab/lite/creator";
  public static final String CONTRIBUTOR_PRED = "http://bibfra.me/vocab/lite/contributor";
  public static final String CONTENT_PRED = "http://bibfra.me/vocab/marc/content";

  // Properties
  public static final String ID = "id";
  public static final String TYPE = "type";
  public static final String NOTE = "http://bibfra.me/vocab/lite/note";
  public static final String PART_NAME = "http://bibfra.me/vocab/marc/partName";
  public static final String PART_NUMBER = "http://bibfra.me/vocab/marc/partNumber";
  public static final String MAIN_TITLE = "http://bibfra.me/vocab/marc/mainTitle";
  public static final String DATE = "http://bibfra.me/vocab/lite/date";
  public static final String SUBTITLE = "http://bibfra.me/vocab/marc/subTitle";
  public static final String NON_SORT_NUM = "http://bibfra.me/vocab/bflc/nonSortNum";
  public static final String VARIANT_TYPE = "http://bibfra.me/vocab/marc/variantType";
  public static final String RESPONSIBILITY_STATEMENT = "http://bibfra.me/vocab/marc/statementOfResponsibility";
  public static final String EDITION_STATEMENT = "http://bibfra.me/vocab/marc/edition";
  public static final String DIMENSIONS = "http://bibfra.me/vocab/marc/dimensions";
  public static final String PROJECTED_PROVISION_DATE = "http://bibfra.me/vocab/bflc/projectedProvisionDate";
  public static final String ISSUANCE = "http://bibfra.me/vocab/marc/issuance";
  public static final String LINK = "http://bibfra.me/vocab/lite/link";
  public static final String NAME = "http://bibfra.me/vocab/lite/name";
  public static final String PROVIDER_DATE = "http://bibfra.me/vocab/lite/providerDate";
  public static final String SIMPLE_PLACE = "http://bibfra.me/vocab/lite/place";
  public static final String LABEL_RDF = "http://www.w3.org/2000/01/rdf-schema#label";
  public static final String LABEL = "http://bibfra.me/vocab/lite/label";
  public static final String QUALIFIER = "http://bibfra.me/vocab/marc/qualifier";
  public static final String EAN_VALUE = "http://bibfra.me/vocab/marc/ean";
  public static final String LOCAL_ID_VALUE = "http://bibfra.me/vocab/marc/localId";
  public static final String ASSIGNING_SOURCE = "http://bibfra.me/vocab/marc/localIdAssigningSource";
  public static final String CODE = "http://bibfra.me/vocab/marc/code";
  public static final String TERM = "http://bibfra.me/vocab/marc/term";
  public static final String TARGET_AUDIENCE = "http://bibfra.me/vocab/marc/targetAudience";
  public static final String SUMMARY = "http://bibfra.me/vocab/marc/summary";
  public static final String LANGUAGE = "http://bibfra.me/vocab/lite/language";
  public static final String TABLE_OF_CONTENTS = "http://bibfra.me/vocab/marc/tableOfContents";
  public static final String SOURCE = "http://bibfra.me/vocab/marc/source";
  public static final String LCNAF_ID = "http://bibfra.me/vocab/marc/lcnafId";
}
