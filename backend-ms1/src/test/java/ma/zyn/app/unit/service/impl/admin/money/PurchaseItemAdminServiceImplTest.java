package ma.zyn.app.unit.service.impl.admin.money;

import ma.zyn.app.bean.core.money.PurchaseItem;
import ma.zyn.app.dao.facade.core.money.PurchaseItemDao;
import ma.zyn.app.service.impl.admin.money.PurchaseItemAdminServiceImpl;

import ma.zyn.app.bean.core.money.Purchase ;
import ma.zyn.app.bean.core.catalog.Product ;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;



import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class PurchaseItemAdminServiceImplTest {

    @Mock
    private PurchaseItemDao repository;
    private AutoCloseable autoCloseable;
    private PurchaseItemAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PurchaseItemAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllPurchaseItem() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSavePurchaseItem() {
        // Given
        PurchaseItem toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeletePurchaseItem() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetPurchaseItemById() {
        // Given
        Long idToRetrieve = 1L; // Example PurchaseItem ID to retrieve
        PurchaseItem expected = new PurchaseItem(); // You need to replace PurchaseItem with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        PurchaseItem result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private PurchaseItem constructSample(int i) {
		PurchaseItem given = new PurchaseItem();
        given.setProduct(new Product(1L));
        given.setPrice(BigDecimal.TEN);
        given.setQuantity(BigDecimal.TEN);
        given.setPurchase(new Purchase(1L));
        return given;
    }

}
