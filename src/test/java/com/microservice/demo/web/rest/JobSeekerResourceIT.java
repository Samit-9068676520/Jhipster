package com.microservice.demo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.microservice.demo.IntegrationTest;
import com.microservice.demo.domain.JobSeeker;
import com.microservice.demo.repository.JobSeekerRepository;
import com.microservice.demo.service.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link JobSeekerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class JobSeekerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Integer DEFAULT_EXPERIENCE = 1;
    private static final Integer UPDATED_EXPERIENCE = 2;

    private static final Integer DEFAULT_CTC = 1;
    private static final Integer UPDATED_CTC = 2;

    private static final Integer DEFAULT_EXP_CTC = 1;
    private static final Integer UPDATED_EXP_CTC = 2;

    private static final String ENTITY_API_URL = "/api/job-seekers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private JobSeeker jobSeeker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobSeeker createEntity(EntityManager em) {
        JobSeeker jobSeeker = new JobSeeker()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .experience(DEFAULT_EXPERIENCE)
            .ctc(DEFAULT_CTC)
            .expCTC(DEFAULT_EXP_CTC);
        return jobSeeker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobSeeker createUpdatedEntity(EntityManager em) {
        JobSeeker jobSeeker = new JobSeeker()
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .experience(UPDATED_EXPERIENCE)
            .ctc(UPDATED_CTC)
            .expCTC(UPDATED_EXP_CTC);
        return jobSeeker;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(JobSeeker.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        jobSeeker = createEntity(em);
    }

    @Test
    void createJobSeeker() throws Exception {
        int databaseSizeBeforeCreate = jobSeekerRepository.findAll().collectList().block().size();
        // Create the JobSeeker
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeCreate + 1);
        JobSeeker testJobSeeker = jobSeekerList.get(jobSeekerList.size() - 1);
        assertThat(testJobSeeker.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJobSeeker.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testJobSeeker.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testJobSeeker.getCtc()).isEqualTo(DEFAULT_CTC);
        assertThat(testJobSeeker.getExpCTC()).isEqualTo(DEFAULT_EXP_CTC);
    }

    @Test
    void createJobSeekerWithExistingId() throws Exception {
        // Create the JobSeeker with an existing ID
        jobSeeker.setId(1L);

        int databaseSizeBeforeCreate = jobSeekerRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllJobSeekers() {
        // Initialize the database
        jobSeekerRepository.save(jobSeeker).block();

        // Get all the jobSeekerList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(jobSeeker.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].age")
            .value(hasItem(DEFAULT_AGE))
            .jsonPath("$.[*].experience")
            .value(hasItem(DEFAULT_EXPERIENCE))
            .jsonPath("$.[*].ctc")
            .value(hasItem(DEFAULT_CTC))
            .jsonPath("$.[*].expCTC")
            .value(hasItem(DEFAULT_EXP_CTC));
    }

    @Test
    void getJobSeeker() {
        // Initialize the database
        jobSeekerRepository.save(jobSeeker).block();

        // Get the jobSeeker
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, jobSeeker.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(jobSeeker.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.age")
            .value(is(DEFAULT_AGE))
            .jsonPath("$.experience")
            .value(is(DEFAULT_EXPERIENCE))
            .jsonPath("$.ctc")
            .value(is(DEFAULT_CTC))
            .jsonPath("$.expCTC")
            .value(is(DEFAULT_EXP_CTC));
    }

    @Test
    void getNonExistingJobSeeker() {
        // Get the jobSeeker
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewJobSeeker() throws Exception {
        // Initialize the database
        jobSeekerRepository.save(jobSeeker).block();

        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();

        // Update the jobSeeker
        JobSeeker updatedJobSeeker = jobSeekerRepository.findById(jobSeeker.getId()).block();
        updatedJobSeeker.name(UPDATED_NAME).age(UPDATED_AGE).experience(UPDATED_EXPERIENCE).ctc(UPDATED_CTC).expCTC(UPDATED_EXP_CTC);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedJobSeeker.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedJobSeeker))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
        JobSeeker testJobSeeker = jobSeekerList.get(jobSeekerList.size() - 1);
        assertThat(testJobSeeker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJobSeeker.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testJobSeeker.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testJobSeeker.getCtc()).isEqualTo(UPDATED_CTC);
        assertThat(testJobSeeker.getExpCTC()).isEqualTo(UPDATED_EXP_CTC);
    }

    @Test
    void putNonExistingJobSeeker() throws Exception {
        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();
        jobSeeker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, jobSeeker.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchJobSeeker() throws Exception {
        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();
        jobSeeker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamJobSeeker() throws Exception {
        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();
        jobSeeker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateJobSeekerWithPatch() throws Exception {
        // Initialize the database
        jobSeekerRepository.save(jobSeeker).block();

        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();

        // Update the jobSeeker using partial update
        JobSeeker partialUpdatedJobSeeker = new JobSeeker();
        partialUpdatedJobSeeker.setId(jobSeeker.getId());

        partialUpdatedJobSeeker.age(UPDATED_AGE).experience(UPDATED_EXPERIENCE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobSeeker.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedJobSeeker))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
        JobSeeker testJobSeeker = jobSeekerList.get(jobSeekerList.size() - 1);
        assertThat(testJobSeeker.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJobSeeker.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testJobSeeker.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testJobSeeker.getCtc()).isEqualTo(DEFAULT_CTC);
        assertThat(testJobSeeker.getExpCTC()).isEqualTo(DEFAULT_EXP_CTC);
    }

    @Test
    void fullUpdateJobSeekerWithPatch() throws Exception {
        // Initialize the database
        jobSeekerRepository.save(jobSeeker).block();

        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();

        // Update the jobSeeker using partial update
        JobSeeker partialUpdatedJobSeeker = new JobSeeker();
        partialUpdatedJobSeeker.setId(jobSeeker.getId());

        partialUpdatedJobSeeker.name(UPDATED_NAME).age(UPDATED_AGE).experience(UPDATED_EXPERIENCE).ctc(UPDATED_CTC).expCTC(UPDATED_EXP_CTC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobSeeker.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedJobSeeker))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
        JobSeeker testJobSeeker = jobSeekerList.get(jobSeekerList.size() - 1);
        assertThat(testJobSeeker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJobSeeker.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testJobSeeker.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testJobSeeker.getCtc()).isEqualTo(UPDATED_CTC);
        assertThat(testJobSeeker.getExpCTC()).isEqualTo(UPDATED_EXP_CTC);
    }

    @Test
    void patchNonExistingJobSeeker() throws Exception {
        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();
        jobSeeker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, jobSeeker.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchJobSeeker() throws Exception {
        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();
        jobSeeker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamJobSeeker() throws Exception {
        int databaseSizeBeforeUpdate = jobSeekerRepository.findAll().collectList().block().size();
        jobSeeker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobSeeker))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobSeeker in the database
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteJobSeeker() {
        // Initialize the database
        jobSeekerRepository.save(jobSeeker).block();

        int databaseSizeBeforeDelete = jobSeekerRepository.findAll().collectList().block().size();

        // Delete the jobSeeker
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, jobSeeker.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll().collectList().block();
        assertThat(jobSeekerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
