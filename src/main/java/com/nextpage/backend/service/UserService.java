package com.nextpage.backend.service;

import com.nextpage.backend.config.jwt.TokenService;
import com.nextpage.backend.dto.request.UserCreateRequest;
import com.nextpage.backend.dto.response.SignUpResponseDTO;
import com.nextpage.backend.dto.response.UserResponseDTO;
import com.nextpage.backend.entity.User;
import com.nextpage.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public SignUpResponseDTO createUser(UserCreateRequest request) {
        String email = request.getEmail();
        String nickname = request.getNickname();
        if (userRepository.existsByEmail(email)) { // 이미 존재하는 이메일이면 유저 생성 x
            throw new NoSuchElementException("이미 존재하는 이메일입니다.");
        }
        User newUser = new User().builder()
                .email(email)
                .nickname(nickname)
                .build();
        newUser = userRepository.save(newUser); // db에 유저 저장 - 회원 가입
        newUser = updateUserId(newUser.getId(), newUser.getNickname());
        return new SignUpResponseDTO(newUser, tokenService.generateAccessToken(newUser.getId()), tokenService.generateRefreshToken());
    }

    public User updateUserId(Long id, String nickname) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        user.update(nickname + "#" + user.getId());
        userRepository.save(user);
        return user;
    }

    public void deleteUser(HttpServletRequest request) {
        tokenService.validateAccessToken(request); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        userRepository.delete(user);
    }

    public UserResponseDTO getUserInfo(HttpServletRequest request){
        tokenService.validateAccessToken(request); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        return new UserResponseDTO(user);
    }
}
