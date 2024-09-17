package ma.zyn.app.unit.service.impl.admin.money;

import ma.zyn.app.bean.core.money.Purchase;
import ma.zyn.app.dao.facade.core.money.PurchaseDao;
import ma.zyn.app.service.impl.admin.money.PurchaseAdminServiceImpl;

import ma.zyn.app.bean.core.money.PurchaseState ;
import ma.zyn.app.bean.core.money.Purchase ;
import ma.zyn.app.bean.core.catalog.Product ;
import ma.zyn.app.bean.core.money.PurchaseItem ;
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
class PurchaseAdminServiceImplTest {

    @Mock
    private PurchaseDao repository;
    private AutoCloseable autoCloseable;
    private PurchaseAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PurchaseAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllPurchase() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSavePurchase() {
        // Given
        Purchase toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeletePurchase() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetPurchaseById() {
        // Given
        Long idToRetrieve = 1L; // Example Purchase ID to retrieve
        Purchase expected = new Purchase(); // You need to replace Purchase with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Purchase result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Purchase constructSample(int i) {
		Purchase given = new Purchase();
        given.setReference("reference-"+i);
        given.setPurchaseDate(LocalDateTime.now());
        given.setImage("image-"+i);
        given.setTotal(BigDecimal.TEN);
        given.setDescription("description-"+i);
        List<PurchaseItem> purchaseItems = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                PurchaseItem element = new PurchaseItem();
                                                element.setId((long)id);
                                                element.setProduct(new Product(Long.valueOf(1)));
                                                element.setPrice(new BigDecimal(2*10));
                                                element.setQuantity(new BigDecimal(3*10));
                                                element.setPurchase(new Purchase(Long.valueOf(4)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setPurchaseItems(purchaseItems);
        given.setPurchaseState(new PurchaseState(1L));
        given.setEnabled(false);
        return given;
    }

}
