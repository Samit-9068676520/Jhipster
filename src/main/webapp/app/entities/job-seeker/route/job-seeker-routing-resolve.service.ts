import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJobSeeker, JobSeeker } from '../job-seeker.model';
import { JobSeekerService } from '../service/job-seeker.service';

@Injectable({ providedIn: 'root' })
export class JobSeekerRoutingResolveService implements Resolve<IJobSeeker> {
  constructor(protected service: JobSeekerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobSeeker> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jobSeeker: HttpResponse<JobSeeker>) => {
          if (jobSeeker.body) {
            return of(jobSeeker.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobSeeker());
  }
}
