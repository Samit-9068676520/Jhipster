package com.microservice.demo.service.impl;

import com.microservice.demo.domain.JobSeeker;
import com.microservice.demo.repository.JobSeekerRepository;
import com.microservice.demo.service.JobSeekerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link JobSeeker}.
 */
@Service
@Transactional
public class JobSeekerServiceImpl implements JobSeekerService {

    private final Logger log = LoggerFactory.getLogger(JobSeekerServiceImpl.class);

    private final JobSeekerRepository jobSeekerRepository;

    public JobSeekerServiceImpl(JobSeekerRepository jobSeekerRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
    }

    @Override
    public Mono<JobSeeker> save(JobSeeker jobSeeker) {
        log.debug("Request to save JobSeeker : {}", jobSeeker);
        return jobSeekerRepository.save(jobSeeker);
    }

    @Override
    public Mono<JobSeeker> partialUpdate(JobSeeker jobSeeker) {
        log.debug("Request to partially update JobSeeker : {}", jobSeeker);

        return jobSeekerRepository
            .findById(jobSeeker.getId())
            .map(
                existingJobSeeker -> {
                    if (jobSeeker.getName() != null) {
                        existingJobSeeker.setName(jobSeeker.getName());
                    }
                    if (jobSeeker.getAge() != null) {
                        existingJobSeeker.setAge(jobSeeker.getAge());
                    }
                    if (jobSeeker.getExperience() != null) {
                        existingJobSeeker.setExperience(jobSeeker.getExperience());
                    }
                    if (jobSeeker.getCtc() != null) {
                        existingJobSeeker.setCtc(jobSeeker.getCtc());
                    }
                    if (jobSeeker.getExpCTC() != null) {
                        existingJobSeeker.setExpCTC(jobSeeker.getExpCTC());
                    }

                    return existingJobSeeker;
                }
            )
            .flatMap(jobSeekerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<JobSeeker> findAll(Pageable pageable) {
        log.debug("Request to get all JobSeekers");
        return jobSeekerRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return jobSeekerRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<JobSeeker> findOne(Long id) {
        log.debug("Request to get JobSeeker : {}", id);
        return jobSeekerRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete JobSeeker : {}", id);
        return jobSeekerRepository.deleteById(id);
    }
}
