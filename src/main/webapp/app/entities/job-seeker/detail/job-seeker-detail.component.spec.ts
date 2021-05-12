import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobSeekerDetailComponent } from './job-seeker-detail.component';

describe('Component Tests', () => {
  describe('JobSeeker Management Detail Component', () => {
    let comp: JobSeekerDetailComponent;
    let fixture: ComponentFixture<JobSeekerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [JobSeekerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ jobSeeker: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(JobSeekerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobSeekerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jobSeeker on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobSeeker).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
