package org.example.spring.controller;

import org.example.spring.dao.TodoDao;
import org.example.spring.model.Todo;
import org.example.spring.repository.DB;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TodoController {

    ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("ioc_settings.xml");
    TodoDao dao = context.getBean(TodoDao.class);

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @PostMapping("/todo/add/post")
    public String postTodo(@ModelAttribute Todo todo) {


        if (!todo.getTitle().isEmpty() || !todo.getPriority().isEmpty()) {
//            todo.setId(DB.todos.size() + 1);
//            todo.setCreatedAt(LocalDateTime.now());
//            DB.todos.add(0, todo);
            dao.save(todo);
        }
        return "redirect:/todo/list";
    }

    @GetMapping("/todo/list")
    public ModelAndView getTodos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("todolist");
        modelAndView.addObject("todos", dao.findAll());
        return modelAndView;
    }

    @GetMapping("/todo/addpage")
    public String addTodos() {
        return "todoadd";
    }

    @PostMapping(value = "/todo/delete")
    public String deleteTodo(@RequestParam("id") int id) {
//        DB.todos = DB.todos.stream().filter(todo -> todo.getId() != id).collect(Collectors.toList());
        dao.delete(id);
        return "redirect:/todo/list";
    }

    @GetMapping(value = "/todo/update")
    public ModelAndView updateTodo(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
//
//        Optional<Todo> todo1 = DB.todos.stream().filter(todo -> todo.getId() == id).findFirst();
//        todo1.ifPresent(todo -> {
//            modelAndView.setViewName("todoupdate");
//            modelAndView.addObject(todo);
//        });

        Todo byId = dao.findById(id);
        modelAndView.setViewName("todoupdate");
        modelAndView.addObject(byId);
        return modelAndView;
    }


    @PostMapping(value = "/todo/update")
    public String updateTodoDone(@RequestParam("id") int id, @RequestParam("title") String newTitle, @RequestParam("priority") String newPriority) {
        Todo byId = dao.findById(id);
        if (!newPriority.isEmpty() || !newTitle.isEmpty()) {
//            DB.todos = DB.todos.stream().peek(todo -> {
//                if (todo.getId() == id) {
//                    todo.setTitle(newTitle);
//                    todo.setPriority(newPriority);
//                }
//            }).collect(Collectors.toList());
            byId.setTitle(newTitle);
            byId.setPriority(newPriority);

            dao.update(byId);
        }
        return "redirect:/todo/list";
    }




}
