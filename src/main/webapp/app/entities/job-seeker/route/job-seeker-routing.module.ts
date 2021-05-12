import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JobSeekerComponent } from '../list/job-seeker.component';
import { JobSeekerDetailComponent } from '../detail/job-seeker-detail.component';
import { JobSeekerUpdateComponent } from '../update/job-seeker-update.component';
import { JobSeekerRoutingResolveService } from './job-seeker-routing-resolve.service';

const jobSeekerRoute: Routes = [
  {
    path: '',
    component: JobSeekerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobSeekerDetailComponent,
    resolve: {
      jobSeeker: JobSeekerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobSeekerUpdateComponent,
    resolve: {
      jobSeeker: JobSeekerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobSeekerUpdateComponent,
    resolve: {
      jobSeeker: JobSeekerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jobSeekerRoute)],
  exports: [RouterModule],
})
export class JobSeekerRoutingModule {}
