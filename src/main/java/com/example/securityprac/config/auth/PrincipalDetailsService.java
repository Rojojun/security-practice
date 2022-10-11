package com.example.securityprac.config.auth;

import com.example.securityprac.model.Member;
import com.example.securityprac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login")'
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    // 시큐리티 session( = Authentication ( = UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = userRepository.findByUsername(username);
        if (memberEntity != null) {
            return new PrincipalDetails(memberEntity);
        }
        return null;
    }
}
