package ma.zyn.app.unit.ws.facade.admin.catalog;

import ma.zyn.app.bean.core.catalog.Product;
import ma.zyn.app.service.impl.admin.catalog.ProductAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.catalog.ProductRestAdmin;
import ma.zyn.app.ws.converter.catalog.ProductConverter;
import ma.zyn.app.ws.dto.catalog.ProductDto;
import org.aspectj.lang.annotation.Before;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private ProductAdminServiceImpl service;
    @Mock
    private ProductConverter converter;

    @InjectMocks
    private ProductRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllProductTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<ProductDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<ProductDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveProductTest() throws Exception {
        // Mock data
        ProductDto requestDto = new ProductDto();
        Product entity = new Product();
        Product saved = new Product();
        ProductDto savedDto = new ProductDto();

        // Mock the converter to return the product object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved product DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<ProductDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        ProductDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved product DTO
        assertEquals(savedDto.getCode(), responseBody.getCode());
        assertEquals(savedDto.getReference(), responseBody.getReference());
    }

}
