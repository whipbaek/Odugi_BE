package devcamp.ottogi.userservice.service;

import devcamp.ottogi.userservice.dto.response.FriendResponseDto;
import devcamp.ottogi.userservice.dto.response.MemberResponseDto;
import devcamp.ottogi.userservice.entity.Friend;
import devcamp.ottogi.userservice.entity.Member;
import devcamp.ottogi.userservice.response.ApiException;
import devcamp.ottogi.userservice.repository.FriendRepository;
import devcamp.ottogi.userservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static devcamp.ottogi.userservice.domain.FriendState.*;
import static devcamp.ottogi.userservice.response.ErrorCode.*;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto findMemberInfoById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));
    }

    public MemberResponseDto findMemberInfoByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));
    }

    public String addFriend(Long userId, String email) {

        Member sender = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));


        Member receiver = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));

        log.info("친구 요청 [IN] userId : {} , To : {}", sender.getEmail(), receiver.getEmail());


        if (friendRepository.existFriendRow(sender.getId(), receiver.getId()).isPresent()) {
            throw new ApiException(DUPLICATED_FRIEND);
        }
        String channelId = sender.getName() + receiver.getName();

        friendRepository.save(new Friend(sender, receiver, SEND, channelId));

        log.info("친구 요청 [DONE] userId {} , To : {}", userId, email);

        return "OK";
    }

    public List<FriendResponseDto> showFriend(Long userId) {

        log.info("친구 조회 [IN] userId : {}", userId);

        List<Friend> friendList = friendRepository.findFriends(userId)
                .orElseThrow(() -> new ApiException(NO_SHOW_FRIENDS));

        List<FriendResponseDto> friendResponseDtoList = new ArrayList<>();

        for (Friend friend : friendList) {

            // 내가 sender 라면, receiver 를 불러 와야함.

            // (1) sender 인지 receiver 인지 구분
            // (2) 친구 상태인지 아닌지 구분

            if(friend.getSender().getId().equals(userId)){

                if(friend.getState() == ACCEPTED){
                    friendResponseDtoList.add(new FriendResponseDto().builder()
                            .name(friend.getReceiver().getName())
                            .userId(friend.getReceiver().getId())
                            .email(friend.getReceiver().getEmail())
                            .profileImagePath(friend.getReceiver().getProfileImagePath())
                            .friendState(ACCEPTED)
                            .channelId(friend.getChannelId())
                            .createdAt(friend.getCreatedAt())
                            .build());
                } else{
                    friendResponseDtoList.add(new FriendResponseDto().builder()
                            .name(friend.getReceiver().getName())
                            .userId(friend.getReceiver().getId())
                            .email(friend.getReceiver().getEmail())
                            .profileImagePath(friend.getReceiver().getProfileImagePath())
                            .friendState(REQUEST)
                            .channelId(friend.getChannelId())
                            .createdAt(friend.getCreatedAt())
                            .build());
                }

            }
            // 내가 receiver 라면 , sender 를 불러 와야 함.
            else {
                if(friend.getState() == ACCEPTED){
                    friendResponseDtoList.add(new FriendResponseDto().builder()
                            .name(friend.getSender().getName())
                            .userId(friend.getSender().getId())
                            .email(friend.getSender().getEmail())
                            .profileImagePath(friend.getSender().getProfileImagePath())
                            .friendState(ACCEPTED)
                            .channelId(friend.getChannelId())
                            .createdAt(friend.getCreatedAt())
                            .build());
                } else{
                    friendResponseDtoList.add(new FriendResponseDto().builder()
                            .name(friend.getSender().getName())
                            .userId(friend.getSender().getId())
                            .email(friend.getSender().getEmail())
                            .profileImagePath(friend.getSender().getProfileImagePath())
                            .friendState(WAIT)
                            .channelId(friend.getChannelId())
                            .createdAt(friend.getCreatedAt())
                            .build());
                }
            }
        }

        log.info("친구 조회 [DONE] userId : {}", userId);

        return friendResponseDtoList;
    }

    public String acceptFriend(Long userId, String email) {

         //accept 를 하는 경우는 내가 receiver 일때만 이다.
        // sender & receiver 로 찾아낸다.

        log.info("친구 승인 [IN] userId : {} , To : {}", userId, email);

        Member sender = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));

        Friend friendRow = friendRepository.findFriendRow(sender.getId(), userId)
                .orElseThrow(() -> new ApiException(NO_FRIEND_REQUEST));

        friendRow.modifyState(ACCEPTED);

        friendRow.modifyCreatedAt();

        log.info("친구 승인 [DONE] userId : {} , To : {}", userId, email);

        return "OK";
    }

    public String rejectFriend(Long userId, String email) {

        log.info("친구 거절 [IN] userId : {} , To : {}", userId, email);

        Member sender = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));

        friendRepository.deleteFriendRow(sender.getId(), userId);

        log.info("친구 거절 [DONE] userId : {} , To : {}", userId, email);

        return "OK";
    }

    public String userDelete(Long userId) {
        memberRepository.deleteMemberById(userId);
        return "OK";
    }

    public String userNameModify(Long userId, String newName) {

        log.info("이름 변경 [IN] userId : {}, newName : {}", userId, newName);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));
        member.modifyName(newName);

        log.info("이름 변경 [DONE] userId : {}, newName : {}", userId, newName);

        return "OK";
    }

    public String userPasswordModify(Long userId, String newPassword) {
        log.info("비밀번호 변경 [IN] userId : {}, newPassword : {}", userId, newPassword);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));
        member.modifyPassword(passwordEncoder.encode(newPassword));

        log.info("비밀번호 변경 [DONE] userId : {}, newPassword : {}", userId, newPassword);
        return "OK";
    }


    public String userIntroModify(Long userId, String introduction) {
        log.info("자기소개 변경 [IN] userId : {}, introduction : {}", userId, introduction);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));

        member.modifyIntroduction(introduction);
        log.info("자기소개 변경 [DONE] userId : {}, introduction : {}", userId, introduction);

        return "OK";
    }
}
