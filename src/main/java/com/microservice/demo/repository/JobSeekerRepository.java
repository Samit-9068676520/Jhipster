package com.microservice.demo.repository;

import com.microservice.demo.domain.JobSeeker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the JobSeeker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobSeekerRepository extends R2dbcRepository<JobSeeker, Long>, JobSeekerRepositoryInternal {
    Flux<JobSeeker> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<JobSeeker> findAll();

    @Override
    Mono<JobSeeker> findById(Long id);

    @Override
    <S extends JobSeeker> Mono<S> save(S entity);
}

interface JobSeekerRepositoryInternal {
    <S extends JobSeeker> Mono<S> insert(S entity);
    <S extends JobSeeker> Mono<S> save(S entity);
    Mono<Integer> update(JobSeeker entity);

    Flux<JobSeeker> findAll();
    Mono<JobSeeker> findById(Long id);
    Flux<JobSeeker> findAllBy(Pageable pageable);
    Flux<JobSeeker> findAllBy(Pageable pageable, Criteria criteria);
}
