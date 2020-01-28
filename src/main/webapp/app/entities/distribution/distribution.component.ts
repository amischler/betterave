import { Component, OnInit, OnDestroy } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IDistribution, Type } from 'app/shared/model/distribution.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { DistributionService } from './distribution.service';
import { IDistributionPlace } from 'app/shared/model/distribution-place.model';
import { DistributionPlaceService } from 'app/entities/distribution-place';
import { CommentService } from 'app/entities/comment/comment.service';
import { IComment } from 'app/shared/model/comment.model';
import { LocalStorageService } from 'angular-2-local-storage';

import { AccountFormatPipe } from 'app/shared/pipes/account-format';

@Component({
    selector: 'jhi-distribution',
    templateUrl: './distribution.component.html'
})
export class DistributionComponent implements OnInit, OnDestroy {
    distributions: IDistribution[];
    currentAccount: any;
    eventSubscriber: Subscription;
    commentsEventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    fromDate: string;
    toDate: string;
    placeId: string;
    distributionplaces: IDistributionPlace[];

    constructor(
        protected distributionService: DistributionService,
        protected distributionPlaceService: DistributionPlaceService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected accountService: AccountService,
        protected commentService: CommentService,
        private datePipe: DatePipe,
        private localStorageService: LocalStorageService
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
        this.localStorageService.set('defaultPlaceId', this.placeId);
        this.distributionService
            .query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort(),
                fromDate: this.fromDate,
                toDate: this.toDate,
                placeId: this.placeId
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
        this.placeId = this.localStorageService.get('defaultPlaceId');
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDistributions();
        this.registerChangeInComments();
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
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.eventManager.destroy(this.commentsEventSubscriber);
    }

    trackId(index: number, item: IDistribution) {
        return item.id;
    }

    registerChangeInDistributions() {
        this.eventSubscriber = this.eventManager.subscribe('distributionListModification', response => this.reset());
    }

    registerChangeInComments() {
        this.commentsEventSubscriber = this.eventManager.subscribe('commentsModification', response => this.reset());
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
            this.loadComments(i);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    subscribe(distributionId) {
        this.distributionService.subscribe(distributionId).subscribe(
            (res: HttpResponse<IDistribution>) => {
                const old = this.distributions.find(d => d.id === res.body.id);
                this.distributions[this.distributions.indexOf(old)].users = res.body.users;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    unsubscribe(distributionId) {
        this.distributionService.unsubscribe(distributionId).subscribe(
            (res: HttpResponse<IDistribution>) => {
                const old = this.distributions.find(d => d.id === res.body.id);
                this.distributions[this.distributions.indexOf(old)].users = res.body.users;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    canSubscribe(distribution) {
        return (
            !distribution.users.some(user => {
                return user.login === this.currentAccount.login;
            }) && distribution.startDate > new Date()
        );
    }

    canUnsubscribe(distribution) {
        return (
            distribution.users.some(user => {
                return user.login === this.currentAccount.login;
            }) && distribution.startDate > new Date()
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

    preview(text, showCars) {
        if (text === null) {
            return '';
        }
        if (text.length <= showCars) {
            return text;
        }
        return text.substr(0, showCars) + '...';
    }

    loadComments(i) {
        this.commentService.query({ distributionId: this.distributions[i].id }).subscribe(
            (res: HttpResponse<IComment[]>) => {
                this.distributions[i].comments = res.body;
            },
            (res: HttpErrorResponse) => this.onCommentError(res.message)
        );
    }

    onCommentError(message) {}

    trackDistributionPlaceById(index: number, item: IDistributionPlace) {
        return item.id;
    }

    getMinRequiredClasses(numberSubscribed, minRequired) {
        if (numberSubscribed === 0) {
            return 'badge-pill badge-danger';
        } else if (numberSubscribed < minRequired) {
            return 'badge-pill badge-warning';
        } else if (!minRequired) {
            return 'badge-pill badge-primary';
        } else {
            return 'badge-pill badge-success';
        }
    }

    getIconByType(type) {
        if (type === Type.DISTRIBUTION) {
            return 'shopping-basket';
        } else if (type === Type.WORKSHOP) {
            return 'tractor';
        }
    }
}
