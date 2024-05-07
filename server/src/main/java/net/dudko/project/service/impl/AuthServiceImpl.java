package net.dudko.project.service.impl;

import lombok.AllArgsConstructor;

import net.dudko.project.domain.entity.Role;
import net.dudko.project.domain.entity.User;
import net.dudko.project.domain.repository.RoleRepository;
import net.dudko.project.domain.repository.UserRepository;
import net.dudko.project.model.dto.LoginDto;
import net.dudko.project.model.dto.RegisterDto;
import net.dudko.project.model.exceprion.MoneronAPIException;
import net.dudko.project.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new MoneronAPIException("UserName already exists!");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new MoneronAPIException("Email already exists!");
        }
        User user = User.builder()
                .name(registerDto.getName())
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roles);
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "User logged in successfully";
    }

}
