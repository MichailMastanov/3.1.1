package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepositories(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/user")
    public String openUser(Principal principal, Model model){
        User userName = (User)userRepository.findByUsername(principal.getName());
        model.addAttribute("user", userName);
        return "user";
    }

    @GetMapping("/admin/list")
    public String usersAll(Model model){
        model.addAttribute("xxx", userRepository.findAll());
        return "admin";
    }

    @GetMapping("/admin/create_user")
    public String newUser(){
        return "newUser";
    }

    @PostMapping("/admin/create_user")
    public String addNewUser(@RequestParam(value = "username") String username,
                             @RequestParam(value = "password") String password,
                             @RequestParam("roles") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            System.out.println(role);
            roleSet.add(roleRepository.findByName(role.toLowerCase()));
        }
        User user = new User(username,password);
        user.setRole(roleSet);
        userRepository.save(user);
        return "redirect:/admin/list";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam Long id){
        userRepository.deleteById(id);
        return "redirect:/admin/list";
    }

    @PostMapping("/admin/update")
    public String updateUser(@RequestParam("username") String username, Model model){
        User user = userRepository.findByUsername(username);
        model.addAttribute("id", user.getId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        return "updateUser";
    }

    @PostMapping("/admin/up_user")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("roles") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleRepository.findByName(role.toLowerCase()));
        }
        User user = new User(username, password);
        user.setRole(roleSet);
        user.setId(id);
        userRepository.save(user);
        return "redirect:/admin/list";
    }


}
