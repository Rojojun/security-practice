package com.example.securityprac.config.oauth;

import com.example.securityprac.config.auth.PrincipalDetails;
import com.example.securityprac.config.oauth.provider.FacebookUserInfo;
import com.example.securityprac.config.oauth.provider.GoogleUserInfo;
import com.example.securityprac.config.oauth.provider.NaverUserInfo;
import com.example.securityprac.config.oauth.provider.OAuth2UserInfo;
import com.example.securityprac.model.Member;
import com.example.securityprac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 함수
    // 함수 종료시 @AuthenticationPrincipal 생성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 서버에 회원의 기본 정보가 들어가있음, registrationId로 어떤 OAuth로 로그인했는지 확인 가능
        System.out.println("userRequest: " + userRequest.getClientRegistration());
        // 회원의 토큰 정보가 들어가있음
        System.out.println("userRequest: " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> Code를 리턴(OAuth-client 라이브러리) -> AccessToken요청
        // userRequest정보 -> 회원프로필을 받아야함(loadUser 함수) -> 구글로 부터 회원프로필을 받아줌
        System.out.println("userRequest: " + oAuth2User.getAttributes());

        // 회원가입 강제진행
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("Google");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("FaceBook");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("Naver");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            throw new RuntimeException("지원하지 않는 OAuth");
        }
        // String provider = userRequest.getClientRegistration().getClientId(); // google
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId(); // google의 provider 아이디
        String providerEmail = oAuth2UserInfo.getEmail();
        String username = provider + "_" +providerId; // google_000000000000000
        String password = bCryptPasswordEncoder.encode("xxxx");
        String role = "ROLE_USER";

        Member member = userRepository.findByUsername(username);
        if (member == null) {
           member = member.builder()
                   .username(username)
                   .password(password)
                   .email(providerEmail)
                   .role(role)
                   .provider(provider)
                   .providerId(providerId)
                   .build();
           userRepository.save(member);
        } else {
            throw new RuntimeException("User가 이미 존재합니다.");
        }

        // 회원가입을 강제로 진행
        //return super.loadUser(userRequest);
        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
