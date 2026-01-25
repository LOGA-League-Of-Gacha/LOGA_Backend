package com.loga.infrastructure.security;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loga.domain.user.entity.User;
import com.loga.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2 사용자 정보 처리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.security.oauth2.client.registration.google.client-id", matchIfMissing = false)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration()
                .getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String providerId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> createUser(provider, providerId, email, name, picture));

        // Update profile if changed
        boolean updated = false;
        if (name != null && !name.equals(user.getNickname())) {
            user.updateProfile(name, user.getProfileImage());
            updated = true;
        }
        if (picture != null && !picture.equals(user.getProfileImage())) {
            user.updateProfile(user.getNickname(), picture);
            updated = true;
        }
        if (updated) {
            userRepository.save(user);
        }

        log.info("OAuth2 login: {} ({})", email, provider);
        return new CustomOAuth2User(user, attributes);
    }

    private User createUser(String provider, String providerId, String email, String name, String picture) {
        User user = User.createOAuth2User(provider, providerId, email, name, picture);
        return userRepository.save(user);
    }
}
