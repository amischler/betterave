/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BetteraveTestModule } from '../../../test.module';
import { DistributionPlaceComponent } from 'app/entities/distribution-place/distribution-place.component';
import { DistributionPlaceService } from 'app/entities/distribution-place/distribution-place.service';
import { DistributionPlace } from 'app/shared/model/distribution-place.model';

describe('Component Tests', () => {
    describe('DistributionPlace Management Component', () => {
        let comp: DistributionPlaceComponent;
        let fixture: ComponentFixture<DistributionPlaceComponent>;
        let service: DistributionPlaceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BetteraveTestModule],
                declarations: [DistributionPlaceComponent],
                providers: []
            })
                .overrideTemplate(DistributionPlaceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DistributionPlaceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistributionPlaceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DistributionPlace(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.distributionPlaces[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
