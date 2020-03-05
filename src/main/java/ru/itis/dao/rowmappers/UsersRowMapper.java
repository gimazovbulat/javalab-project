package ru.itis.dao.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.itis.models.User;
import ru.itis.models.UserState;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UsersRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        String email = rs.getString("email");
        UserState userState = UserState.valueOf(rs.getString("state"));
        Long id = rs.getLong("id");
        String password = rs.getString("password");
        String confirmLink = rs.getString("confirm_link");
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .userState(userState)
                .confirmLink(confirmLink)
                .build();
    }
}
