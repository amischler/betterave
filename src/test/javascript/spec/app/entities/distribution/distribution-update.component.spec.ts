/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BetteraveTestModule } from '../../../test.module';
import { DistributionUpdateComponent } from 'app/entities/distribution/distribution-update.component';
import { DistributionService } from 'app/entities/distribution/distribution.service';
import { Distribution } from 'app/shared/model/distribution.model';

describe('Component Tests', () => {
    describe('Distribution Management Update Component', () => {
        let comp: DistributionUpdateComponent;
        let fixture: ComponentFixture<DistributionUpdateComponent>;
        let service: DistributionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BetteraveTestModule],
                declarations: [DistributionUpdateComponent]
            })
                .overrideTemplate(DistributionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DistributionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistributionService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Distribution(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.distribution = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Distribution();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.distribution = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
