import {PurchaseStateDto} from './PurchaseState.model';
import {PurchaseItemDto} from './PurchaseItem.model';

import {BaseDto} from 'src/app/zynerator/dto/BaseDto.model';


export class PurchaseDto extends BaseDto{

    public reference: string;

   public purchaseDate: Date;

    public image: string;

    public total: null | number;

    public description: string;

   public enabled: null | boolean;

    public purchaseState: PurchaseStateDto ;
     public purchaseItems: Array<PurchaseItemDto>;


    constructor() {
        super();

        this.reference = '';
        this.purchaseDate = null;
        this.image = '';
        this.total = null;
        this.description = '';
        this.enabled = null;
        this.purchaseState = new PurchaseStateDto() ;
        this.purchaseItems = new Array<PurchaseItemDto>();

        }

}
