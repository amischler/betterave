import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IDistribution } from 'app/shared/model/distribution.model';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment/comment.service';
import { AccountService } from 'app/core';

@Component({
    selector: 'jhi-distribution-detail',
    templateUrl: './distribution-detail.component.html'
})
export class DistributionDetailComponent implements OnInit {
    distribution: IDistribution;
    comments;
    currentAccount: any;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected activatedRoute: ActivatedRoute,
        protected commentService: CommentService,
        protected accountService: AccountService
    ) {}

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.activatedRoute.data.subscribe(({ distribution }) => {
            this.distribution = distribution;
            // load all comments
            this.loadComments();
        });
    }

    loadComments() {
        this.comments = this.commentService.query({ distributionId: this.distribution.id }).subscribe(
            (res: HttpResponse<IComment[]>) => {
                this.comments = res.body;
            },
            (res: HttpErrorResponse) => this.onCommentError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    onCommentError(message) {}

    canEditComment(comment) {
        return comment.userId === this.currentAccount.id;
    }

    deleteComment(comment) {
        this.commentService.delete(comment.id).subscribe(response => this.loadComments());
    }
}
