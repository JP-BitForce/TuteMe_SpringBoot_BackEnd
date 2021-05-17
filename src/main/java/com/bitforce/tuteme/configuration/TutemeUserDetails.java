package com.bitforce.tuteme.configuration;

import com.bitforce.tuteme.model.UserAuth;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TutemeUserDetails implements OAuth2User, UserDetails {

    private Long id;
    private String email;
    private String password;
    private List<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public TutemeUserDetails(Long id, String email, String password, List<GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static TutemeUserDetails create(UserAuth userAuth) {
        List<GrantedAuthority> authorities = Arrays.stream(userAuth.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new TutemeUserDetails(
                userAuth.getId(),
                userAuth.getEmail(),
                userAuth.getPassword(),
                authorities
        );
    }

    public static TutemeUserDetails create(UserAuth user, Map<String, Object> attributes) {
        TutemeUserDetails userPrincipal = TutemeUserDetails.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
