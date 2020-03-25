package ru.itis.dao.impl;

import org.hibernate.exception.DataException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dao.rowmappers.UsersRowMapper;
import ru.itis.models.User;
import ru.itis.security.Role;

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
        String sql = "SELECT ut.id, ut.email, ut.password," +
                " ut.state, ut.ava_path, ur.role, ut.confirm_link FROM project.users_table ut JOIN project.user_roles ur " +
                "ON ut.id = ur.user_id WHERE email = ?";
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
        String sql = "SELECT ut.id, ut.email, ut.password," +
                " ut.state, ut.ava_path, ur.role, ut.confirm_link FROM project.users_table ut JOIN project.user_roles ur " +
                "ON ut.id = ur.user_id WHERE confirm_link = ?";
        try {
            user = jdbcTemplate.queryForObject(sql, usersRowmapper, confirmLink);
        } catch (DataException e) {
            throw new IllegalStateException(e);
        }
        return Optional.of(user);
    }

    @Transactional
    @Override
    public Long save(User user) {
        String saveSql = "INSERT INTO project.users_table (email, password, state, confirm_link, ava_path) VALUES (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(saveSql, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUserState().getValue());
            ps.setString(4, user.getConfirmLink());
            ps.setString(5, user.getAvaPath());
            return ps;
        }, keyHolder);

        user.setId((Long) keyHolder.getKey());

        String rolesSql = "INSERT INTO project.user_roles (user_id, role) VALUES (?, ?)";
        for (Role role : user.getRoles()) {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(rolesSql);
                ps.setLong(1, user.getId());
                ps.setString(2, role.getVal());
                return ps;
            });
        }
        return user.getId();
    }

    @Override
    public Optional<User> find(Long id) {
        User user;
        String sql = "SELECT ut.id, ut.email, ut.password," +
                " ut.state, ut.ava_path, ur.role, ut.confirm_link FROM project.users_table ut JOIN project.user_roles ur " +
                "ON ut.id = ur.user_id WHERE id = ?";
        try {
            user = jdbcTemplate.queryForObject(sql, usersRowmapper, id);
        } catch (DataException e) {
            throw new IllegalStateException(e);
        }
        return Optional.of(user);
    }

    @Override
    public void update(User user) {
        String updateSql = "UPDATE project.users_table SET email = ?, password = ?, state = ?, confirm_link = ?, ava_path = ? WHERE id = ?";

        jdbcTemplate.update(updateSql,
                user.getEmail(),
                user.getPassword(),
                user.getUserState().getValue(),
                user.getConfirmLink(),
                user.getAvaPath(),
                user.getId());
    }

    @Override
    public void delete(Long aLong) {

    }
}
