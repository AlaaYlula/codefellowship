package com.example.CodeFellowship.Repository;

import com.example.CodeFellowship.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Integer> {
    AppUser findByUsername(String username);
    AppUser findById(int id);
}
