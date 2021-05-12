import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobSeeker } from '../job-seeker.model';
import { JobSeekerService } from '../service/job-seeker.service';

@Component({
  templateUrl: './job-seeker-delete-dialog.component.html',
})
export class JobSeekerDeleteDialogComponent {
  jobSeeker?: IJobSeeker;

  constructor(protected jobSeekerService: JobSeekerService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobSeekerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
