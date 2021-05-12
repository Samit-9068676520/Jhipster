import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { JobSeekerComponent } from './list/job-seeker.component';
import { JobSeekerDetailComponent } from './detail/job-seeker-detail.component';
import { JobSeekerUpdateComponent } from './update/job-seeker-update.component';
import { JobSeekerDeleteDialogComponent } from './delete/job-seeker-delete-dialog.component';
import { JobSeekerRoutingModule } from './route/job-seeker-routing.module';

@NgModule({
  imports: [SharedModule, JobSeekerRoutingModule],
  declarations: [JobSeekerComponent, JobSeekerDetailComponent, JobSeekerUpdateComponent, JobSeekerDeleteDialogComponent],
  entryComponents: [JobSeekerDeleteDialogComponent],
})
export class JobSeekerModule {}
