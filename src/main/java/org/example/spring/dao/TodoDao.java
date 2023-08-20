package org.example.spring.dao;

import org.example.spring.model.Todo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

public class TodoDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TodoDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


// USED JDBC INSERT
    public void save(Todo todo) {
        var sql = "insert into spring_jdbc.todos(title, priority) values (:title, :priority);";
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


    public List<Todo> findAll(){
        var sql = "select * from spring_jdbc.todos";
        var mapper = BeanPropertyRowMapper.newInstance(Todo.class);
        return namedParameterJdbcTemplate.query(sql, mapper);
    }

    public void delete(Integer id){
        var sql = "delete from spring_jdbc.todos where id = :id;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }

    public void update(Todo todo) {
        var sql = "update spring_jdbc.todos set title = :title, priority = :priority where id = :id;";
        SqlParameterSource source = new BeanPropertySqlParameterSource(todo);
        namedParameterJdbcTemplate.update(sql, source);
    }

}
