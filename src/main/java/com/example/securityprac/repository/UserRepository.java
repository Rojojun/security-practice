package com.example.securityprac.repository;

import com.example.securityprac.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserRepository extends JpaRepository<Member, Integer> {
    // findBy 규칙 -> Username 문법
    // JPA Query Method를 검색하면 JPA에서 제공하는 Query에 대해서 알 수 있음
    // select * from MEMBER where username = 1?
    public Member findByUsername(String username);

    // select * from MEMBER where email = 1?
    public Member findByEmail(String email);
}
