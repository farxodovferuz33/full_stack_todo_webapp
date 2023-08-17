package org.example.spring;

import org.example.spring.dao.TodoDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("ioc_settings.xml");
        TodoDao dao = context.getBean(TodoDao.class);
        System.out.println(dao.findById(1));
        System.out.println(dao.findAll());
    }
}
