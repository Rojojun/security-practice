package com.example.securityprac.controller;

import com.example.securityprac.config.auth.PrincipalDetails;
import com.example.securityprac.model.Member;
import com.example.securityprac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {
        // DI(의존성 주입)
        System.out.println("/test/login ================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        // PrincipalDetails로 다운캐스팅이 가능한 이유는? 내부에 UserDetails를 상속받았기 때문
        System.out.println("authentication:" + principalDetails.getMember());
        // @AuthenticationPrincipal을 통해 유저 정보를 가져올 수 있음
        System.out.println("userDetails:" + userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLoginTest(Authentication authentication,
                                                   @AuthenticationPrincipal OAuth2User oAuth2) {
        System.out.println("/test/oauth/login ================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication:" + oAuth2User.getAttributes());

        System.out.println("oAuth2UserPrincipal:" + oAuth2.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    // OAuth 로그인을 해도 Principal Details Type GET!
    // 일반 로그인을 해도 Principal Details Type GET!
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("PrincipalDetails : " + principalDetails.getMember() );
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(Member user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        user.setPassword(encPassword);

        userRepository.save(user);
        return "redirect:/loginForm";
    }

    // 하나만 인가를 걸 때 사용
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    // 여러개의 인가를 걸 때 사용
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
