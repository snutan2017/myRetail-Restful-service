package com.retail.myRetail.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.myRetail.exception.DataNotFoundException;
import com.retail.myRetail.model.Price;
import com.retail.myRetail.model.Product;
import com.retail.myRetail.repository.PriceRepository;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RetailServiceSpec {

    @Mock
    private PriceRepository priceRepository;


    @InjectMocks
    private RetailService retailService;

    @Mocked
    RestTemplate mockRestTemplate;

    String extrnalURL = "http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    @Test
    public void shouldReturnProductInformation() throws IOException {
        Price price = new Price();
        price.setId(13860428);
        price.setValue(13.0);
        price.setCurrencyCode("USD");


        new Expectations() {{
            mockRestTemplate.getForObject(extrnalURL, JsonNode.class);
            result = externalAPIResponseJson();
        }};

        when(priceRepository.findById(13860428L)).thenReturn(price);

        Product productRes = retailService.getProductDetail(13860428L);

        assert (productRes.getId() == 13860428);
        assert (productRes.getName().equals("Dummy Product"));
        assert (productRes.getPrice().getValue() == 13.0);
        assert (productRes.getPrice().getCurrencyCode().equals("USD"));
    }

    @Test
    public void returnExceptionIfEntityIsNotFound() {


        new Expectations() {{
            mockRestTemplate.getForObject(extrnalURL, JsonNode.class);
            result = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }};

        try {
            retailService.getProductDetail(12345678);
        } catch (Exception e) {
            assert (e instanceof DataNotFoundException);
        }
    }

    public JsonNode externalAPIResponseJson() throws IOException {
        String extURLResponse = "{\n" +
                "\"product\": {\n" +
                "\"available_to_promise_network\": {\n" +
                "\"product_id\": \"13860428\",\n" +
                "\"id_type\": \"TCIN\",\n" +
                "\"is_loyalty_purchase_enabled\": false\n" +
                "},\n" +
                "\"item\": {\n" +
                "\"tcin\": \"13860428\",\n" +
                "\"bundle_components\": {\n" +
                "\"is_assortment\": false,\n" +
                "\"is_kit_master\": false,\n" +
                "\"is_standard_item\": true,\n" +
                "\"is_component\": false\n" +
                "},\n" +
                "\"dpci\": \"058-34-0436\",\n" +
                "\"upc\": \"490583404362\",\n" +
                "\"product_description\": {\n" +
                "\"title\": \"Test Product\",\n" +
                "\"bullet_description\": [\n" +
                "\"<B>Movie Genre:</B> Comedy\",\n" +
                "\"<B>Software Format:</B> Blu-ray\",\n" +
                "\"<B>Movie Studio:</B> Universal Studios\"\n" +
                "],\n" +
                "\"general_description\": \"Blu-ray BIG LEBOWSKI, THE Movies\"\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser(extURLResponse);
        JsonNode actualObj = mapper.readTree(jp);
        return actualObj;
    }

}
