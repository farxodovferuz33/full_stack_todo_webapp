package org.example.spring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.spring.config.security.CustomUserDetails;
import org.example.spring.dao.TodoDao;
import org.example.spring.domain.AuthUser;
import org.example.spring.exception.AccessDenied;
import org.example.spring.exception.NotFoundException;
import org.example.spring.model.Todo;
import org.example.spring.session.SessionUser;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TodoController {
    private final SessionUser sessionUser;
    ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("ioc_settings.xml");
    TodoDao todoDao = context.getBean(TodoDao.class);

    public TodoController(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/todo/addpage")
    public String addTodos(Model model) {
        model.addAttribute("todo", new Todo());
        return "todoadd";
    }

    @PostMapping("/todo/add/post")
    public String postTodo(@Valid @ModelAttribute("todo") Todo todo, BindingResult errors) {

        if (errors.hasErrors()) {
            return "todoadd";
        }

        AuthUser user = sessionUser.getUser();

        todo.setUser_id(user.getId());
        todoDao.save(todo);

        return "redirect:/todo/list";
    }

    @GetMapping("/todo/list")
    public ModelAndView getTodos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("todolist");
        AuthUser user = sessionUser.getUser();

        Long id = user.getId();
//        userDetails.getAuthUser().getId();
        modelAndView.addObject("todos", todoDao.selectByUserId(id));

        return modelAndView;
    }



    @PostMapping(value = "/todo/delete")
    public String deleteTodo(@RequestParam("id") int id, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            AuthUser user = sessionUser.getUser();
            if (!todoDao.deleteByUserId(id, user.getId()))
                throw new NotFoundException("Todo not Found with id:%s".formatted(id), "/todo/list");
            return "redirect:/todo/list";
        }
        return "redirect:/auth/login";
    }

    @GetMapping(value = "/todo/update")
    public ModelAndView updateTodo(@RequestParam("id") int id, @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDenied {
        ModelAndView modelAndView = new ModelAndView();
        Todo byId = todoDao.findByIdByUserId(id, userDetails.getAuthUser().getId());
        modelAndView.setViewName("todoupdate");
        modelAndView.addObject(byId);
        return modelAndView;
    }

    @PostMapping(value = "/todo/update")
    public String updateTodoDone(@RequestParam("id") int id, @RequestParam("title") String newTitle, @RequestParam("priority") String newPriority) throws AccessDenied {
        Todo byId = todoDao.findById(id);
        if (!newPriority.isEmpty() || !newTitle.isEmpty()) {
            AuthUser user = sessionUser.getUser();
            byId.setTitle(newTitle);
            byId.setPriority(newPriority);
            todoDao.updateByUserId(byId, user.getId());
        }
        return "redirect:/todo/list";
    }


    @ExceptionHandler({NotFoundException.class})
    public ModelAndView error_404(HttpServletRequest request, NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.addObject("back_path", e.getPath());
        return modelAndView;
    }

}
