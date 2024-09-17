package ma.zyn.app.unit.ws.facade.admin.money;

import ma.zyn.app.bean.core.money.PurchaseItem;
import ma.zyn.app.service.impl.admin.money.PurchaseItemAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.money.PurchaseItemRestAdmin;
import ma.zyn.app.ws.converter.money.PurchaseItemConverter;
import ma.zyn.app.ws.dto.money.PurchaseItemDto;
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
public class PurchaseItemRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private PurchaseItemAdminServiceImpl service;
    @Mock
    private PurchaseItemConverter converter;

    @InjectMocks
    private PurchaseItemRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllPurchaseItemTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<PurchaseItemDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<PurchaseItemDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSavePurchaseItemTest() throws Exception {
        // Mock data
        PurchaseItemDto requestDto = new PurchaseItemDto();
        PurchaseItem entity = new PurchaseItem();
        PurchaseItem saved = new PurchaseItem();
        PurchaseItemDto savedDto = new PurchaseItemDto();

        // Mock the converter to return the purchaseItem object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved purchaseItem DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<PurchaseItemDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        PurchaseItemDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved purchaseItem DTO
        assertEquals(savedDto.getPrice(), responseBody.getPrice());
        assertEquals(savedDto.getQuantity(), responseBody.getQuantity());
    }

}
