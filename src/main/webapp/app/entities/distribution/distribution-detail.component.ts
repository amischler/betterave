import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IDistribution } from 'app/shared/model/distribution.model';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment/comment.service';
import { UserService } from 'app/core/user/user.service';

@Component({
    selector: 'jhi-distribution-detail',
    templateUrl: './distribution-detail.component.html'
})
export class DistributionDetailComponent implements OnInit {
    distribution: IDistribution;
    comments;
    users;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected activatedRoute: ActivatedRoute,
        protected commentService: CommentService,
        protected userService: UserService
    ) {}

    ngOnInit() {
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
                // load user information for the received comments
                this.loadUsers();
            },
            (res: HttpErrorResponse) => this.onCommentError(res.message)
        );
    }

    loadUsers() {
        const userIds = this.comments.map(comment => comment.userId);
        this.userService.query({ id: userIds }).subscribe((res2: HttpResponse<IComment[]>) => {
            this.users = res2.body;
        });
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

    getUserLogin(id) {
        if (this.users) {
            return this.users.filter(user => user.id === id)[0].login;
        } else {
            return '...';
        }
    }
}
