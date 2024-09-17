package  ma.zyn.app.dao.specification.core.money;

import ma.zyn.app.dao.criteria.core.money.PurchaseStateCriteria;
import ma.zyn.app.bean.core.money.PurchaseState;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PurchaseStateSpecification extends  AbstractSpecification<PurchaseStateCriteria, PurchaseState>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("libelle", criteria.getLibelle(),criteria.getLibelleLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
    }

    public PurchaseStateSpecification(PurchaseStateCriteria criteria) {
        super(criteria);
    }

    public PurchaseStateSpecification(PurchaseStateCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
