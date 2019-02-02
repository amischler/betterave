import { NgModule } from '@angular/core';

import { BetteraveSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [BetteraveSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [BetteraveSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class BetteraveSharedCommonModule {}
