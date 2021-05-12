import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IJobSeeker, JobSeeker } from '../job-seeker.model';

import { JobSeekerService } from './job-seeker.service';

describe('Service Tests', () => {
  describe('JobSeeker Service', () => {
    let service: JobSeekerService;
    let httpMock: HttpTestingController;
    let elemDefault: IJobSeeker;
    let expectedResult: IJobSeeker | IJobSeeker[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(JobSeekerService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        age: 0,
        experience: 0,
        ctc: 0,
        expCTC: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a JobSeeker', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new JobSeeker()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a JobSeeker', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            age: 1,
            experience: 1,
            ctc: 1,
            expCTC: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a JobSeeker', () => {
        const patchObject = Object.assign(
          {
            age: 1,
            experience: 1,
          },
          new JobSeeker()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of JobSeeker', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            age: 1,
            experience: 1,
            ctc: 1,
            expCTC: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a JobSeeker', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addJobSeekerToCollectionIfMissing', () => {
        it('should add a JobSeeker to an empty array', () => {
          const jobSeeker: IJobSeeker = { id: 123 };
          expectedResult = service.addJobSeekerToCollectionIfMissing([], jobSeeker);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(jobSeeker);
        });

        it('should not add a JobSeeker to an array that contains it', () => {
          const jobSeeker: IJobSeeker = { id: 123 };
          const jobSeekerCollection: IJobSeeker[] = [
            {
              ...jobSeeker,
            },
            { id: 456 },
          ];
          expectedResult = service.addJobSeekerToCollectionIfMissing(jobSeekerCollection, jobSeeker);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a JobSeeker to an array that doesn't contain it", () => {
          const jobSeeker: IJobSeeker = { id: 123 };
          const jobSeekerCollection: IJobSeeker[] = [{ id: 456 }];
          expectedResult = service.addJobSeekerToCollectionIfMissing(jobSeekerCollection, jobSeeker);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(jobSeeker);
        });

        it('should add only unique JobSeeker to an array', () => {
          const jobSeekerArray: IJobSeeker[] = [{ id: 123 }, { id: 456 }, { id: 26885 }];
          const jobSeekerCollection: IJobSeeker[] = [{ id: 123 }];
          expectedResult = service.addJobSeekerToCollectionIfMissing(jobSeekerCollection, ...jobSeekerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const jobSeeker: IJobSeeker = { id: 123 };
          const jobSeeker2: IJobSeeker = { id: 456 };
          expectedResult = service.addJobSeekerToCollectionIfMissing([], jobSeeker, jobSeeker2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(jobSeeker);
          expect(expectedResult).toContain(jobSeeker2);
        });

        it('should accept null and undefined values', () => {
          const jobSeeker: IJobSeeker = { id: 123 };
          expectedResult = service.addJobSeekerToCollectionIfMissing([], null, jobSeeker, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(jobSeeker);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
