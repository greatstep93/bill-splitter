package ru.greatstep.bill.splitter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greatstep.bill.splitter.models.dto.user.UserCreateResponse;
import ru.greatstep.bill.splitter.models.dto.user.UserDto;
import ru.greatstep.bill.splitter.models.dto.user.UserLoginResponse;
import ru.greatstep.bill.splitter.service.user.UserService;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/public/api/user/reg")
    public UserCreateResponse createUser(@RequestBody @Valid UserDto dto) {
        return userService.createUser(dto);
    }

    @PostMapping("/public/api/user/login")
    public UserLoginResponse login(@RequestBody @Valid UserDto dto) {
        return userService.login(dto);
    }

    @PatchMapping("/private/api/user/update")
    public UserDto updateUser(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestBody UserDto dto
    ) {
        return userService.updateUser(authHeader, dto);
    }
}
