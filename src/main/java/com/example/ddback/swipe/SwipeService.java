package com.example.ddback.swipe;

import com.example.ddback.user.User;
import com.example.ddback.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SwipeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikematchRepository likematchRepository;
    @Autowired
    private MatchedUsersRepository matchedUsersRepository;

    public ArrayList<SwipePostResponseDto> swipe(String userId) {
        // 1. 현재 사용자 정보 가져오기
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. 현재 사용자의 관심 위치 리스트 가져오기
        List<String> currentUserLocations = currentUser.getLocation();

        // 3. 현재 사용자의 상대방 위치 리스트 가져오기
        List<String[]> currentUserPartnerLocations = currentUser.getPartnerLocation();

        // 4. 매칭된 사용자들을 담을 리스트 생성
        List<User> matchingUsers = new ArrayList<>();


        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<String[]> userPartnerLocations = user.getPartnerLocation();
            List<String> userLocations = user.getLocation();

            boolean shouldAddUser = false;

            // Check all conditions in one loop
            for (String[] currentUserPartnerLocation : currentUserPartnerLocations) {
                for (String[] userPartnerLocation : userPartnerLocations) {
                    if (currentUserPartnerLocation[0].equals(userPartnerLocation[0])
                            && currentUserPartnerLocation[1].equals(userPartnerLocation[1])) {
                        shouldAddUser = true;
                    }
                }
                if (userPartnerLocations.stream()
                        .anyMatch(partnerLocation -> partnerLocation[0].equals(currentUserLocations.get(0))
                                && partnerLocation[1].equals(currentUserLocations.get(1)))) {
                    shouldAddUser = true;
                }
                if (currentUserPartnerLocations.stream()
                        .anyMatch(partnerLocation -> partnerLocation[0].equals(userLocations.get(0))
                                && partnerLocation[1].equals(userLocations.get(1)))) {
                    shouldAddUser = true;
                }
            }

            if (shouldAddUser) {
                matchingUsers.add(user);
            }
        }
        // 7. 중복 제거 및 현재 사용자 제외
        matchingUsers.removeIf(user -> user.getUserId().equals(userId));

        // 8. 최종적으로 반환할 SwipePostResponseDto 리스트 생성
        ArrayList<SwipePostResponseDto> responses = new ArrayList<>();

        for (User matchingUser : matchingUsers) {
            responses.add(new SwipePostResponseDto(
                    matchingUser.getUserId(),
                    matchingUser.getNickname(),
                    matchingUser.getAge(),
                    matchingUser.getGender(),
                    matchingUser.getImageLink(),
                    matchingUser.getLocation().get(1)
            ));
        }

        return responses;
    }

    public Boolean likematch(SwipeLikeRequestDto swipeLikeRequestDto) {
        // 1. Likematch 저장
        Likematch likematch = new Likematch(
                null,
                swipeLikeRequestDto.getUserId(),
                swipeLikeRequestDto.getPartnerId()
        );
        likematchRepository.save(likematch);

        // 2. 매치 여부 확인
        Likematch matched = likematchRepository.findPartnerById(likematch.getFromUserId(), likematch.getToUserId());

        // 3. 매치된 사용자들 저장
        if (matched!= null) {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
            String matchedTime = localDateTime.format(dateTimeFormatter);
            MatchedUsers matchedUsers = new MatchedUsers(null, likematch.getFromUserId(), likematch.getToUserId(), matchedTime);
            matchedUsersRepository.save(matchedUsers);
        }

        // 4. 리턴
        return (matched != null)? true: false;
    }

    public ArrayList<MatchResponseDto> showMatchedUsers(MatchRequestDto matchRequestDto) {
        ArrayList<MatchedUsers> matches = matchedUsersRepository.findAllByUserId(matchRequestDto.getUserId());
        ArrayList<MatchResponseDto> responses = new ArrayList<>();
        for (MatchedUsers match : matches) {
            if (match.getUser1Id().equals(matchRequestDto.getUserId())) {
                User partner = userRepository.findById(match.getUser2Id()).orElse(null);
                responses.add(new MatchResponseDto(
                        partner.getUserId(),
                        partner.getNickname(),
                        partner.getImageLink()
                ));
            }
            else {
                User partner = userRepository.findById(match.getUser1Id()).orElse(null);
                responses.add(new MatchResponseDto(
                        partner.getUserId(),
                        partner.getNickname(),
                        partner.getImageLink()
                ));
            }
        }
        return responses;
    }
}
