package com.api1.validator;

import java.sql.Date;
import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * ProductExpiryDateValidator Class. Checks the given date is expired or not.
 *
 */
public class ProductExpiryDateValidator implements ConstraintValidator<ProductExpiryDate, String> {

	@Override
	public boolean isValid(String productExpiryDate, ConstraintValidatorContext context) {
		if (!productExpiryDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			return false;
		}
		if (Date.valueOf(productExpiryDate).before(Date.valueOf(LocalDate.now()))) {
			return false;
		}
		return true;

	}
}
