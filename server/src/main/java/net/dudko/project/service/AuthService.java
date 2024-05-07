package net.dudko.project.service;


import net.dudko.project.model.dto.LoginDto;
import net.dudko.project.model.dto.RegisterDto;

public interface AuthService {

    String register(RegisterDto registerDto);

    String login(LoginDto loginDto);

}
