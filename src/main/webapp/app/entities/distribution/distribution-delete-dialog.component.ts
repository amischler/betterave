import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDistribution } from 'app/shared/model/distribution.model';
import { DistributionService } from './distribution.service';

@Component({
    selector: 'jhi-distribution-delete-dialog',
    templateUrl: './distribution-delete-dialog.component.html'
})
export class DistributionDeleteDialogComponent {
    distribution: IDistribution;

    constructor(
        protected distributionService: DistributionService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.distributionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'distributionListModification',
                content: 'Deleted an distribution'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-distribution-delete-popup',
    template: ''
})
export class DistributionDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ distribution }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DistributionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.distribution = distribution;
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
