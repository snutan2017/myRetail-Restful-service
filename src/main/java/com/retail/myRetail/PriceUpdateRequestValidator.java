package com.retail.myRetail;

import com.retail.myRetail.message.RetailErrorCode;
import com.retail.myRetail.model.ProductPriceUpdatePayload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Class is responsible for Price validation
 */

public class PriceUpdateRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductPriceUpdatePayload.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ProductPriceUpdatePayload productPriceUpdatePayload = (ProductPriceUpdatePayload) target;
        validateCurrencyCode(productPriceUpdatePayload, errors);
        validateCurrencyValue(productPriceUpdatePayload, errors);
    }

    /**
     * Validate currency code blank
     *
     * @param productPriceUpdatePayload
     * @param errors
     */
    private void validateCurrencyCode(ProductPriceUpdatePayload productPriceUpdatePayload, Errors errors) {
        if (StringUtils.isBlank(productPriceUpdatePayload.getCurrency_code())) {
            errors.rejectValue("currency_code", RetailErrorCode.REQUIRED);
        }
    }

    /**
     * Validate currency value is valid
     *
     * @param productPriceUpdatePayload
     * @param errors
     */
    private void validateCurrencyValue(ProductPriceUpdatePayload productPriceUpdatePayload, Errors errors) {
        if (productPriceUpdatePayload.getValue() == null) {
            errors.rejectValue("value", RetailErrorCode.REQUIRED);
        } else if (productPriceUpdatePayload.getValue().isNaN()) {
            errors.rejectValue("value", RetailErrorCode.NOTDOUBLE);
        }
    }

}
