package com.travelplanner.backend.oauth;

import com.travelplanner.backend.domain.AuthProvider;
import com.travelplanner.backend.domain.User;
import com.travelplanner.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId(); // google or kakao
        String email = extractEmail(oAuth2User, provider);
        String nickname = extractNickname(oAuth2User, provider);

        // 사용자 저장 또는 조회
        User savedUser = userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .provider(AuthProvider.valueOf(provider.toUpperCase()))
                        .build())
        );

        // JWT 발급
        String token = jwtUtil.generateToken(savedUser.getEmail());

        // 기존 attributes 복사 후 token 추가
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("token", token);

        // 기본 식별자 설정 (google은 email, kakao는 id 또는 email)
        String nameAttributeKey = "email";
        if (provider.equals("kakao")) {
            nameAttributeKey = "id"; // 카카오는 보통 'id' 필드를 principal로 사용
        }

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                nameAttributeKey
        );
    }
    @SuppressWarnings("unchecked")
    private String extractEmail(OAuth2User user, String provider) {
        if (provider.equals("kakao")) {
            var kakaoAccount = (Map<String, Object>) user.getAttributes().get("kakao_account");
            return (String) kakaoAccount.get("email");
        }
        return (String) user.getAttributes().get("email");
    }

    @SuppressWarnings("unchecked")
    private String extractNickname(OAuth2User user, String provider) {
        if (provider.equals("kakao")) {
            var profile = (Map<String, Object>) ((Map<String, Object>) user.getAttributes().get("kakao_account")).get("profile");
            return (String) profile.get("nickname");
        }
        return (String) user.getAttributes().get("name"); // 구글은 name
    }
}
