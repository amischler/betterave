import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDistributionPlace } from 'app/shared/model/distribution-place.model';
import { DistributionPlaceService } from './distribution-place.service';

@Component({
    selector: 'jhi-distribution-place-delete-dialog',
    templateUrl: './distribution-place-delete-dialog.component.html'
})
export class DistributionPlaceDeleteDialogComponent {
    distributionPlace: IDistributionPlace;

    constructor(
        protected distributionPlaceService: DistributionPlaceService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.distributionPlaceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'distributionPlaceListModification',
                content: 'Deleted an distributionPlace'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-distribution-place-delete-popup',
    template: ''
})
export class DistributionPlaceDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ distributionPlace }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DistributionPlaceDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.distributionPlace = distributionPlace;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/distribution-place', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/distribution-place', { outlets: { popup: null } }]);
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
