/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BetteraveTestModule } from '../../../test.module';
import { DistributionPlaceDetailComponent } from 'app/entities/distribution-place/distribution-place-detail.component';
import { DistributionPlace } from 'app/shared/model/distribution-place.model';

describe('Component Tests', () => {
    describe('DistributionPlace Management Detail Component', () => {
        let comp: DistributionPlaceDetailComponent;
        let fixture: ComponentFixture<DistributionPlaceDetailComponent>;
        const route = ({ data: of({ distributionPlace: new DistributionPlace(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BetteraveTestModule],
                declarations: [DistributionPlaceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DistributionPlaceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DistributionPlaceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.distributionPlace).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
