package org.example.spring.dao;

import org.example.spring.domain.AuthUser;
import org.example.spring.domain.Uploads;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class UploadsDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final AuthUserDao authUserDao;

    public UploadsDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, AuthUserDao authUserDao) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.authUserDao = authUserDao;
    }

    public void save(Uploads uploads) {
        String sql = "insert into spring_jdbc.uploads (originalname,generatedname, mimetype, size, user_id) values(:originalName, :generatedName, :mimeType, :size, :user_id)";
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(uploads);
        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public Uploads findByGenerateName(String filename) {
        String sql = "select * from spring_jdbc.uploads where generatedName = :generatedName";
        Map<String, Object> paramSource = Map.of("generatedName", filename);
        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, BeanPropertyRowMapper.newInstance(Uploads.class));
    }

    public Uploads findImgByUserId(Long id) {
        String sql = "select * from spring_jdbc.uploads where user_id = :id";
        Map<String, Object> paramSource = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, BeanPropertyRowMapper.newInstance(Uploads.class));
    }


    public Uploads findImgByUserName(String username) {
        Optional<AuthUser> byUsername = authUserDao.findByUsername(username);
        String sql = "select * from spring_jdbc.uploads where user_id = :id";
        AuthUser authUser = new AuthUser();

        byUsername.ifPresent(authUserO -> {
            authUser.setPassword(authUserO.getPassword());
            authUser.setRole(authUserO.getRole());
            authUser.setUsername(authUserO.getUsername());
            authUser.setId(authUserO.getId());
            authUser.setActive(authUserO.isActive());
        });
        Map<String, Object> paramSource = Map.of("id", authUser.getId());

        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, BeanPropertyRowMapper.newInstance(Uploads.class));
    }
}
