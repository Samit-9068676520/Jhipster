package com.microservice.demo.repository.rowmapper;

import com.microservice.demo.domain.JobSeeker;
import com.microservice.demo.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link JobSeeker}, with proper type conversions.
 */
@Service
public class JobSeekerRowMapper implements BiFunction<Row, String, JobSeeker> {

    private final ColumnConverter converter;

    public JobSeekerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link JobSeeker} stored in the database.
     */
    @Override
    public JobSeeker apply(Row row, String prefix) {
        JobSeeker entity = new JobSeeker();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAge(converter.fromRow(row, prefix + "_age", Integer.class));
        entity.setExperience(converter.fromRow(row, prefix + "_experience", Integer.class));
        entity.setCtc(converter.fromRow(row, prefix + "_ctc", Integer.class));
        entity.setExpCTC(converter.fromRow(row, prefix + "_exp_ctc", Integer.class));
        return entity;
    }
}
