import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDistribution } from 'app/shared/model/distribution.model';

@Component({
    selector: 'jhi-distribution-detail',
    templateUrl: './distribution-detail.component.html'
})
export class DistributionDetailComponent implements OnInit {
    distribution: IDistribution;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ distribution }) => {
            this.distribution = distribution;
        });
    }

    previousState() {
        window.history.back();
    }
}
