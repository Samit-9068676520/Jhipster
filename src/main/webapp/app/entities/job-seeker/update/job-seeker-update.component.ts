import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IJobSeeker, JobSeeker } from '../job-seeker.model';
import { JobSeekerService } from '../service/job-seeker.service';

@Component({
  selector: 'jhi-job-seeker-update',
  templateUrl: './job-seeker-update.component.html',
})
export class JobSeekerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    age: [],
    experience: [],
    ctc: [],
    expCTC: [],
  });

  constructor(protected jobSeekerService: JobSeekerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobSeeker }) => {
      this.updateForm(jobSeeker);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobSeeker = this.createFromForm();
    if (jobSeeker.id !== undefined) {
      this.subscribeToSaveResponse(this.jobSeekerService.update(jobSeeker));
    } else {
      this.subscribeToSaveResponse(this.jobSeekerService.create(jobSeeker));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobSeeker>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(jobSeeker: IJobSeeker): void {
    this.editForm.patchValue({
      id: jobSeeker.id,
      name: jobSeeker.name,
      age: jobSeeker.age,
      experience: jobSeeker.experience,
      ctc: jobSeeker.ctc,
      expCTC: jobSeeker.expCTC,
    });
  }

  protected createFromForm(): IJobSeeker {
    return {
      ...new JobSeeker(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      age: this.editForm.get(['age'])!.value,
      experience: this.editForm.get(['experience'])!.value,
      ctc: this.editForm.get(['ctc'])!.value,
      expCTC: this.editForm.get(['expCTC'])!.value,
    };
  }
}
