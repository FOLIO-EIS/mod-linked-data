package org.folio.linked.data.mapper.dto.monograph.instance.sub;

import static org.folio.ld.dictionary.PredicateDictionary.MEDIA;
import static org.folio.ld.dictionary.ResourceTypeDictionary.CATEGORY;

import org.folio.linked.data.domain.dto.Category;
import org.folio.linked.data.domain.dto.Instance;
import org.folio.linked.data.mapper.dto.common.CoreMapper;
import org.folio.linked.data.mapper.dto.common.MapperUnit;
import org.folio.linked.data.mapper.dto.monograph.common.CategoryMapperUnit;
import org.folio.linked.data.service.HashService;
import org.springframework.stereotype.Component;

@Component
@MapperUnit(type = CATEGORY, predicate = MEDIA, dtoClass = Category.class)
public class MediaMapperUnit extends CategoryMapperUnit {

  private static final String CATEGORY_SET_LABEL = "rdamedia";
  private static final String CATEGORY_SET_LINK = "http://id.loc.gov/vocabulary/genreFormSchemes/rdamedia";
  private static final String MEDIA_TYPE_LINK_PREFIX = "http://id.loc.gov/vocabulary/mediaTypes/";

  public MediaMapperUnit(CoreMapper coreMapper, HashService hashService) {
    super(coreMapper, hashService);
  }

  @Override
  protected String getLabel() {
    return CATEGORY_SET_LABEL;
  }

  @Override
  protected String getLink() {
    return CATEGORY_SET_LINK;
  }

  @Override
  protected void addToParent(Category category, Object parentDto) {
    if (parentDto instanceof Instance instance) {
      instance.addMediaItem(category);
    }
  }

  @Override
  public String getLinkPrefix() {
    return MEDIA_TYPE_LINK_PREFIX;
  }
}
