import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobSeeker } from '../job-seeker.model';

@Component({
  selector: 'jhi-job-seeker-detail',
  templateUrl: './job-seeker-detail.component.html',
})
export class JobSeekerDetailComponent implements OnInit {
  jobSeeker: IJobSeeker | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobSeeker }) => {
      this.jobSeeker = jobSeeker;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
