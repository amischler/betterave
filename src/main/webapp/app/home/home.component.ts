import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LoginModalService, AccountService, Account } from 'app/core';

import { IDistribution } from 'app/shared/model/distribution.model';
import { DistributionService } from 'app/entities/distribution/distribution.service';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { IUser } from 'app/core/user/user.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    distributions: IDistribution[];
    futureDistributions: IDistribution[];
    pastDistributions: IDistribution[];

    constructor(
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private distributionService: DistributionService,
        private jhiAlertService: JhiAlertService
    ) {}

    loadDistributions() {
        this.distributionService
            .loadByUserId(this.account.id)
            .pipe(
                filter((res: HttpResponse<IDistribution[]>) => res.ok),
                map((res: HttpResponse<IDistribution[]>) => res.body)
            )
            .subscribe(
                (res: IDistribution[]) => {
                    this.distributions = res;
                    const now = new Date();
                    this.futureDistributions = res.filter(d => d.startDate.toDate() >= now);
                    this.pastDistributions = res.filter(d => d.startDate.toDate() < now);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
            if (account) {
                this.loadDistributions();
            }
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
                if (account) {
                    this.loadDistributions();
                }
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
