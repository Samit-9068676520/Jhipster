import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'job-seeker',
        data: { pageTitle: 'JobSeekers' },
        loadChildren: () => import('./job-seeker/job-seeker.module').then(m => m.JobSeekerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
