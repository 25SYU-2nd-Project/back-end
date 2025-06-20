package com.example.brieflog.Service;

import com.example.brieflog.Dto.JwtResponse;
import com.example.brieflog.Dto.UserLoginRequest;
import com.example.brieflog.Entity.User;
import com.example.brieflog.Dto.UserJoinRequest;
import com.example.brieflog.Exception.CustomException;
import com.example.brieflog.Repository.UserRepository;
import com.example.brieflog.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired private JwtUtil jwtUtil;

    public void registerUser(UserJoinRequest req) {
        if (userRepository.existsByUserId(req.getUserId())) {
            throw new CustomException("이미 존재하는 아이디입니다.");
        }

        User user = new User();
        user.setUserId(req.getUserId());
        user.setUserPw(passwordEncoder.encode(req.getUserPw()));  // 암호화
        user.setUserName(req.getUserName());

        userRepository.save(user);
    }

    public JwtResponse login(UserLoginRequest req) {
        User user = userRepository.findByUserId(req.getUserId())
                .orElseThrow(() -> new CustomException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(req.getUserPw(), user.getUserPw())) {
            throw new CustomException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUserId());
        return new JwtResponse(token);
    }
}
