package com.example.newwork.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.newwork.Entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Role findByName(String name);

    Role save(Role role);

}