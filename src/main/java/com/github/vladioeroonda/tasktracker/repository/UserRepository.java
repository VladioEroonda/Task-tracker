package com.github.vladioeroonda.tasktracker.repository;

import com.github.vladioeroonda.tasktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
