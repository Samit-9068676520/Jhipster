jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JobSeekerService } from '../service/job-seeker.service';
import { IJobSeeker, JobSeeker } from '../job-seeker.model';

import { JobSeekerUpdateComponent } from './job-seeker-update.component';

describe('Component Tests', () => {
  describe('JobSeeker Management Update Component', () => {
    let comp: JobSeekerUpdateComponent;
    let fixture: ComponentFixture<JobSeekerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let jobSeekerService: JobSeekerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JobSeekerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(JobSeekerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobSeekerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      jobSeekerService = TestBed.inject(JobSeekerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const jobSeeker: IJobSeeker = { id: 456 };

        activatedRoute.data = of({ jobSeeker });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(jobSeeker));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jobSeeker = { id: 123 };
        spyOn(jobSeekerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jobSeeker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: jobSeeker }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(jobSeekerService.update).toHaveBeenCalledWith(jobSeeker);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jobSeeker = new JobSeeker();
        spyOn(jobSeekerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jobSeeker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: jobSeeker }));
        saveSubject.complete();

        // THEN
        expect(jobSeekerService.create).toHaveBeenCalledWith(jobSeeker);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jobSeeker = { id: 123 };
        spyOn(jobSeekerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jobSeeker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(jobSeekerService.update).toHaveBeenCalledWith(jobSeeker);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
