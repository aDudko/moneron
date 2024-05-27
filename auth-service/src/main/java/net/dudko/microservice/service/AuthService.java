package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.LoginDto;
import net.dudko.microservice.model.dto.RegisterDto;

public interface AuthService {

    String register(RegisterDto registerDto);

    String login(LoginDto loginDto);

}
