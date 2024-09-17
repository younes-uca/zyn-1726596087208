package ma.zyn.app.unit.ws.facade.admin.money;

import ma.zyn.app.bean.core.money.Purchase;
import ma.zyn.app.service.impl.admin.money.PurchaseAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.money.PurchaseRestAdmin;
import ma.zyn.app.ws.converter.money.PurchaseConverter;
import ma.zyn.app.ws.dto.money.PurchaseDto;
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
public class PurchaseRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private PurchaseAdminServiceImpl service;
    @Mock
    private PurchaseConverter converter;

    @InjectMocks
    private PurchaseRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllPurchaseTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<PurchaseDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<PurchaseDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSavePurchaseTest() throws Exception {
        // Mock data
        PurchaseDto requestDto = new PurchaseDto();
        Purchase entity = new Purchase();
        Purchase saved = new Purchase();
        PurchaseDto savedDto = new PurchaseDto();

        // Mock the converter to return the purchase object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved purchase DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<PurchaseDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        PurchaseDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved purchase DTO
        assertEquals(savedDto.getReference(), responseBody.getReference());
        assertEquals(savedDto.getPurchaseDate(), responseBody.getPurchaseDate());
        assertEquals(savedDto.getImage(), responseBody.getImage());
        assertEquals(savedDto.getTotal(), responseBody.getTotal());
        assertEquals(savedDto.getDescription(), responseBody.getDescription());
        assertEquals(savedDto.getEnabled(), responseBody.getEnabled());
    }

}
