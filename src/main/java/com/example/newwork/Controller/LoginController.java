package com.example.newwork.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.newwork.Entity.Role;
import com.example.newwork.Entity.User;
import com.example.newwork.Entity.UserDto;
import com.example.newwork.Repo.RoleRepository;
import com.example.newwork.Repo.UserRepository;
import com.example.newwork.Repo.UserServiceImpl;
import com.example.newwork.Security.TbConstants;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class LoginController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/accessDenied")
    public String getAccessDen() {
        return "accessDenied";
    }

    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        // Check if the user already exists
        Optional<User> existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser.isPresent()) {
            // If the user already exists, reject the registration and return to the
            // registration page with an error message
            result.rejectValue("email", null, "User already registered !!!");
            return "/registration";
        }

        // If there are validation errors, return to the registration page with the
        // entered user data
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/registration";
        }

        // Find the user role in the database
        Optional<Role> userRole = roleRepository.findById(TbConstants.Roles.USER);

        // Create a set to store user roles
        Set<Role> userRoles = new HashSet<>();

        // If the user role is present in the database, add it to the set
        if (userRole.isPresent() && userRole != null) {
            userRoles.add(userRole.get());
        } else {
            // If the user role is not present, create it and save it to the database
            Role user = new Role();
            user.setId(TbConstants.Roles.USER);
            user.setName("ROLE_USER");

            Role admin = new Role();
            admin.setId(TbConstants.Roles.ADMIN);
            admin.setName("ROLE_ADMIN");

            roleRepository.save(admin);
            roleRepository.save(user);

            // Add the newly created roles to the set
            userRoles.add(admin);
            userRoles.add(user);
        }

        // Create a new user entity
        User newUser = new User();
        // Set the user roles
        newUser.setRoles(userRoles);
        // Set the user name, email, and encrypted password
        newUser.setUserName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Save the new user to the database
        userRepository.save(newUser);

        // Redirect to the registration page with a success message
        return "redirect:/registration?success";
    }

}
