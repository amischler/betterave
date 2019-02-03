/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BetteraveTestModule } from '../../../test.module';
import { DistributionDeleteDialogComponent } from 'app/entities/distribution/distribution-delete-dialog.component';
import { DistributionService } from 'app/entities/distribution/distribution.service';

describe('Component Tests', () => {
    describe('Distribution Management Delete Component', () => {
        let comp: DistributionDeleteDialogComponent;
        let fixture: ComponentFixture<DistributionDeleteDialogComponent>;
        let service: DistributionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BetteraveTestModule],
                declarations: [DistributionDeleteDialogComponent]
            })
                .overrideTemplate(DistributionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DistributionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistributionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
