package com.retail.myRetail.controller;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.myRetail.model.Price;
import com.retail.myRetail.model.Product;
import com.retail.myRetail.service.RetailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RetailControllerSpec {

    @Mock
    private RetailService retailService;

    @InjectMocks
    private RetailController retailController;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mapper = new ObjectMapper(new JsonFactory());
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.retailController).build();
    }

    @Test
    public void shouldGetProduct() throws Exception {

        long id = 13860428;

        Price price = new Price();
        price.setId(13860428);
        price.setValue(13.0);
        price.setCurrencyCode("USD");

        Product product = new Product();
        product.setId(id);
        product.setName("The Big Lebowski");
        product.setPrice(price);


        when(retailService.getProductDetail(13860428)).thenReturn(product);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/products/13860428");

        MvcResult result = this.mockMvc.perform(builder).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        Product productResponse = mapper.readValue(content, new TypeReference<Product>() {
        });


        assert (productResponse.getName().equals("The Big Lebowski"));
        assert (productResponse.getId() == 13860428);
        assert (productResponse.getPrice().getValue() == 13.0);
        assert (productResponse.getPrice().getCurrencyCode().equals("USD"));
    }

}
