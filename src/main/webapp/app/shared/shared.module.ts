import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { BetteraveSharedLibsModule, BetteraveSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { AccountFormatPipe } from './pipes/account-format';

@NgModule({
    imports: [BetteraveSharedLibsModule, BetteraveSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, AccountFormatPipe],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }, { provide: AccountFormatPipe }],
    entryComponents: [JhiLoginModalComponent],
    exports: [BetteraveSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, AccountFormatPipe],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetteraveSharedModule {
    static forRoot() {
        return {
            ngModule: BetteraveSharedModule
        };
    }
}
