package com.example.ddback.swipe;

import com.example.ddback.user.User;
import com.example.ddback.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class SwipeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikematchRepository likematchRepository;
    @Autowired
    private MatchedUsersRepository matchedUsersRepository;

    public ArrayList<SwipePostResponseDto> swipe(String userId) {
        log.info("Starting swipe method for userId: {}", userId);

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Current user: {}", currentUser);

        List<String> currentUserLocations = currentUser.getLocation();
        if (currentUserLocations.isEmpty()) {
            log.warn("Current user's location list is empty");
            throw new RuntimeException("Current user's location list is empty");
        }

        List<String[]> currentUserPartnerLocations = currentUser.getPartnerLocation();
        if (currentUserPartnerLocations.isEmpty()) {
            log.warn("Current user's partner location list is empty");
            throw new RuntimeException("Current user's partner location list is empty");
        }

        List<User> users = userRepository.findAll();
        log.info("Total users found: {}", users.size());

        List<User> matchingUsers = new ArrayList<>();
        for (User user : users) {
            log.debug("Checking user: {}", user.getUserId());

            if (!currentUser.getPartnerGender().equals("상관없음") && !currentUser.getPartnerGender().equals(user.getGender())) {
                continue;
            }

            List<String[]> userPartnerLocations = user.getPartnerLocation();
            List<String> userLocations = user.getLocation();

            if (userPartnerLocations.isEmpty() || userLocations.isEmpty()) {
                log.debug("User {} has empty partner locations or locations", user.getUserId());
                continue;
            }

            boolean shouldAddUser = false;
            for (String[] currentUserPartnerLocation : currentUserPartnerLocations) {
                for (String[] userPartnerLocation : userPartnerLocations) {
                    if (currentUserPartnerLocation[0].equals(userPartnerLocation[0]) &&
                            currentUserPartnerLocation[1].equals(userPartnerLocation[1])) {
                        shouldAddUser = true;
                    }
                }
                if (userPartnerLocations.stream()
                        .anyMatch(partnerLocation -> partnerLocation[0].equals(currentUserLocations.get(0)) &&
                                partnerLocation[1].equals(currentUserLocations.get(1)))) {
                    shouldAddUser = true;
                }
                if (currentUserPartnerLocations.stream()
                        .anyMatch(partnerLocation -> partnerLocation[0].equals(userLocations.get(0)) &&
                                partnerLocation[1].equals(userLocations.get(1)))) {
                    shouldAddUser = true;
                }
            }

            if (shouldAddUser) {
                matchingUsers.add(user);
            }
        }

        log.info("Matching users found: {}", matchingUsers.size());
        matchingUsers.removeIf(user -> user.getUserId().equals(userId));

        ArrayList<SwipePostResponseDto> responses = new ArrayList<>();
        for (User matchingUser : matchingUsers) {
            if (matchingUser.getLocation().size() > 1) {
                responses.add(new SwipePostResponseDto(
                        matchingUser.getUserId(),
                        matchingUser.getNickname(),
                        matchingUser.getAge(),
                        matchingUser.getVeganState(),
                        matchingUser.getHobby(),
                        matchingUser.getImageLink(),
                        matchingUser.getLocation().get(1)
                ));
            }
        }

        log.info("Returning {} responses", responses.size());
        return responses;    }

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
                        partner.getImageLink(),
                        match.getMatchedTime()
                ));
            }
            else {
                User partner = userRepository.findById(match.getUser1Id()).orElse(null);
                responses.add(new MatchResponseDto(
                        partner.getUserId(),
                        partner.getNickname(),
                        partner.getImageLink(),
                        match.getMatchedTime()
                ));
            }
        }
        return responses;
    }
}
