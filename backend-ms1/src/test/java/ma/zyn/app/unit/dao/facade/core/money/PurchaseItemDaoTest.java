package ma.zyn.app.unit.dao.facade.core.money;

import ma.zyn.app.bean.core.money.PurchaseItem;
import ma.zyn.app.dao.facade.core.money.PurchaseItemDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.time.LocalDateTime;

import ma.zyn.app.bean.core.money.Purchase ;
import ma.zyn.app.bean.core.catalog.Product ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PurchaseItemDaoTest {

@Autowired
    private PurchaseItemDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        PurchaseItem entity = new PurchaseItem();
        entity.setId(id);
        underTest.save(entity);
        PurchaseItem loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        PurchaseItem entity = new PurchaseItem();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        PurchaseItem loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<PurchaseItem> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<PurchaseItem> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        PurchaseItem given = constructSample(1);
        PurchaseItem saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
