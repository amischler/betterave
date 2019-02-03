import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DistributionPlace } from 'app/shared/model/distribution-place.model';
import { DistributionPlaceService } from './distribution-place.service';
import { DistributionPlaceComponent } from './distribution-place.component';
import { DistributionPlaceDetailComponent } from './distribution-place-detail.component';
import { DistributionPlaceUpdateComponent } from './distribution-place-update.component';
import { DistributionPlaceDeletePopupComponent } from './distribution-place-delete-dialog.component';
import { IDistributionPlace } from 'app/shared/model/distribution-place.model';

@Injectable({ providedIn: 'root' })
export class DistributionPlaceResolve implements Resolve<IDistributionPlace> {
    constructor(private service: DistributionPlaceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDistributionPlace> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DistributionPlace>) => response.ok),
                map((distributionPlace: HttpResponse<DistributionPlace>) => distributionPlace.body)
            );
        }
        return of(new DistributionPlace());
    }
}

export const distributionPlaceRoute: Routes = [
    {
        path: '',
        component: DistributionPlaceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DistributionPlaces'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DistributionPlaceDetailComponent,
        resolve: {
            distributionPlace: DistributionPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DistributionPlaces'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DistributionPlaceUpdateComponent,
        resolve: {
            distributionPlace: DistributionPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DistributionPlaces'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DistributionPlaceUpdateComponent,
        resolve: {
            distributionPlace: DistributionPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DistributionPlaces'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const distributionPlacePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DistributionPlaceDeletePopupComponent,
        resolve: {
            distributionPlace: DistributionPlaceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DistributionPlaces'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
