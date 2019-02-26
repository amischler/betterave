import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IDistribution } from 'app/shared/model/distribution.model';
import { DistributionService } from './distribution.service';
import { IDistributionPlace } from 'app/shared/model/distribution-place.model';
import { DistributionPlaceService } from 'app/entities/distribution-place';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-distribution-update',
    templateUrl: './distribution-update.component.html'
})
export class DistributionUpdateComponent implements OnInit {
    distribution: IDistribution;
    isSaving: boolean;
    bulk: boolean;

    distributionplaces: IDistributionPlace[];

    users: IUser[];
    dateDp: any;
    endDate: string;
    startDate: string;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected distributionService: DistributionService,
        protected distributionPlaceService: DistributionPlaceService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ distribution }) => {
            this.distribution = distribution;
            this.endDate = this.distribution.endDate != null ? this.distribution.endDate.format(DATE_TIME_FORMAT) : null;
            this.startDate = this.distribution.startDate != null ? this.distribution.startDate.format(DATE_TIME_FORMAT) : null;
        });
        this.distributionPlaceService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDistributionPlace[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDistributionPlace[]>) => response.body)
            )
            .subscribe(
                (res: IDistributionPlace[]) => (this.distributionplaces = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.distribution.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        this.distribution.startDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        if (this.distribution.id !== undefined) {
            this.subscribeToSaveResponse(this.distributionService.update(this.distribution));
        } else {
            if (this.bulk) {
                this.subscribeToSaveResponse(this.distributionService.createMultiple(this.distribution));
            } else {
                this.subscribeToSaveResponse(this.distributionService.create(this.distribution));
            }
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDistribution>>) {
        result.subscribe((res: HttpResponse<IDistribution>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDistributionPlaceById(index: number, item: IDistributionPlace) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
