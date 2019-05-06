package com.retail.myRetail.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.retail.myRetail.exception.DataNotFoundException;
import com.retail.myRetail.model.Price;
import com.retail.myRetail.model.Product;
import com.retail.myRetail.model.ProductPriceUpdatePayload;
import com.retail.myRetail.repository.PriceRepository;
import com.retail.myRetail.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RetailService {

    Logger logger = LoggerFactory.getLogger(RetailService.class);

    @Autowired
    private PriceRepository priceRepository;

    /**
     * Get Product data for given id.
     * <p>
     * Steps:-
     * retrieve the product name from an external API.
     * retrieve product price from data store(mongodb)
     * and return product object containing product information(id, name,price)
     *
     * @param id
     * @return
     */
    public Product getProductDetail(long id) {

        //retrieve the product name from an external API.
        logger.info("Get product details starts....");
        String name = getProductInfo(id);


        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(getPrice(id));

        return product;
    }

    /**
     * Update Product price in Database
     * <p>
     * Check if the information about product with given id exists in data store(database)
     * if it does not exist throw DataNotFound Exception
     * <p>
     * if it exists updateProductPrice the product’s price in the data store.
     *
     * @param id
     * @param productPriceUpdatePayload
     */
    public void updateProductPrice(long id, ProductPriceUpdatePayload productPriceUpdatePayload) {

        logger.info("Update product details starts....");
        Price price = priceRepository.findById(id);

        if (price == null) {
            throw new DataNotFoundException();
        }

        price.setValue(productPriceUpdatePayload.getValue());
        price.setCurrencyCode(productPriceUpdatePayload.getCurrency_code());

        logger.info("Save product details ");
        priceRepository.save(price);
        logger.info("Update product details ends....");

    }

    /**
     * Get price information for given id for data store(mongodb)
     *
     * @param id
     * @return
     */
    private Price getPrice(long id) {
        return priceRepository.findById(id);
    }

    /**
     * Performs an HTTP GET to retrieve the product name from an external API.
     *
     * @param id
     * @return
     */
    private String getProductInfo(long id) {

        StringBuffer URL = new StringBuffer(AppConstants.BASE_URL);
        URL.append(id);
        URL.append(AppConstants.QUERY_DATA);

        String urlString = String.valueOf(URL);
        // Use RestTemplate to make the HTTP get request and JSONNode to read the name from the retrieved response
        RestTemplate restTemplate = new RestTemplate();
        try {
            JsonNode root = restTemplate.getForObject(urlString, JsonNode.class);
            String name = root.at(AppConstants.PRICE_INFO_URL).asText();
            logger.info("Get Product details ends..");
            return name;


        } catch (Exception e) {
            //If the external API gives Not Found exception then throw DataNotFoundException.
            throw new DataNotFoundException(e.getMessage(), e);
        }
    }
}
