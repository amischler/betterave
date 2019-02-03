import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetteraveSharedModule } from 'app/shared';
import {
    DistributionComponent,
    DistributionDetailComponent,
    DistributionUpdateComponent,
    DistributionDeletePopupComponent,
    DistributionDeleteDialogComponent,
    distributionRoute,
    distributionPopupRoute
} from './';

const ENTITY_STATES = [...distributionRoute, ...distributionPopupRoute];

@NgModule({
    imports: [BetteraveSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DistributionComponent,
        DistributionDetailComponent,
        DistributionUpdateComponent,
        DistributionDeleteDialogComponent,
        DistributionDeletePopupComponent
    ],
    entryComponents: [
        DistributionComponent,
        DistributionUpdateComponent,
        DistributionDeleteDialogComponent,
        DistributionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetteraveDistributionModule {}
