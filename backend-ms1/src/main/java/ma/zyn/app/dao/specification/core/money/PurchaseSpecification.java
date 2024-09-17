package  ma.zyn.app.dao.specification.core.money;

import ma.zyn.app.dao.criteria.core.money.PurchaseCriteria;
import ma.zyn.app.bean.core.money.Purchase;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PurchaseSpecification extends  AbstractSpecification<PurchaseCriteria, Purchase>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("reference", criteria.getReference(),criteria.getReferenceLike());
        addPredicate("purchaseDate", criteria.getPurchaseDate(), criteria.getPurchaseDateFrom(), criteria.getPurchaseDateTo());
        addPredicate("image", criteria.getImage(),criteria.getImageLike());
        addPredicateBigDecimal("total", criteria.getTotal(), criteria.getTotalMin(), criteria.getTotalMax());
        addPredicateBool("enabled", criteria.getEnabled());
        addPredicateFk("purchaseState","id", criteria.getPurchaseState()==null?null:criteria.getPurchaseState().getId());
        addPredicateFk("purchaseState","id", criteria.getPurchaseStates());
        addPredicateFk("purchaseState","code", criteria.getPurchaseState()==null?null:criteria.getPurchaseState().getCode());
    }

    public PurchaseSpecification(PurchaseCriteria criteria) {
        super(criteria);
    }

    public PurchaseSpecification(PurchaseCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
