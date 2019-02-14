import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BetteraveSharedModule } from 'app/shared';
import {
    DistributionComponent,
    DistributionDetailComponent,
    DistributionUpdateComponent,
    DistributionDeletePopupComponent,
    DistributionDeleteDialogComponent,
    DistributionCommentPopupComponent,
    DistributionCommentDialogComponent,
    distributionRoute,
    distributionPopupRoute,
    distributionCommentPopupRoute,
    editDistributionCommentPopupRoute
} from './';

const ENTITY_STATES = [
    ...distributionRoute,
    ...distributionPopupRoute,
    ...distributionCommentPopupRoute,
    ...editDistributionCommentPopupRoute
];

@NgModule({
    imports: [BetteraveSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DistributionComponent,
        DistributionDetailComponent,
        DistributionUpdateComponent,
        DistributionDeleteDialogComponent,
        DistributionDeletePopupComponent,
        DistributionCommentDialogComponent,
        DistributionCommentPopupComponent
    ],
    entryComponents: [
        DistributionComponent,
        DistributionUpdateComponent,
        DistributionDeleteDialogComponent,
        DistributionDeletePopupComponent,
        DistributionCommentDialogComponent,
        DistributionCommentPopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetteraveDistributionModule {}
