package com.example.securityprac.config.auth;

// 시큐리티가 /login을 낚아채서 로그인을 진행 시킴
// 로그인 진행이 완료가 되면 security session을 만들어줌 (SecurityContextHolder)
// 오브젝트 => Authentication 타입의 객체
// Authentication 안에 User정보가 있어야함 => principals (유저정보) / credentials (비밀번호 검증화) / authorization (인가)
// User오브젝트 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails

import com.example.securityprac.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collection;
    }

    // 해당 User의 Password를 리턴하는 곳
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    // 계정이 만료되었는가?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 정지되었는가?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정의 비밀번호가 n일을 넘었는가
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되었는가?
    @Override
    public boolean isEnabled() {
        // 서비스 내에서 1년 동안 회원이 로그인을 안할 때 -> 휴면 계정으로 변경
        // 현재시간 - 로그인 시간 => 1년 초과이면 return false
        // Model에서는 private Timestamp lastLogin; 같은 데이터 필요
        return true;
    }
}
