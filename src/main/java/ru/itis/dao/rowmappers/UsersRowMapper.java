package ru.itis.dao.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.itis.models.User;
import ru.itis.models.UserState;
import ru.itis.security.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UsersRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        do {
            String email = rs.getString("email");
            UserState userState = UserState.valueOf(rs.getString("state"));
            Long id = rs.getLong("id");
            String password = rs.getString("password");
            String confirmLink = rs.getString("confirm_link");
            String avaPath = rs.getString("ava_path");
            String role = rs.getString("role");

            user.setId(id);
            user.setEmail(email);
            user.setPassword(password);
            user.setAvaPath(avaPath);
            user.setConfirmLink(confirmLink);
            user.setUserState(userState);
            user.getRoles().add(Role.valueOf(role));
        }
        while (rs.next());
        return user;
    }
}
