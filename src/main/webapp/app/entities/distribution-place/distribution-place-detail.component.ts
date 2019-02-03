import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDistributionPlace } from 'app/shared/model/distribution-place.model';

@Component({
    selector: 'jhi-distribution-place-detail',
    templateUrl: './distribution-place-detail.component.html'
})
export class DistributionPlaceDetailComponent implements OnInit {
    distributionPlace: IDistributionPlace;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ distributionPlace }) => {
            this.distributionPlace = distributionPlace;
        });
    }

    previousState() {
        window.history.back();
    }
}
