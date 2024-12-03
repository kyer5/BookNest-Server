package spring.univ_board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.univ_board.controller.dto.request.LoginRequest;
import spring.univ_board.controller.dto.request.SignUpRequest;
import spring.univ_board.controller.dto.response.LoginResponse;
import spring.univ_board.controller.dto.response.UserInformationResponse;
import spring.univ_board.domain.User;
import spring.univ_board.repository.UserRepository;

import javax.security.sasl.AuthenticationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public LoginResponse login(final LoginRequest loginRequest) throws AuthenticationException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new AuthenticationException("This account does not exist."));
        validatePassword(password, user);
        return new LoginResponse(user.getId());
    }

    @Transactional
    public Long signUp(final SignUpRequest signUpRequest) {
        User signUpUser = User.singUp(signUpRequest);
        User savedUser = userRepository.save(signUpUser);
        return savedUser.getId();
    }

    public UserInformationResponse getUserInformation(User user) {
        return UserInformationResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .build();
    }

    private void validatePassword(String password, User user) throws AuthenticationException {
        if (password.equals(user.getPassword())) {
            log.info("Login successful.");
        } else {
            log.info("Password incorrect.");
            throw new AuthenticationException("Password incorrect.");
        }
    }
}
