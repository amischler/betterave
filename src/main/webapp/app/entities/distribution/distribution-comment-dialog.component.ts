import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDistribution } from 'app/shared/model/distribution.model';
import { DistributionService } from './distribution.service';

import { CommentService } from 'app/entities/comment/comment.service';
import { IComment } from 'app/shared/model/comment.model';

@Component({
    selector: 'jhi-distribution-comment-dialog',
    templateUrl: './distribution-comment-dialog.component.html'
})
export class DistributionCommentDialogComponent {
    distribution: IDistribution;
    comment: IComment;

    constructor(
        protected distributionService: DistributionService,
        protected commentService: CommentService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmComment() {
        if (this.comment.id !== undefined) {
            this.commentService.update(this.comment).subscribe(response => {
                this.activeModal.dismiss(true);
            });
        } else {
            this.comment.distributionId = this.distribution.id;
            this.commentService.create(this.comment).subscribe(response => {
                this.activeModal.dismiss(true);
            });
        }
    }
}

@Component({
    selector: 'jhi-distribution-comment-popup',
    template: ''
})
export class DistributionCommentPopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ distribution, comment }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DistributionCommentDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.distribution = distribution;
                this.ngbModalRef.componentInstance.comment = comment;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/distribution', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/distribution', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
