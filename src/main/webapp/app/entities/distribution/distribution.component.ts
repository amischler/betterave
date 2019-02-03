import { Component, OnInit, OnDestroy } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IDistribution } from 'app/shared/model/distribution.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { DistributionService } from './distribution.service';

@Component({
    selector: 'jhi-distribution',
    templateUrl: './distribution.component.html'
})
export class DistributionComponent implements OnInit, OnDestroy {
    distributions: IDistribution[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    fromDate: string;
    toDate: string;

    constructor(
        protected distributionService: DistributionService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected accountService: AccountService,
        private datePipe: DatePipe
    ) {
        this.distributions = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    loadAll() {
        this.distributionService
            .query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort(),
                fromDate: this.fromDate,
                toDate: this.toDate
            })
            .subscribe(
                (res: HttpResponse<IDistribution[]>) => this.paginateDistributions(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    reset() {
        this.page = 0;
        this.distributions = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    ngOnInit() {
        this.today();
        this.nextYear();
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDistributions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDistribution) {
        return item.id;
    }

    registerChangeInDistributions() {
        this.eventSubscriber = this.eventManager.subscribe('distributionListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateDistributions(data: IDistribution[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.distributions.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    subscribe(distributionId) {
        this.distributionService.subscribe(distributionId).subscribe(
            (res: HttpResponse<IDistribution[]>) =>
                this.eventManager.broadcast({
                    name: 'distributionListModification',
                    content: 'Subscribed to a distribution'
                }),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    unsubscribe(distributionId) {
        this.distributionService.unsubscribe(distributionId).subscribe(
            (res: HttpResponse<IDistribution[]>) =>
                this.eventManager.broadcast({
                    name: 'distributionListModification',
                    content: 'Unsubscribed from a distribution'
                }),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    canSubscribe(distribution) {
        return (
            !distribution.users.some(user => {
                return user.login === this.currentAccount.login;
            }) && distribution.date > new Date()
        );
    }

    canUnsubscribe(distribution) {
        return (
            distribution.users.some(user => {
                return user.login === this.currentAccount.login;
            }) && distribution.date > new Date()
        );
    }

    today() {
        const dateFormat = 'yyyy-MM-dd';
        // Today
        const today: Date = new Date();
        const date = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        this.fromDate = this.datePipe.transform(date, dateFormat);
    }

    nextYear() {
        const dateFormat = 'yyyy-MM-dd';
        let fromDate: Date = new Date();
        fromDate = new Date(fromDate.getFullYear() + 1, fromDate.getMonth(), fromDate.getDate());
        this.toDate = this.datePipe.transform(fromDate, dateFormat);
    }

    transition() {
        this.reset();
    }
}
