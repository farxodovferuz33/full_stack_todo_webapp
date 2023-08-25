package org.example.spring.controller;


import org.example.spring.dao.AdminDAO;
import org.example.spring.dao.AuthUserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    private final AuthUserDao authUserDao;
    private final AdminDAO adminDAO;

    public AdminController(AuthUserDao authUserDao, AdminDAO adminDAO) {
        this.authUserDao = authUserDao;
        this.adminDAO = adminDAO;
    }


    @GetMapping("/users/list")
//    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView adminsPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("users", authUserDao.getAllUsers());
        return modelAndView;
    }

    @PostMapping("/activate/user")
//    @PreAuthorize("hasRole('ADMIN')")
    public String activateUser(@RequestParam("id") Long id) {
        adminDAO.activate(id);
        return "redirect:/admin/users/list";
    }

    @PostMapping("/deactivate/user")
//    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateUser(@RequestParam("id") Long id) {
        adminDAO.deactivate(id);
        return "redirect:/admin/users/list";
    }

}
