package org.example.spring.dao.archive;

import org.example.spring.model.Todo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.sql.Types;
import java.util.List;
import java.util.Map;

// Methodlarda SimpleJdbcCall ishlatilgan, JdbcTemplate esa params sifatida berilishi uchun
public class TodoDaoForSimpleJdbcCall {

    // Methodlarda SimpleJdbcCall ishlatilgan, JdbcTemplate esa params sifatida berilishi uchun
    private final JdbcTemplate jdbcTemplate;

    // Methodlarda SimpleJdbcCall ishlatilgan, JdbcTemplate esa params sifatida berilishi uchun
    public TodoDaoForSimpleJdbcCall(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Todo todo) {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withFunctionName("insert_todo");
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("title", todo.getTitle())
                    .addValue("priority", todo.getPriority());
            jdbcCall.execute(parameters);
    }

    public Todo findById(Integer id) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("find_todo_by_id")
                .returningResultSet("todo", BeanPropertyRowMapper.newInstance(Todo.class));

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        Map<String, Object> result = jdbcCall.execute(parameters);
        List<Todo> todoList = (List<Todo>) result.get("todo");

        if (todoList != null && !todoList.isEmpty()) return todoList.get(0);
        else return null;
    }


    public List<Todo> findAll(){
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withProcedureName("findAll");
        jdbcCall.returningResultSet("result", BeanPropertyRowMapper.newInstance(Todo.class));
        Map<String, Object> result = jdbcCall.execute();
        return (List<Todo>) result.get("result");
    }

    public void delete(Integer id){
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withProcedureName("delete");
        jdbcCall.addDeclaredParameter(new SqlParameter("id", Types.INTEGER));
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        jdbcCall.execute(parameterSource);
    }


    public void update(Todo todo) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
        jdbcCall.withProcedureName("update");
        jdbcCall.addDeclaredParameter(new SqlParameter("title", Types.VARCHAR));
        jdbcCall.addDeclaredParameter(new SqlParameter("priority", Types.INTEGER));

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", todo.getTitle());
        parameterSource.addValue("priority", todo.getPriority());
        jdbcCall.execute(parameterSource);
    }

}
