package org.folio.linked.data.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.folio.linked.data.validation.dto.PrimaryTitleDtoValidator;
import org.folio.linked.data.validation.entity.PrimaryTitleEntityValidator;

@Documented
@Constraint(validatedBy = {PrimaryTitleDtoValidator.class, PrimaryTitleEntityValidator.class})
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryTitleConstraint {

  String message() default "Primary main title should be presented";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
