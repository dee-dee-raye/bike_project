package com.globomatics.bike.services;

import com.globomatics.bike.models.BikeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class BikeUserDetailsService implements UserDetailsService {
    @Autowired
    DataSource dataSource;

    private static final String loadUserByUsernameQuery = "select username, enabled, nickname " +
            "from users where username = ?";
    private static final String getUserExistByUsernameQuery = "select exists(select from users where username = ?)";
    private static final String addUserQuery = "insert into users (username, enabled) values (?, 1)";

    @Override
    public BikeUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //"fake" because we do not pass the password around our app, but User
        // expects username, password,
        // and a collection of authorities
        final BikeUserDetails userDetails = new BikeUserDetails(
                username,
                "fake",
                Collections.EMPTY_LIST);

        if (userExistsInDB(username)) {
            //Now we take that username and query for the user with that same username.
            //We then map that result back into our userDetails object via it's setter.
            jdbcTemplate.queryForObject(loadUserByUsernameQuery, new RowMapper<BikeUserDetails>() {

                @Override
                public BikeUserDetails mapRow(ResultSet resultSet, int i) throws SQLException {
                    userDetails.setNickname(resultSet.getString("nickname"));
                    return userDetails;
                }
            }, username);


        } else {
            System.out.println("here yo!");
            jdbcTemplate.update(addUserQuery, username);
            userDetails.setNickname(null);
        }
        return userDetails;
    }

    private Boolean userExistsInDB(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return jdbcTemplate.queryForObject(getUserExistByUsernameQuery, Boolean.class, username);
    }
}
