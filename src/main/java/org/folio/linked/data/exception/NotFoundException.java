package org.folio.linked.data.exception;

import org.folio.linked.data.model.ErrorCode;

public class NotFoundException extends BaseLinkedDataException {

  public NotFoundException(String message) {
    super(message, ErrorCode.NOT_FOUND_ERROR);
  }

}
