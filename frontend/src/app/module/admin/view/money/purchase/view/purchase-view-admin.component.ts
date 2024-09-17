import {Component, OnInit} from '@angular/core';


import {DatePipe} from '@angular/common';
import {Router} from '@angular/router';
import {Inject, Injectable, PLATFORM_ID} from '@angular/core';


import {environment} from 'src/environments/environment';

import {RoleService} from 'src/app/zynerator/security/shared/service/Role.service';
import {AbstractService} from 'src/app/zynerator/service/AbstractService';
import {BaseDto} from 'src/app/zynerator/dto/BaseDto.model';
import {BaseCriteria} from 'src/app/zynerator/criteria/BaseCriteria.model';
import {StringUtilService} from 'src/app/zynerator/util/StringUtil.service';
import {ServiceLocator} from 'src/app/zynerator/service/ServiceLocator';
import {ConfirmationService, MessageService,MenuItem} from 'primeng/api';
import {FileTempDto} from 'src/app/zynerator/dto/FileTempDto.model';


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
  selector: 'app-purchase-view-admin',
  templateUrl: './purchase-view-admin.component.html'
})
export class PurchaseViewAdminComponent implements OnInit {


	protected _submitted = false;
    protected _errorMessages = new Array<string>();

    protected datePipe: DatePipe;
    protected messageService: MessageService;
    protected confirmationService: ConfirmationService;
    protected roleService: RoleService;
    protected router: Router;
    protected stringUtilService: StringUtilService;


    purchaseItems = new PurchaseItemDto();
    purchaseItemss: Array<PurchaseItemDto> = [];

    constructor(private service: PurchaseAdminService, private purchaseStateService: PurchaseStateAdminService, private productService: ProductAdminService, private purchaseItemService: PurchaseItemAdminService){
		this.datePipe = ServiceLocator.injector.get(DatePipe);
        this.messageService = ServiceLocator.injector.get(MessageService);
        this.confirmationService = ServiceLocator.injector.get(ConfirmationService);
        this.roleService = ServiceLocator.injector.get(RoleService);
        this.router = ServiceLocator.injector.get(Router);
        this.stringUtilService = ServiceLocator.injector.get(StringUtilService);
	}

    ngOnInit(): void {
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

    public hideViewDialog() {
        this.viewDialog = false;
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

    get viewDialog(): boolean {
        return this.service.viewDialog;
    }

    set viewDialog(value: boolean) {
        this.service.viewDialog = value;
    }

    get criteria(): PurchaseCriteria {
        return this.service.criteria;
    }

    set criteria(value: PurchaseCriteria) {
        this.service.criteria = value;
    }

    get dateFormat(){
        return environment.dateFormatView;
    }

    get dateFormatColumn(){
        return environment.dateFormatList;
    }


}
