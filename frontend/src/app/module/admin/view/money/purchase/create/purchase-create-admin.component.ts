import {Component, OnInit, Input} from '@angular/core';
import {ConfirmationService, MessageService} from 'primeng/api';

import {DatePipe} from '@angular/common';
import {Router} from '@angular/router';
import {Inject, Injectable, PLATFORM_ID} from '@angular/core';


import {environment} from 'src/environments/environment';

import {RoleService} from 'src/app/zynerator/security/shared/service/Role.service';
import {StringUtilService} from 'src/app/zynerator/util/StringUtil.service';
import {ServiceLocator} from 'src/app/zynerator/service/ServiceLocator';




import {PurchaseAdminService} from 'src/app/shared/service/admin/money/PurchaseAdmin.service';
import {PurchaseDto} from 'src/app/shared/model/money/Purchase.model';
import {PurchaseCriteria} from 'src/app/shared/criteria/money/PurchaseCriteria.model';
import {PurchaseStateDto} from 'src/app/shared/model/money/PurchaseState.model';
import {PurchaseStateAdminService} from 'src/app/shared/service/admin/money/PurchaseStateAdmin.service';
import {ProductDto} from 'src/app/shared/model/catalog/Product.model';
import {ProductAdminService} from 'src/app/shared/service/admin/catalog/ProductAdmin.service';
import {PurchaseItemDto} from 'src/app/shared/model/money/PurchaseItem.model';
import {PurchaseItemAdminService} from 'src/app/shared/service/admin/money/PurchaseItemAdmin.service';
@Component({
  selector: 'app-purchase-create-admin',
  templateUrl: './purchase-create-admin.component.html'
})
export class PurchaseCreateAdminComponent  implements OnInit {

	protected _submitted = false;
    protected _errorMessages = new Array<string>();

    protected datePipe: DatePipe;
    protected messageService: MessageService;
    protected confirmationService: ConfirmationService;
    protected roleService: RoleService;
    protected router: Router;
    protected stringUtilService: StringUtilService;
    private _activeTab = 0;

    private _purchaseItemsElement = new PurchaseItemDto();


   private _validPurchaseReference = true;
    private _validPurchaseItemsProduct = true;
    private _validPurchaseItemsPrice = true;
    private _validPurchaseItemsQuantity = true;
    private _validPurchaseStateCode = true;
    private _validPurchaseStateLibelle = true;
    private _validPurchaseStateStyle = true;

	constructor(private service: PurchaseAdminService , private purchaseStateService: PurchaseStateAdminService, private productService: ProductAdminService, private purchaseItemService: PurchaseItemAdminService, @Inject(PLATFORM_ID) private platformId? ) {
        this.datePipe = ServiceLocator.injector.get(DatePipe);
        this.messageService = ServiceLocator.injector.get(MessageService);
        this.confirmationService = ServiceLocator.injector.get(ConfirmationService);
        this.roleService = ServiceLocator.injector.get(RoleService);
        this.router = ServiceLocator.injector.get(Router);
        this.stringUtilService = ServiceLocator.injector.get(StringUtilService);
    }

    ngOnInit(): void {
        this.purchaseItemsElement.product = new ProductDto();
        this.productService.findAll().subscribe((data) => this.products = data);
        this.purchaseStateService.findAll().subscribe((data) => this.purchaseStates = data);
    }



    public save(): void {
        this.submitted = true;
        this.validateForm();
        if (this.errorMessages.length === 0) {
            this.saveWithShowOption(false);
        } else {
            this.messageService.add({severity: 'error',summary: 'Erreurs',detail: 'Merci de corrigé les erreurs sur le formulaire'});
        }
    }

    public saveWithShowOption(showList: boolean) {
        this.service.save().subscribe(item => {
            if (item != null) {
                this.items.push({...item});
                this.createDialog = false;
                this.submitted = false;
                this.item = new PurchaseDto();
            } else {
                this.messageService.add({severity: 'error', summary: 'Erreurs', detail: 'Element existant'});
            }

        }, error => {
            console.log(error);
        });
    }


    public hideCreateDialog() {
        this.createDialog = false;
        this.setValidation(true);
    }



    validatePurchaseItems(){
        this.errorMessages = new Array();
        this.validatePurchaseItemsProduct();
        this.validatePurchaseItemsPrice();
        this.validatePurchaseItemsQuantity();
    }


    public  setValidation(value: boolean){
        this.validPurchaseReference = value;
        this.validPurchaseItemsProduct = value;
        this.validPurchaseItemsPrice = value;
        this.validPurchaseItemsQuantity = value;
    }

    public addPurchaseItems() {
        if( this.item.purchaseItems == null )
            this.item.purchaseItems = new Array<PurchaseItemDto>();
       this.validatePurchaseItems();
       if (this.errorMessages.length === 0) {
              this.item.purchaseItems.push({... this.purchaseItemsElement});
              this.purchaseItemsElement = new PurchaseItemDto();
       }else{
            this.messageService.add({severity: 'error',summary: 'Erreurs',detail: 'Merci de corrigé les erreurs suivant : ' + this.errorMessages});
       }
    }


    public deletepurchaseItems(p: PurchaseItemDto) {
        this.item.purchaseItems.forEach((element, index) => {
            if (element === p) { this.item.purchaseItems.splice(index, 1); }
        });
    }

    public editpurchaseItems(p: PurchaseItemDto) {
        this.purchaseItemsElement = {... p};
        this.activeTab = 0;
    }


    public  validateForm(): void{
        this.errorMessages = new Array<string>();
        this.validatePurchaseReference();
    }

    public validatePurchaseReference(){
        if (this.stringUtilService.isEmpty(this.item.reference)) {
        this.errorMessages.push('Reference non valide');
        this.validPurchaseReference = false;
        } else {
            this.validPurchaseReference = true;
        }
    }

    public validatePurchaseItemsProduct(){
        if (this.purchaseItemsElement.product == null) {
            this.errorMessages.push('Product de la purchaseItem est  invalide');
            this.validPurchaseItemsProduct = false;
        } else {
            this.validPurchaseItemsProduct = true;
        }
    }
    public validatePurchaseItemsPrice(){
        if (this.purchaseItemsElement.price == null) {
            this.errorMessages.push('Price de la purchaseItem est  invalide');
            this.validPurchaseItemsPrice = false;
        } else {
            this.validPurchaseItemsPrice = true;
        }
    }
    public validatePurchaseItemsQuantity(){
        if (this.purchaseItemsElement.quantity == null) {
            this.errorMessages.push('Quantity de la purchaseItem est  invalide');
            this.validPurchaseItemsQuantity = false;
        } else {
            this.validPurchaseItemsQuantity = true;
        }
    }

    public async openCreatePurchaseState(purchaseState: string) {
    const isPermistted = await this.roleService.isPermitted('PurchaseState', 'add');
    if(isPermistted) {
         this.purchaseState = new PurchaseStateDto();
         this.createPurchaseStateDialog = true;
    }else{
        this.messageService.add({
        severity: 'error', summary: 'erreur', detail: 'problème de permission'
        });
     }
    }

    get product(): ProductDto {
        return this.productService.item;
    }
    set product(value: ProductDto) {
        this.productService.item = value;
    }
    get products(): Array<ProductDto> {
        return this.productService.items;
    }
    set products(value: Array<ProductDto>) {
        this.productService.items = value;
    }
    get createProductDialog(): boolean {
        return this.productService.createDialog;
    }
    set createProductDialog(value: boolean) {
        this.productService.createDialog= value;
    }
    get purchaseState(): PurchaseStateDto {
        return this.purchaseStateService.item;
    }
    set purchaseState(value: PurchaseStateDto) {
        this.purchaseStateService.item = value;
    }
    get purchaseStates(): Array<PurchaseStateDto> {
        return this.purchaseStateService.items;
    }
    set purchaseStates(value: Array<PurchaseStateDto>) {
        this.purchaseStateService.items = value;
    }
    get createPurchaseStateDialog(): boolean {
        return this.purchaseStateService.createDialog;
    }
    set createPurchaseStateDialog(value: boolean) {
        this.purchaseStateService.createDialog= value;
    }



    get validPurchaseReference(): boolean {
        return this._validPurchaseReference;
    }

    set validPurchaseReference(value: boolean) {
         this._validPurchaseReference = value;
    }

    get validPurchaseItemsProduct(): boolean {
        return this._validPurchaseItemsProduct;
    }
    set validPurchaseItemsProduct(value: boolean) {
        this._validPurchaseItemsProduct = value;
    }
    get validPurchaseItemsPrice(): boolean {
        return this._validPurchaseItemsPrice;
    }
    set validPurchaseItemsPrice(value: boolean) {
        this._validPurchaseItemsPrice = value;
    }
    get validPurchaseItemsQuantity(): boolean {
        return this._validPurchaseItemsQuantity;
    }
    set validPurchaseItemsQuantity(value: boolean) {
        this._validPurchaseItemsQuantity = value;
    }
    get validPurchaseStateCode(): boolean {
        return this._validPurchaseStateCode;
    }
    set validPurchaseStateCode(value: boolean) {
        this._validPurchaseStateCode = value;
    }
    get validPurchaseStateLibelle(): boolean {
        return this._validPurchaseStateLibelle;
    }
    set validPurchaseStateLibelle(value: boolean) {
        this._validPurchaseStateLibelle = value;
    }
    get validPurchaseStateStyle(): boolean {
        return this._validPurchaseStateStyle;
    }
    set validPurchaseStateStyle(value: boolean) {
        this._validPurchaseStateStyle = value;
    }

    get purchaseItemsElement(): PurchaseItemDto {
        if( this._purchaseItemsElement == null )
            this._purchaseItemsElement = new PurchaseItemDto();
        return this._purchaseItemsElement;
    }

    set purchaseItemsElement(value: PurchaseItemDto) {
        this._purchaseItemsElement = value;
    }

    get items(): Array<PurchaseDto> {
        return this.service.items;
    }

    set items(value: Array<PurchaseDto>) {
        this.service.items = value;
    }

    get item(): PurchaseDto {
        return this.service.item;
    }

    set item(value: PurchaseDto) {
        this.service.item = value;
    }

    get createDialog(): boolean {
        return this.service.createDialog;
    }

    set createDialog(value: boolean) {
        this.service.createDialog = value;
    }

    get criteria(): PurchaseCriteria {
        return this.service.criteria;
    }

    set criteria(value: PurchaseCriteria) {
        this.service.criteria = value;
    }

    get dateFormat() {
        return environment.dateFormatCreate;
    }

    get dateFormatColumn() {
        return environment.dateFormatCreate;
    }

    get submitted(): boolean {
        return this._submitted;
    }

    set submitted(value: boolean) {
        this._submitted = value;
    }

    get errorMessages(): string[] {
        if (this._errorMessages == null) {
            this._errorMessages = new Array<string>();
        }
        return this._errorMessages;
    }

    set errorMessages(value: string[]) {
        this._errorMessages = value;
    }

    get validate(): boolean {
        return this.service.validate;
    }

    set validate(value: boolean) {
        this.service.validate = value;
    }


    get activeTab(): number {
        return this._activeTab;
    }

    set activeTab(value: number) {
        this._activeTab = value;
    }

}
