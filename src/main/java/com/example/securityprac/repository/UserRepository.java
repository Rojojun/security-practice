package com.example.securityprac.repository;

import com.example.securityprac.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Integer> {

}
