jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { JobSeekerService } from '../service/job-seeker.service';

import { JobSeekerDeleteDialogComponent } from './job-seeker-delete-dialog.component';

describe('Component Tests', () => {
  describe('JobSeeker Management Delete Component', () => {
    let comp: JobSeekerDeleteDialogComponent;
    let fixture: ComponentFixture<JobSeekerDeleteDialogComponent>;
    let service: JobSeekerService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JobSeekerDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(JobSeekerDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobSeekerDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(JobSeekerService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
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
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
