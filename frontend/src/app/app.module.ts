import {DropdownModule} from 'primeng/dropdown';
import {ButtonModule} from 'primeng/button';
import {ToolbarModule} from 'primeng/toolbar';
import {InputTextareaModule} from 'primeng/inputtextarea';

import {FileUploadModule} from 'primeng/fileupload';
import {SelectButtonModule} from 'primeng/selectbutton';
import {PanelMenuModule} from 'primeng/panelmenu';
import {CalendarModule} from 'primeng/calendar';
import {TabViewModule} from 'primeng/tabview';
import {InputSwitchModule} from 'primeng/inputswitch';
import {InputTextModule} from 'primeng/inputtext';
import {ToastModule} from 'primeng/toast';
import {PanelModule} from 'primeng/panel';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {TableModule} from 'primeng/table';
import {CardModule} from 'primeng/card';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {PasswordModule} from 'primeng/password';
import {MessageModule} from 'primeng/message';
import {RadioButtonModule} from 'primeng/radiobutton';
import {SplitButtonModule} from 'primeng/splitbutton';
import {DialogModule} from 'primeng/dialog';
import {ConfirmationService, MessageService} from 'primeng/api';

import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {Injector, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule, DatePipe} from '@angular/common';

import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AppLayoutModule } from './layout/app.layout.module';

import {RoleService} from './zynerator/security/shared/service/Role.service';
import {UserService} from './zynerator/security/shared/service/User.service';
import {ServiceLocator} from './zynerator/service/ServiceLocator';
import {JwtInterceptor} from './zynerator/security/interceptors/jwt.interceptor';

import {MenuService} from './layout/app.menu.service';

import {SpinnerComponent} from './zynerator/spinner/spinner.component';
import { HttpRequestInterceptor } from './zynerator/spinner/http.interceptor';

import {PublicModule} from "./public/public.module";
import {PublicRoutingModule} from "./public/public-routing.module";

import {AdminModule} from './module/admin/admin.module';
import {AdminRoutingModule} from './module/admin/admin-routing.module';

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}
@NgModule({
imports: [
    AppRoutingModule,
    AppLayoutModule,
    ButtonModule,
    PasswordModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    BrowserModule,
    PanelMenuModule,
    RadioButtonModule,
    InputTextModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    DropdownModule,
    TabViewModule,
    SplitButtonModule,
    InputSwitchModule,
    InputTextareaModule,
    CalendarModule,
    PanelModule,
    MessageModule,
    CardModule,
    ToolbarModule,
    TableModule,
    DialogModule,
    ConfirmDialogModule,
    ToastModule,
    FileUploadModule,
    SelectButtonModule,

    PublicModule,
    PublicRoutingModule,

    AdminModule,
    AdminRoutingModule,

  TranslateModule.forRoot({
  loader: {
    provide: TranslateLoader,
    useFactory: HttpLoaderFactory,
    deps: [HttpClient]
  }
  })
],
declarations: [
    AppComponent,
    SpinnerComponent,
],
providers: [
/*    { provide: LocationStrategy, useClass: HashLocationStrategy }, */
    { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},

    UserService,
    MenuService,
    RoleService,
    MessageService,
    ConfirmationService,
    DatePipe
],
bootstrap: [AppComponent],
  exports: [
  ]
})
export class AppModule{
  constructor(private injector: Injector) {
    ServiceLocator.injector = this.injector;
  }
}

