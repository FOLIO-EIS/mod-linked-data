package org.folio.linked.data.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BibframeConstants {

  // Types
  public static final String MONOGRAPH = "http://bibfra.me/vocab/marc/Monograph";
  public static final String INSTANCE = "http://bibfra.me/vocab/lite/Instance";
  public static final String ITEM = "http://bibfra.me/vocab/lite/Item";
  public static final String WORK = "http://bibfra.me/vocab/lite/Work";
  public static final String INSTANCE_TITLE = "http://bibfra.me/vocab/marc/Title";
  public static final String PARALLEL_TITLE = "http://bibfra.me/vocab/marc/ParallelTitle";
  public static final String VARIANT_TITLE = "http://bibfra.me/vocab/marc/VariantTitle";
  public static final String PROVIDER_EVENT = "https://bibfra.me/vocab/lite/ProviderEvent";
  public static final String PLACE = "http://bibfra.me/vocab/lite/Place";
  public static final String E_LOCATOR = "https://bibfra.me/vocab/marc/ElectronicLocator";
  public static final String LCCN = "http://library.link/identifier/LCCN";
  public static final String STATUS = "http://bibfra.me/vocab/marc/Status";

  // Predicates
  public static final String INSTANCE_TITLE_PRED = "http://bibfra.me/vocab/marc/title";
  public static final String PRODUCTION_PRED = "http://bibfra.me/vocab/marc/production";
  public static final String PUBLICATION_PRED = "http://bibfra.me/vocab/marc/publication";
  public static final String DISTRIBUTION_PRED = "http://bibfra.me/vocab/marc/distribution";
  public static final String MANUFACTURE_PRED = "http://bibfra.me/vocab/marc/manufacture";
  public static final String PLACE_PRED = "http://bibfra.me/vocab/lite/place";
  public static final String E_LOCATOR_PRED = "https://bibfra.me/vocab/marc/electronicLocator";
  public static final String MAP_PRED = "http://bibfra.me/vocab/lite/map";
  public static final String STATUS_PRED = "http://bibfra.me/vocab/marc/status";

  // Properties
  public static final String NOTE = "http://bibfra.me/vocab/lite/note";
  public static final String PART_NAME = "http://bibfra.me/vocab/marc/partName";
  public static final String PART_NUMBER = "http://bibfra.me/vocab/marc/partNumber";
  public static final String MAIN_TITLE = "http://bibfra.me/vocab/marc/mainTitle";
  public static final String DATE = "http://bibfra.me/vocab/lite/date";
  public static final String SUBTITLE = "http://bibfra.me/vocab/marc/subtitle";
  public static final String NON_SORT_NUM = "http://bibfra.me/vocab/bflc/nonSortNum";
  public static final String VARIANT_TYPE = "http://bibfra.me/vocab/marc/variantType";
  public static final String RESPONSIBILITY_STATEMENT = "http://bibfra.me/vocab/marc/statementOfResponsibility";
  public static final String EDITION_STATEMENT = "http://bibfra.me/vocab/marc/edition";
  public static final String COPYRIGHT_DATE = "http://bibfra.me/vocab/lite/copyrightDate";
  public static final String DIMENSIONS = "http://bibfra.me/vocab/marc/dimensions";
  public static final String PROJECTED_PROVISION_DATE = "http://bibfra.me/vocab/bflc/projectedProvisionDate";
  public static final String MEDIA = "http://bibfra.me/vocab/marc/mediaType";
  public static final String CARRIER = "http://bibfra.me/vocab/marc/carrier";
  public static final String ISSUANCE = "http://bibfra.me/vocab/marc/issuance";
  public static final String LINK = "http://bibfra.me/vocab/lite/link";
  public static final String NAME = "http://bibfra.me/vocab/lite/name";
  public static final String SIMPLE_DATE = "http://bibfra.me/vocab/lite/simpleDate";
  public static final String SIMPLE_PLACE = "http://bibfra.me/vocab/bflc/simplePlace";
  public static final String LABEL = "http://bibfra.me/vocab/lite/label";

  // fields
  public static final String ID = "id";
  public static final String TYPE = "type";
}
