package com.ua.sutty.app;

import com.ua.sutty.dao.UsersDao;
import com.ua.sutty.dao.UsersDaoJdbcTemplateImpl;
import com.ua.sutty.models.User;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setUrl("jdbc:mysql://localhost:3306/fix_mvc?serverTimezone=UTC");

        UsersDao usersDao = new UsersDaoJdbcTemplateImpl(dataSource);

        List<User> users = usersDao.findAll();
        System.out.println(users);

    }

}
