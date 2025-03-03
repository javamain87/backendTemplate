package com.template.exam.biz.common.jwt.service;

import com.template.exam.biz.auth.mapper.AuthMapper;
import com.template.exam.biz.auth.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AuthMapper authRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserVO entity = authRepository.findByEmail(email);
        if(ObjectUtils.isEmpty(entity)) {
            throw new UsernameNotFoundException("Not found auth with email: " + email);
        }
        Set<GrantedAuthority> grantedAuthorities = entity.getRoles().stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        return User.builder()
                .username(entity.getEmail())
                .password(entity.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }

    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                .collect(Collectors.toList());
    }
}
