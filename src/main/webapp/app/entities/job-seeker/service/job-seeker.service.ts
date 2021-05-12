import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJobSeeker, getJobSeekerIdentifier } from '../job-seeker.model';

export type EntityResponseType = HttpResponse<IJobSeeker>;
export type EntityArrayResponseType = HttpResponse<IJobSeeker[]>;

@Injectable({ providedIn: 'root' })
export class JobSeekerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/job-seekers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(jobSeeker: IJobSeeker): Observable<EntityResponseType> {
    return this.http.post<IJobSeeker>(this.resourceUrl, jobSeeker, { observe: 'response' });
  }

  update(jobSeeker: IJobSeeker): Observable<EntityResponseType> {
    return this.http.put<IJobSeeker>(`${this.resourceUrl}/${getJobSeekerIdentifier(jobSeeker) as number}`, jobSeeker, {
      observe: 'response',
    });
  }

  partialUpdate(jobSeeker: IJobSeeker): Observable<EntityResponseType> {
    return this.http.patch<IJobSeeker>(`${this.resourceUrl}/${getJobSeekerIdentifier(jobSeeker) as number}`, jobSeeker, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobSeeker>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobSeeker[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addJobSeekerToCollectionIfMissing(
    jobSeekerCollection: IJobSeeker[],
    ...jobSeekersToCheck: (IJobSeeker | null | undefined)[]
  ): IJobSeeker[] {
    const jobSeekers: IJobSeeker[] = jobSeekersToCheck.filter(isPresent);
    if (jobSeekers.length > 0) {
      const jobSeekerCollectionIdentifiers = jobSeekerCollection.map(jobSeekerItem => getJobSeekerIdentifier(jobSeekerItem)!);
      const jobSeekersToAdd = jobSeekers.filter(jobSeekerItem => {
        const jobSeekerIdentifier = getJobSeekerIdentifier(jobSeekerItem);
        if (jobSeekerIdentifier == null || jobSeekerCollectionIdentifiers.includes(jobSeekerIdentifier)) {
          return false;
        }
        jobSeekerCollectionIdentifiers.push(jobSeekerIdentifier);
        return true;
      });
      return [...jobSeekersToAdd, ...jobSeekerCollection];
    }
    return jobSeekerCollection;
  }
}
