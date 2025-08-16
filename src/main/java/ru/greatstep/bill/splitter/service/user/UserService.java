package ru.greatstep.bill.splitter.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.greatstep.bill.splitter.models.dto.user.UserCreateResponse;
import ru.greatstep.bill.splitter.models.dto.user.UserDto;
import ru.greatstep.bill.splitter.models.dto.user.UserLoginResponse;
import ru.greatstep.bill.splitter.models.entity.UserInfoEntity;
import ru.greatstep.bill.splitter.repo.UserInfoRepo;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepo userInfoRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserCreateResponse createUser(UserDto dto) {
        var user = new UserInfoEntity();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        userInfoRepo.save(user);
        return new UserCreateResponse("Success");
    }


    public UserLoginResponse login(UserDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new UserLoginResponse(jwtService.generateToken(userDetails));
    }

    public UserDto updateUser(String authHeader, UserDto dto) {
        var username = jwtService.extractUsernameFromHeader(authHeader);
        var user = userInfoRepo.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getEmail().equals(username)) {
            throw new RuntimeException("У вас нет прав на редактирование этого пользователя");
        }

        boolean isChanged = false;

        if (isNotBlank(dto.password())) {
            user.setPassword(passwordEncoder.encode(dto.password()));
            isChanged = true;
        }

        if (isNotBlank(dto.email())) {
            user.setEmail(dto.email());
            isChanged = true;
        }

        if (isChanged) {
            userInfoRepo.save(user);
        }

        return new UserDto(user.getEmail(), "*******");
    }
}
