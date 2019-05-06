package com.retail.myRetail.controller;


import com.retail.myRetail.PriceUpdateRequestValidator;
import com.retail.myRetail.model.Product;
import com.retail.myRetail.model.ProductPriceUpdatePayload;
import com.retail.myRetail.service.RetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
public class RetailController {

    @Autowired
    private RetailService retailService;

    /**
     * Endpoint to get product details
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        return new ResponseEntity(retailService.getProductDetail(id), HttpStatus.OK);
    }

    /**
     * Endpoint to update price
     *
     * @param id
     * @param productPriceUpdatePayload
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePrice(@PathVariable Long id,
                                      @RequestBody @Valid ProductPriceUpdatePayload productPriceUpdatePayload) {
        retailService.updateProductPrice(id, productPriceUpdatePayload);
        return new ResponseEntity(retailService.getProductDetail(id), HttpStatus.OK);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new PriceUpdateRequestValidator());
    }
}
