package org.example.spring.dao;

import org.example.spring.exception.AccessDenied;
import org.example.spring.model.Todo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TodoDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

    }


    // USED JDBC INSERT
    public void save(Todo todo) {
        var sql = "insert into spring_jdbc.todos(user_id, title, priority) values (:user_id, :title, :priority);";
        SqlParameterSource source = new BeanPropertySqlParameterSource(todo);
        namedParameterJdbcTemplate.update(sql, source);
    }

    public Todo findById(Integer id) {
        var sql = "select * from spring_jdbc.todos where id = :id;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        var mapper = BeanPropertyRowMapper.newInstance(Todo.class);
        return namedParameterJdbcTemplate.queryForObject(sql, parameterSource, mapper);
    }

    public Todo findByIdByUserId(Integer id, Long user_id) throws AccessDenied {
        var sql = "select * from spring_jdbc.todos where id = :id and user_id = :us_id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("us_id", user_id);
        var mapper = BeanPropertyRowMapper.newInstance(Todo.class);
        Todo todo = namedParameterJdbcTemplate.queryForObject(sql, parameterSource, mapper);
        if (todo != null) {
            return todo;

        } else {

            throw new AccessDenied("You can delete and update your todos");

        }
    }


    public List<Todo> findAll() {
        var sql = "select * from spring_jdbc.todos";
        var mapper = BeanPropertyRowMapper.newInstance(Todo.class);
        return namedParameterJdbcTemplate.query(sql, mapper);
    }

    public List<Todo> selectByUserId(Long id) {
        var sql = "select * from spring_jdbc.todos where user_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        return namedParameterJdbcTemplate.query(sql, parameterSource, new BeanPropertyRowMapper<>(Todo.class));
    }

    public void delete(Integer id) {
        var sql = "delete from spring_jdbc.todos where id = :id;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    public void deleteByUserId(Integer id, Long user_id) {
        var sql = "delete from spring_jdbc.todos where id = :id and user_id = :us_id;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("us_id", user_id);
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    public void update(Todo todo) {
        var sql = "update spring_jdbc.todos set title = :title, priority = :priority where id = :id;";
        SqlParameterSource source = new BeanPropertySqlParameterSource(todo);
        namedParameterJdbcTemplate.update(sql, source);
    }


    public void updateByUserId(Todo todo, Long user_id) throws AccessDenied {
        var sql = "update spring_jdbc.todos set title = :title, priority = :priority where id = :id and user_id = :us_id;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", todo.getId());
        parameterSource.addValue("title", todo.getTitle());
        parameterSource.addValue("priority", todo.getPriority());
        parameterSource.addValue("us_id", user_id);
        int update = namedParameterJdbcTemplate.update(sql, parameterSource);
        if (update == 0) {
            throw new AccessDenied("You can delete and update your todos");
        }
    }


}
