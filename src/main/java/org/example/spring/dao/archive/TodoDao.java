package org.example.spring.dao.archive;

import lombok.Synchronized;
import org.example.spring.model.Todo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TodoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }


// USED JDBC INSERT
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

    public synchronized void simpleInsert(Todo todo){
        Map<String, Object> params = new HashMap<>();
        params.put("title", todo.getTitle());
        params.put("priority", todo.getPriority());
        jdbcInsert.withTableName("todos").usingColumns("title", "priority")
                .execute(params);
    }

}
