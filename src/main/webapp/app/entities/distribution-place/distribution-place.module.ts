import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetteraveSharedModule } from 'app/shared';
import {
    DistributionPlaceComponent,
    DistributionPlaceDetailComponent,
    DistributionPlaceUpdateComponent,
    DistributionPlaceDeletePopupComponent,
    DistributionPlaceDeleteDialogComponent,
    distributionPlaceRoute,
    distributionPlacePopupRoute
} from './';

const ENTITY_STATES = [...distributionPlaceRoute, ...distributionPlacePopupRoute];

@NgModule({
    imports: [BetteraveSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DistributionPlaceComponent,
        DistributionPlaceDetailComponent,
        DistributionPlaceUpdateComponent,
        DistributionPlaceDeleteDialogComponent,
        DistributionPlaceDeletePopupComponent
    ],
    entryComponents: [
        DistributionPlaceComponent,
        DistributionPlaceUpdateComponent,
        DistributionPlaceDeleteDialogComponent,
        DistributionPlaceDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetteraveDistributionPlaceModule {}
