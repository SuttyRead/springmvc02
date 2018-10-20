package com.ua.sutty.controller;

import com.ua.sutty.forms.UserForm;
import com.ua.sutty.models.User;
import com.ua.sutty.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 18.04.2018
 * UsersWithJpaController
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
@Controller
public class UsersWithJpaController {

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(path = "/jpa/users", method = RequestMethod.GET)
    public ModelAndView getUsers(@RequestParam(required = false, name = "first_name") String firstName) {
        List<User> users = null;

        if (firstName != null) {
            users = usersRepository.findAllByFirstName(firstName);
        } else {
            users = usersRepository.findAll();
        }
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("usersFromServer", users);
        return modelAndView;
    }

    @RequestMapping(path = "/jpa/users", method = RequestMethod.POST)
    public String addUser(UserForm userForm) {
        User newUser = User.from(userForm);
        usersRepository.save(newUser);
        return "redirect:/jpa/users";
    }

    @RequestMapping(path = "/jpa/users/{user-id}", method = RequestMethod.GET)
    public ModelAndView getUserById(@PathVariable("user-id") Long userId) {
        User user = usersRepository.findUsersById(userId);
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("usersFromServer", Collections.singletonList(user));
        return modelAndView;
    }

}
