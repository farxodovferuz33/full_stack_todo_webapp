package org.example.spring.dao;

import org.example.spring.model.Todo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TodoDao {
    private final JdbcTemplate jdbcTemplate;

    public TodoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Todo todo) {
        var sql = "insert into todos(title, priority) values (?, ?);";
        jdbcTemplate.update(sql, todo.getTitle(), todo.getPriority());
    }

    public Todo findById(Integer id) {
        var sql = "select * from todos where id = ?;";
        var mapper = BeanPropertyRowMapper.newInstance(Todo.class);
        return jdbcTemplate.queryForObject(sql, mapper, id);

//        return jdbcTemplate.queryForObject("select * from todos where id = ?;", (rs, rowNum) -> Todo.builder()
//                .id(rs.getInt("id"))
//                .title(rs.getString("title"))
//                .priority(rs.getString("priority"))
//                .createdAt(rs.getObject("createdAt")).
//                build(), id);
    }


    public List<Todo> findAll(){
        var sql = "select * from todos";
        var mapper = BeanPropertyRowMapper.newInstance(Todo.class);
        return jdbcTemplate.query(sql, mapper);
    }

    public void delete(Integer id){
        var sql = "delete from todos where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public void update(Todo todo) {
        var sql = "update todos set title = ?, priority = ? where id = ?;";
        jdbcTemplate.update(sql, todo.getTitle(), todo.getPriority(), todo.getId());
    }

}
