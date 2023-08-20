package org.example.spring.controller;

import org.example.spring.dao.TodoDao;
import org.example.spring.model.Todo;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TodoController {

    ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("ioc_settings.xml");
    TodoDao todoDao = context.getBean(TodoDao.class);

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @PostMapping("/todo/add/post")
    public String postTodo(@ModelAttribute Todo todo) {
        if (!todo.getTitle().isEmpty() || !todo.getPriority().isEmpty()) {
            todoDao.save(todo);
        }
        return "redirect:/todo/list";
    }

    @GetMapping("/todo/list")
    public ModelAndView getTodos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("todolist");
        modelAndView.addObject("todos", todoDao.findAll());
        return modelAndView;
    }

    @GetMapping("/todo/addpage")
    public String addTodos() {
        return "todoadd";
    }

    @PostMapping(value = "/todo/delete")
    public String deleteTodo(@RequestParam("id") int id) {
        todoDao.delete(id);
        return "redirect:/todo/list";
    }

    @GetMapping(value = "/todo/update")
    public ModelAndView updateTodo(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Todo byId = todoDao.findById(id);
        modelAndView.setViewName("todoupdate");
        modelAndView.addObject(byId);
        return modelAndView;
    }


    @PostMapping(value = "/todo/update")
    public String updateTodoDone(@RequestParam("id") int id, @RequestParam("title") String newTitle, @RequestParam("priority") String newPriority) {
        Todo byId = todoDao.findById(id);
        if (!newPriority.isEmpty() || !newTitle.isEmpty()) {
            byId.setTitle(newTitle);
            byId.setPriority(newPriority);
            todoDao.update(byId);
        }
        return "redirect:/todo/list";
    }




}
