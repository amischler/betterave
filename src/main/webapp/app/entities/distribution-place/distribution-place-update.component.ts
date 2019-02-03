import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IDistributionPlace } from 'app/shared/model/distribution-place.model';
import { DistributionPlaceService } from './distribution-place.service';

@Component({
    selector: 'jhi-distribution-place-update',
    templateUrl: './distribution-place-update.component.html'
})
export class DistributionPlaceUpdateComponent implements OnInit {
    distributionPlace: IDistributionPlace;
    isSaving: boolean;

    constructor(protected distributionPlaceService: DistributionPlaceService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ distributionPlace }) => {
            this.distributionPlace = distributionPlace;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.distributionPlace.id !== undefined) {
            this.subscribeToSaveResponse(this.distributionPlaceService.update(this.distributionPlace));
        } else {
            this.subscribeToSaveResponse(this.distributionPlaceService.create(this.distributionPlace));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDistributionPlace>>) {
        result.subscribe((res: HttpResponse<IDistributionPlace>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
