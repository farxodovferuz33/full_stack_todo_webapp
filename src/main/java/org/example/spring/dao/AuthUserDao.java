package org.example.spring.dao;

import lombok.NonNull;
import org.example.spring.domain.AuthUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuthUserDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuthUserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long save(@NonNull AuthUser authUser) {
        var sql = "insert into spring_jdbc.authuser(username, password, role) values(:username, :password, :role)";
        var paramSource = new MapSqlParameterSource()
                .addValue("username", authUser.getUsername())
                .addValue("password", authUser.getPassword())
                .addValue("role", authUser.getRole());
//                .addValue("image", authUser.getImage());
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"id"});
        return (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    public List<AuthUser> getAllUsers() {
        var sql = "select * from spring_jdbc.authuser order by id";
        var mapper = BeanPropertyRowMapper.newInstance(AuthUser.class);
        return namedParameterJdbcTemplate.query(sql, mapper);
    }


    public Optional<AuthUser> findByUsername(@NonNull String username) {
        var sql = "select * from spring_jdbc.authuser t where t.username = :username and active = true";
        var paramSource = new MapSqlParameterSource().addValue("username", username);
        var rowMapper = BeanPropertyRowMapper.newInstance(AuthUser.class);
        try {
            var authUser = namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
            return Optional.of(authUser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
