package com.example.securityprac.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 서버에 회원의 기본 정보가 들어가있음, registrationId로 어떤 OAuth로 로그인했는지 확인 가능
        System.out.println("userRequest: " + userRequest.getClientRegistration());
        // 회원의 토큰 정보가 들어가있음
        System.out.println("userRequest: " + userRequest.getAccessToken().getTokenValue());
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> Code를 리턴(OAuth-client 라이브러리) -> AccessToken요청
        // userRequest정보 -> 회원프로필을 받아야함(loadUser 함수) -> 구글로 부터 회원프로필을 받아줌
        System.out.println("userRequest: " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
