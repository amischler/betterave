import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Distribution } from 'app/shared/model/distribution.model';
import { DistributionService } from './distribution.service';
import { DistributionComponent } from './distribution.component';
import { DistributionDetailComponent } from './distribution-detail.component';
import { DistributionUpdateComponent } from './distribution-update.component';
import { DistributionDeletePopupComponent } from './distribution-delete-dialog.component';
import { IDistribution } from 'app/shared/model/distribution.model';

@Injectable({ providedIn: 'root' })
export class DistributionResolve implements Resolve<IDistribution> {
    constructor(private service: DistributionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDistribution> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Distribution>) => response.ok),
                map((distribution: HttpResponse<Distribution>) => distribution.body)
            );
        }
        return of(new Distribution());
    }
}

export const distributionRoute: Routes = [
    {
        path: '',
        component: DistributionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Distributions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DistributionDetailComponent,
        resolve: {
            distribution: DistributionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Distributions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DistributionUpdateComponent,
        resolve: {
            distribution: DistributionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Distributions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DistributionUpdateComponent,
        resolve: {
            distribution: DistributionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Distributions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const distributionPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DistributionDeletePopupComponent,
        resolve: {
            distribution: DistributionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Distributions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
