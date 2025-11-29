package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.models.dto.user.CustomOAuthUser;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.security.Role;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository userRepository;

    public CustomOAuth2UserService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(userRequest);

        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(oauthUser.getAttribute("name"));
                    u.setPasswordHash("OAUTH_GOOGLE_USER");
                    u.setRole(Role.ROLE_USER);
                    return userRepository.save(u);
                });

        return new CustomOAuthUser(oauthUser, user.getUserId());
    }
}

