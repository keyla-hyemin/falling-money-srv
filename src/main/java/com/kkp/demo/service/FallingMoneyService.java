package com.kkp.demo.service;

import com.kkp.demo.handler.error.*;
import com.kkp.demo.repository.FallingMoneyHistoryRepository;
import com.kkp.demo.repository.FallingMoneyInfoRepository;
import com.kkp.demo.repository.FallingMoneyRepository;
import com.kkp.demo.repository.FallingMoneyTokenRepository;
import com.kkp.demo.dto.FallingMoney;
import com.kkp.demo.dto.FallingMoneyHistory;
import com.kkp.demo.dto.FallingMoneyInfo;
import com.kkp.demo.dto.FallingMoneyToken;
import com.kkp.demo.utils.RandomMoneyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FallingMoneyService {
    private final FallingMoneyTokenRepository fallingMoneyTokenRepository;
    private final FallingMoneyRepository fallingMoneyRepository;
    private final FallingMoneyInfoRepository fallingMoneyInfoRepository;
    private final FallingMoneyHistoryRepository fallingMoneyHistoryRepository;

    /**
     * 머니 뿌리기 비즈니스 수행
     */
    public Mono<String> startFallingMoney(int amount, int personnel, String userId, String roomId) {

        // 뿌리기 머니 토큰 발급
        String tokenStr = StringUtils.EMPTY;
        do{
            tokenStr = RandomStringUtils.randomAlphabetic(3);
        }
        while(fallingMoneyTokenRepository.existsById(tokenStr));

        // 뿌리기 머니 토큰 적재
        FallingMoneyToken fallingMoneyToken = fallingMoneyTokenRepository.save(FallingMoneyToken.builder().token(tokenStr).build());

        if(fallingMoneyToken == null) {
            log.error("failed to save fallingMoneyToken");
            return Mono.error(DBProcessingException::new);
        }

        // 뿌리기 머니 적재
        FallingMoney fallingMoney = fallingMoneyRepository.save(FallingMoney.builder().
                money(RandomMoneyUtils.randomMoneyList(amount, personnel)).
                token(tokenStr).
                build());

        if(fallingMoney == null) {
            log.error("failed to save fallingMoney");
            return Mono.error(DBProcessingException::new);
        }

        // 뿌리기 이력 적재
        FallingMoneyInfo fallingMoneyInfo = fallingMoneyInfoRepository.save(
                FallingMoneyInfo.builder().
                        token(tokenStr).
                        ownerId(userId).
                        roomId(roomId).
                        amount(amount).
                        created(LocalDateTime.now()).
                        build());

        if(fallingMoneyInfo == null) {
            log.error("failed to save fallingMoneyInfo");
            return Mono.error(DBProcessingException::new);
        }

        // 토큰 리턴
        return Mono.just(tokenStr);
    }

    /**
     * 뿌리기 머니 받기 비즈니스 수행
     */
    public Mono<Integer> getFallingMoney(String token, String userId, String roomId) {

        // 뿌리기 머니 토큰 체크 (토큰 유효시간 체크)
        if(!fallingMoneyTokenRepository.existsById(token)) {
            log.error("expired falling money with {}", token);
            return Mono.error(ExpiredFallingMoneyException::new);
        }

        // 잔여 뿌리기 머니 체크
        if(!fallingMoneyRepository.existsById(token)) {
            log.error("falling money is empty with {}", token);
            return Mono.error(FallingMoneyEmptyException::new);
        }

        // 뿌리기 건 정보 확인
        FallingMoneyInfo fallingMoneyInfo = fallingMoneyInfoRepository.findById(token).get();

        // 뿌리기 건의 owner와 Id 일치 여부 체크
        if(fallingMoneyInfo.getOwnerId().equals(userId)) {
            log.error("userId is same as ownerId");
            return Mono.error(InaccessibleException::new);
        }

        // 동일한 방 여부 체크
        if(!fallingMoneyInfo.getRoomId().equals(roomId)) {
            log.error("roomId is not same as");
            return Mono.error(MismatchRoomIdException::new);
        }

        // 뿌리기 이력확인
        FallingMoneyHistory fallingMoneyHistory = null;
        if(fallingMoneyHistoryRepository.existsById(token)) {
            fallingMoneyHistory = fallingMoneyHistoryRepository.findById(token).get();
        }

        if(fallingMoneyHistory != null) {
            // 이미 받은 사용자 리스트에 ID 존재 여부 체크
            if (fallingMoneyHistory.getUsers() != null && fallingMoneyHistory.getUsers().get(userId) != null) {
                log.error("this user ({}) already got money", userId);
                return Mono.error(AlreadyExistFallingMoneyHistoryException::new);
            }
        }

        // 뿌리기 머니 확인
        FallingMoney fallingMoneyList = fallingMoneyRepository.findById(token).get();
        if(fallingMoneyList == null) {
            log.error("falling money is empty with {}", token);
            return Mono.error(FallingMoneyEmptyException::new);
        }

        List<Integer> moneyList = fallingMoneyList.getMoney();
        if(moneyList == null || moneyList.size() == 0) {
            log.error("falling money is empty with {}", token);
            return Mono.error(FallingMoneyEmptyException::new);
        }

        final Integer fallingMoney = moneyList.get(0);
        moneyList.remove(fallingMoney);

        // 잔여 뿌리기 머니 확인
        if(moneyList.size() == 0) {
            fallingMoneyRepository.deleteById(token);
        } else {
            // 잔여 뿌리기 머니 저장
            fallingMoneyRepository.save(FallingMoney.builder().
                    token(token).
                    money(moneyList).
                    build());
        }

        // 신규 받기 이력 추가
        Map<String, Integer> addedUsers = new HashMap<String, Integer>();
        int fallingAmount = 0;

        if(fallingMoneyHistory == null)
            addedUsers = Map.of(userId, fallingMoney);
        else {
            fallingAmount = fallingMoneyHistory.getFallingAmount();
            addedUsers = fallingMoneyHistory.getUsers();

            if (addedUsers == null)
                addedUsers = new HashMap<String, Integer>();

            addedUsers.put(userId, fallingMoney);
        }

        // 뿌리기 이력 저장
        fallingMoneyHistory = fallingMoneyHistoryRepository.save(FallingMoneyHistory.builder().
                token(fallingMoneyInfo.getToken()).
                fallingAmount(fallingAmount+fallingMoney).
                users(addedUsers).
                ownerId(fallingMoneyInfo.getOwnerId()).
                roomId(fallingMoneyInfo.getRoomId()).
                amount(fallingMoneyInfo.getAmount()).
                created(fallingMoneyInfo.getCreated()).
                build());

        if(fallingMoneyHistory == null) {
            log.error("failed to save fallingMoneyInfo");
            return Mono.error(DBProcessingException::new);
        }

        return Mono.just(fallingMoney);
    }

    /**
     * 머니 뿌리기 건에 대한 조회 비즈니스 수행
     */
    public Mono<FallingMoneyHistory> getFallingMoneyHistory(String token, String userId) {
        // 뿌리기 머니 정보 조회 (유효시간 체크)
        if(!fallingMoneyInfoRepository.existsById(token)) {
            log.error("not exist (or expired) falling money info with {}", token);
            return Mono.error(NotExistFallingMoneyInfoException::new);
        }

        FallingMoneyInfo fallingMoneyInfo = fallingMoneyInfoRepository.findById(token).get();
        if(fallingMoneyInfo == null) {
            log.error("not exist (or expired) falling money info with {}", token);
            return Mono.error(NotExistFallingMoneyInfoException::new);
        }

        if(!fallingMoneyInfo.getOwnerId().equals(userId)) {
            log.error("ownerId is not same as {}", userId);
            return Mono.error(InaccessibleException::new);
        }

        // 뿌리기 머니 이력 조회
        if(!fallingMoneyHistoryRepository.existsById(token)) {
            log.error("not exist (or expired) falling money history with {}", token);
            return Mono.error(NotExistFallingMoneyInfoException::new);
        }

        FallingMoneyHistory fallingMoneyHistory = fallingMoneyHistoryRepository.findById(token).get();
        if(fallingMoneyHistory == null) {
            log.error("not exist (or expired) falling money history with {}", token);
            return Mono.error(NotExistFallingMoneyInfoException::new);
        }

        return Mono.just(fallingMoneyHistory);
    }
}