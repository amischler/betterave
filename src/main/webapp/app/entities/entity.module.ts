import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'distribution-place',
                loadChildren: './distribution-place/distribution-place.module#BetteraveDistributionPlaceModule'
            },
            {
                path: 'distribution',
                loadChildren: './distribution/distribution.module#BetteraveDistributionModule'
            },
            {
                path: 'comment',
                loadChildren: './comment/comment.module#BetteraveCommentModule'
            },
            {
                path: 'distribution-place',
                loadChildren: './distribution-place/distribution-place.module#BetteraveDistributionPlaceModule'
            },
            {
                path: 'distribution',
                loadChildren: './distribution/distribution.module#BetteraveDistributionModule'
            },
            {
                path: 'comment',
                loadChildren: './comment/comment.module#BetteraveCommentModule'
            },
            {
                path: 'distribution-place',
                loadChildren: './distribution-place/distribution-place.module#BetteraveDistributionPlaceModule'
            },
            {
                path: 'comment',
                loadChildren: './comment/comment.module#BetteraveCommentModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BetteraveEntityModule {}
