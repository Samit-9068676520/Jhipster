export interface IJobSeeker {
  id?: number;
  name?: string | null;
  age?: number | null;
  experience?: number | null;
  ctc?: number | null;
  expCTC?: number | null;
}

export class JobSeeker implements IJobSeeker {
  constructor(
    public id?: number,
    public name?: string | null,
    public age?: number | null,
    public experience?: number | null,
    public ctc?: number | null,
    public expCTC?: number | null
  ) {}
}

export function getJobSeekerIdentifier(jobSeeker: IJobSeeker): number | undefined {
  return jobSeeker.id;
}
