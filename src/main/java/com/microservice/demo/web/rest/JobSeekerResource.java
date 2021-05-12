package com.microservice.demo.web.rest;

import com.microservice.demo.domain.JobSeeker;
import com.microservice.demo.repository.JobSeekerRepository;
import com.microservice.demo.service.JobSeekerService;
import com.microservice.demo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.microservice.demo.domain.JobSeeker}.
 */
@RestController
@RequestMapping("/api")
public class JobSeekerResource {

    private final Logger log = LoggerFactory.getLogger(JobSeekerResource.class);

    private static final String ENTITY_NAME = "jobSeeker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobSeekerService jobSeekerService;

    private final JobSeekerRepository jobSeekerRepository;

    public JobSeekerResource(JobSeekerService jobSeekerService, JobSeekerRepository jobSeekerRepository) {
        this.jobSeekerService = jobSeekerService;
        this.jobSeekerRepository = jobSeekerRepository;
    }

    /**
     * {@code POST  /job-seekers} : Create a new jobSeeker.
     *
     * @param jobSeeker the jobSeeker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobSeeker, or with status {@code 400 (Bad Request)} if the jobSeeker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-seekers")
    public Mono<ResponseEntity<JobSeeker>> createJobSeeker(@RequestBody JobSeeker jobSeeker) throws URISyntaxException {
        log.debug("REST request to save JobSeeker : {}", jobSeeker);
        if (jobSeeker.getId() != null) {
            throw new BadRequestAlertException("A new jobSeeker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return jobSeekerService
            .save(jobSeeker)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/job-seekers/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /job-seekers/:id} : Updates an existing jobSeeker.
     *
     * @param id the id of the jobSeeker to save.
     * @param jobSeeker the jobSeeker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobSeeker,
     * or with status {@code 400 (Bad Request)} if the jobSeeker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobSeeker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-seekers/{id}")
    public Mono<ResponseEntity<JobSeeker>> updateJobSeeker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobSeeker jobSeeker
    ) throws URISyntaxException {
        log.debug("REST request to update JobSeeker : {}, {}", id, jobSeeker);
        if (jobSeeker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobSeeker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jobSeekerRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return jobSeekerService
                        .save(jobSeeker)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
    }

    /**
     * {@code PATCH  /job-seekers/:id} : Partial updates given fields of an existing jobSeeker, field will ignore if it is null
     *
     * @param id the id of the jobSeeker to save.
     * @param jobSeeker the jobSeeker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobSeeker,
     * or with status {@code 400 (Bad Request)} if the jobSeeker is not valid,
     * or with status {@code 404 (Not Found)} if the jobSeeker is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobSeeker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-seekers/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<JobSeeker>> partialUpdateJobSeeker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobSeeker jobSeeker
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobSeeker partially : {}, {}", id, jobSeeker);
        if (jobSeeker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobSeeker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jobSeekerRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<JobSeeker> result = jobSeekerService.partialUpdate(jobSeeker);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString())
                                    )
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /job-seekers} : get all the jobSeekers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobSeekers in body.
     */
    @GetMapping("/job-seekers")
    public Mono<ResponseEntity<List<JobSeeker>>> getAllJobSeekers(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of JobSeekers");
        return jobSeekerService
            .countAll()
            .zipWith(jobSeekerService.findAll(pageable).collectList())
            .map(
                countWithEntities -> {
                    return ResponseEntity
                        .ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                UriComponentsBuilder.fromHttpRequest(request),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2());
                }
            );
    }

    /**
     * {@code GET  /job-seekers/:id} : get the "id" jobSeeker.
     *
     * @param id the id of the jobSeeker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobSeeker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-seekers/{id}")
    public Mono<ResponseEntity<JobSeeker>> getJobSeeker(@PathVariable Long id) {
        log.debug("REST request to get JobSeeker : {}", id);
        Mono<JobSeeker> jobSeeker = jobSeekerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobSeeker);
    }

    /**
     * {@code DELETE  /job-seekers/:id} : delete the "id" jobSeeker.
     *
     * @param id the id of the jobSeeker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-seekers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteJobSeeker(@PathVariable Long id) {
        log.debug("REST request to delete JobSeeker : {}", id);
        return jobSeekerService
            .delete(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}
