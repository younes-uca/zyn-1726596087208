import {PurchaseStateCriteria} from './PurchaseStateCriteria.model';
import {PurchaseItemCriteria} from './PurchaseItemCriteria.model';

import {BaseCriteria} from 'src/app/zynerator/criteria/BaseCriteria.model';

export class PurchaseCriteria extends BaseCriteria {

    public id: number;
    public reference: string;
    public referenceLike: string;
    public purchaseDate: Date;
    public purchaseDateFrom: Date;
    public purchaseDateTo: Date;
    public image: string;
    public imageLike: string;
     public total: number;
     public totalMin: number;
     public totalMax: number;
    public description: string;
    public descriptionLike: string;
    public enabled: null | boolean;
  public purchaseState: PurchaseStateCriteria ;
  public purchaseStates: Array<PurchaseStateCriteria> ;
      public purchaseItems: Array<PurchaseItemCriteria>;

}
