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
        // 1. 데이터 검증
        if (joinRequestDto.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("값이 잘못되었습니다.");
        // 2. 유저 엔티티 생성
        User user = new User(
                joinRequestDto.getUserId(),
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
        // 3. 이미지 엔티티 생성 및 매핑
        Image image = s3Service.upload(multipartFile);
        user.setImageLink(image.getImageLink());
        // 4. 저장
        User saved = userService.join(user);
        return (saved != null)?
                ResponseEntity.status(HttpStatus.OK).build():
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입에 실패했습니다");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        User target = userService.login(loginRequestDto);
        return (target != null)?
                ResponseEntity.status(HttpStatus.OK).build():
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인에 실패했습니다.");
    }
}
