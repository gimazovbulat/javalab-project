package ru.itis.dao.impl;

import org.hibernate.exception.DataException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dao.rowmappers.UsersRowMapper;
import ru.itis.models.User;

import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
public class UsersRepositoryImpl implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UsersRowMapper usersRowmapper;

    public UsersRepositoryImpl(JdbcTemplate jdbcTemplate,
                               UsersRowMapper usersRowmapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.usersRowmapper = usersRowmapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, email, state, password FROM project.users_table where email = ?;";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, usersRowmapper, email);
        } catch (DataException e) {
            throw new IllegalStateException(e);
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByConfirmLink(String confirmLink) {
        User user;
        String sql = "SELECT * FROM project.users_table WHERE confirm_link = ?";
        try {
            user = jdbcTemplate.queryForObject(sql, usersRowmapper, confirmLink);
        } catch (DataException e) {
            throw new IllegalStateException(e);
        }
        return Optional.of(user);
    }

    @Override
    public void save(User user) {
        String saveSql = "INSERT INTO project.users_table (email, password, state, confirm_link) VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(saveSql, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUserState().getValue());
            ps.setString(4, user.getConfirmLink());
            return ps;
        }, keyHolder);

        user.setId((Long) keyHolder.getKey());
    }

    @Override
    public Optional<User> find(Long id) {
        User user;
        String sql = "SELECT * FROM project.users_table WHERE id = ?";
        try {
            user = jdbcTemplate.queryForObject(sql, usersRowmapper, id);
        } catch (DataException e) {
            throw new IllegalStateException(e);
        }
        return Optional.of(user);
    }

    @Override
    public void update(User user) {
        String updateSql = "UPDATE project.users_table SET email = ?, password = ?, state = ?, confirm_link = ?";

        jdbcTemplate.update(updateSql,
                user.getEmail(),
                user.getPassword(),
                user.getUserState().getValue(),
                user.getConfirmLink());
    }

    @Override
    public void delete(Long aLong) {

    }
}
