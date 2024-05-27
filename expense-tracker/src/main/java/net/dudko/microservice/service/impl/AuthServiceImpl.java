package net.dudko.microservice.service.impl;

import lombok.AllArgsConstructor;

import net.dudko.microservice.domain.entity.Role;
import net.dudko.microservice.domain.entity.User;
import net.dudko.microservice.domain.repository.RoleRepository;
import net.dudko.microservice.domain.repository.UserRepository;
import net.dudko.microservice.model.dto.LoginDto;
import net.dudko.microservice.model.dto.RegisterDto;
import net.dudko.microservice.model.exception.ExpenseTrackerAPIException;
import net.dudko.microservice.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ExpenseTrackerAPIException("Name of user already exists!");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ExpenseTrackerAPIException("Email already exists!");
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
