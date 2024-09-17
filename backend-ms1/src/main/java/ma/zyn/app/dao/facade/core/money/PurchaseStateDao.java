package ma.zyn.app.dao.facade.core.money;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.money.PurchaseState;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.money.PurchaseState;
import java.util.List;


@Repository
public interface PurchaseStateDao extends AbstractRepository<PurchaseState,Long>  {
    PurchaseState findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW PurchaseState(item.id,item.libelle) FROM PurchaseState item")
    List<PurchaseState> findAllOptimized();

}
