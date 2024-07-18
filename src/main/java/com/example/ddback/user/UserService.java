package com.example.ddback.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User join(User user) {
        User saved = userRepository.save(user);
        if (saved!=null) return saved;
        else return null;
    }

    public User login(LoginRequestDto loginRequestDto) {
        // 1. 타겟 찾기
        User target = userRepository.findByUserId(loginRequestDto.getUserId());
        if (target==null) return null;

        // 2. 데이터 검증
        if (target.getPassword().equals(loginRequestDto.getPassword())) return target;
        else return null;
    }
}
