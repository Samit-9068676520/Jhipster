package com.microservice.demo.service;

import com.microservice.demo.domain.JobSeeker;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link JobSeeker}.
 */
public interface JobSeekerService {
    /**
     * Save a jobSeeker.
     *
     * @param jobSeeker the entity to save.
     * @return the persisted entity.
     */
    Mono<JobSeeker> save(JobSeeker jobSeeker);

    /**
     * Partially updates a jobSeeker.
     *
     * @param jobSeeker the entity to update partially.
     * @return the persisted entity.
     */
    Mono<JobSeeker> partialUpdate(JobSeeker jobSeeker);

    /**
     * Get all the jobSeekers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<JobSeeker> findAll(Pageable pageable);

    /**
     * Returns the number of jobSeekers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" jobSeeker.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<JobSeeker> findOne(Long id);

    /**
     * Delete the "id" jobSeeker.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
