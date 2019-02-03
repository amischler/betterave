/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BetteraveTestModule } from '../../../test.module';
import { DistributionDetailComponent } from 'app/entities/distribution/distribution-detail.component';
import { Distribution } from 'app/shared/model/distribution.model';

describe('Component Tests', () => {
    describe('Distribution Management Detail Component', () => {
        let comp: DistributionDetailComponent;
        let fixture: ComponentFixture<DistributionDetailComponent>;
        const route = ({ data: of({ distribution: new Distribution(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BetteraveTestModule],
                declarations: [DistributionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DistributionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DistributionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.distribution).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
