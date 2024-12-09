package com.example.newwork.Repo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.newwork.Entity.User;

@Service
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // public void saveUser(UserDto userDto) {
    // Role role = roleRepository.findByName(TbConstants.Roles.USER);
    
    // if (role == null) {
    // role = roleRepository.save(new Role(TbConstants.Roles.USER));
    // }
    
    // User temp = new User();
    
    // temp.setEmail(userDto.getEmail());
    // temp.setPassword(passwordEncoder.encode(userDto.getPassword()));
    // temp.setName(userDto.getName());
    // temp.setRoles(Arrays.asList(role));
    // userRepository.save(temp);
    // }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

