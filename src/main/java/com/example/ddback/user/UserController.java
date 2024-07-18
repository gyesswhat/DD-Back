package com.example.ddback.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private S3Service s3Service;

    @PostMapping(value = "/join", consumes = {"multipart/form-data"})
    public ResponseEntity<?> join(@RequestPart(name="data") JoinRequestDto joinRequestDto,
                                  @RequestPart(name="file") MultipartFile multipartFile) {
        // 1. 유저 엔티티 생성
        User user = new User(
                joinRequestDto.getUserId(),
                joinRequestDto.getNickname(),
                joinRequestDto.getPassword(),
                joinRequestDto.getName(),
                joinRequestDto.getAge(),
                joinRequestDto.getGender(),
                joinRequestDto.getPartnerGender(),
                joinRequestDto.getVeganState(),
                joinRequestDto.getHobby(),
                joinRequestDto.getLocation(),
                joinRequestDto.getPartnerLocation(),
                null
        );
        // 2. 이미지 엔티티 생성 및 매핑
        Image image = s3Service.upload(multipartFile);
        user.setImageLink(image.getImageLink());
        // 3. 저장
        User saved = userService.join(user);
        // 4. DTO 생성
        JoinResponseDto joinResponseDto = new JoinResponseDto(saved.getUserId());
        return (joinResponseDto != null)?
                ResponseEntity.status(HttpStatus.OK).body(joinResponseDto):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입에 실패했습니다");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        User target = userService.login(loginRequestDto);
        LoginResponseDto loginResponseDto = new LoginResponseDto(target.getUserId());
        return (loginResponseDto != null)?
                ResponseEntity.status(HttpStatus.OK).body(loginResponseDto):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인에 실패했습니다.");
    }
}
