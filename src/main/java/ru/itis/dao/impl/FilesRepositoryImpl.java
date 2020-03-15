package ru.itis.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.dao.interfaces.FilesRepository;
import ru.itis.models.FileInfo;

import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
public class FilesRepositoryImpl implements FilesRepository {
    private final JdbcTemplate jdbcTemplate;

    private RowMapper<FileInfo> rowMapper = (row, num) ->
        FileInfo.builder()
                .url(row.getString("url"))
                .type(row.getString("type"))
                .size(row.getLong("size"))
                .storageFileName(row.getString("storage_name"))
                .originalFileName(row.getString("orig_name"))
                .id(row.getLong("id"))
                .userId(row.getLong("user_id"))
                .build();


    public FilesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(FileInfo fileInfo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sqlToSave = "INSERT INTO project.files (orig_name, storage_name, type, size, url, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlToSave, new String[]{"id"});
            ps.setString(1, fileInfo.getOriginalFileName());
            ps.setString(2, fileInfo.getStorageFileName());
            ps.setString(3, fileInfo.getType());
            ps.setLong(4, fileInfo.getSize());
            ps.setString(5, fileInfo.getUrl());
            ps.setLong(6, fileInfo.getUserId());

            return ps;
        }, keyHolder);
        fileInfo.setId((Long) keyHolder.getKey());
    }

    @Override
    public Optional<FileInfo> find(Long aLong) {
        return Optional.empty();
    }

    @Override
    public void update(FileInfo fileInfo) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public FileInfo findByName(String storageName) {
        String sqlToFind = "SELECT * FROM project.files WHERE storage_name = ?";
        FileInfo fileInfo = jdbcTemplate.queryForObject(sqlToFind, rowMapper, storageName);
        return fileInfo;
    }
}
