import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobSeeker } from '../job-seeker.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { JobSeekerService } from '../service/job-seeker.service';
import { JobSeekerDeleteDialogComponent } from '../delete/job-seeker-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-job-seeker',
  templateUrl: './job-seeker.component.html',
})
export class JobSeekerComponent implements OnInit {
  jobSeekers: IJobSeeker[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected jobSeekerService: JobSeekerService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.jobSeekers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.jobSeekerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IJobSeeker[]>) => {
          this.isLoading = false;
          this.paginateJobSeekers(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.jobSeekers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IJobSeeker): number {
    return item.id!;
  }

  delete(jobSeeker: IJobSeeker): void {
    const modalRef = this.modalService.open(JobSeekerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jobSeeker = jobSeeker;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateJobSeekers(data: IJobSeeker[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.jobSeekers.push(d);
      }
    }
  }
}
