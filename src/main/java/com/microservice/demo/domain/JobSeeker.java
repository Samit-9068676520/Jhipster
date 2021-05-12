package com.microservice.demo.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A JobSeeker.
 */
@Table("job_seeker")
public class JobSeeker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("age")
    private Integer age;

    @Column("experience")
    private Integer experience;

    @Column("ctc")
    private Integer ctc;

    @Column("exp_ctc")
    private Integer expCTC;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobSeeker id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public JobSeeker name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public JobSeeker age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getExperience() {
        return this.experience;
    }

    public JobSeeker experience(Integer experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getCtc() {
        return this.ctc;
    }

    public JobSeeker ctc(Integer ctc) {
        this.ctc = ctc;
        return this;
    }

    public void setCtc(Integer ctc) {
        this.ctc = ctc;
    }

    public Integer getExpCTC() {
        return this.expCTC;
    }

    public JobSeeker expCTC(Integer expCTC) {
        this.expCTC = expCTC;
        return this;
    }

    public void setExpCTC(Integer expCTC) {
        this.expCTC = expCTC;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobSeeker)) {
            return false;
        }
        return id != null && id.equals(((JobSeeker) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobSeeker{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", experience=" + getExperience() +
            ", ctc=" + getCtc() +
            ", expCTC=" + getExpCTC() +
            "}";
    }
}
