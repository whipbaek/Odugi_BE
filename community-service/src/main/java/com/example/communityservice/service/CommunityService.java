package com.example.communityservice.service;

import com.example.communityservice.domain.CommunityRole;
import com.example.communityservice.dto.request.*;
import com.example.communityservice.dto.response.CategoryResponseDto;
import com.example.communityservice.dto.response.ChannelResponseDto;
import com.example.communityservice.dto.response.CommunityMemberResponseDto;
import com.example.communityservice.dto.response.CommunityResponseDto;
import com.example.communityservice.entity.*;
import com.example.communityservice.exception.ApiException;
import com.example.communityservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.communityservice.domain.CommunityRole.*;
import static com.example.communityservice.exception.ErrorCode.*;
import static com.example.communityservice.domain.ChannelType.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityService {

    private final FileUploadService fileUploadService;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;

    public Long createCommunity(Long userId, MultipartFile file, String communityName) {

        // file 유무에 따라 다르게

        String filePath = "";

        if (file != null) {
            filePath = fileUploadService.uploadFile(file);
        }

        CommunityRequestDto communityRequestDto = CommunityRequestDto.builder()
                .name(communityName)
                .profileImage(filePath)
                .build();

        Community community = communityRequestDto.toCommunity();
        Community newCommunity = communityRepository.save(community);

        //생성과 동시에 멤버 추가
        addCommunityMember(userId, newCommunity.getId(), ADMIN);

        // 기본 카테고리 생성
        CategoryRequestDto chatCategoryDto = CategoryRequestDto.builder()
                .communityId(newCommunity.getId())
                .role(ADMIN)
                .name("채팅 채널")
                .build();

        CategoryRequestDto voiceCategoryDto = CategoryRequestDto.builder()
                .communityId(newCommunity.getId())
                .role(ADMIN)
                .name("음성 채널")
                .build();


        Long chatCategoryId = createCategory(chatCategoryDto);
        Long voiceCategoryId = createCategory(voiceCategoryDto);

        // 기본 채널 생성

        ChannelRequestDto chatChannelDto = ChannelRequestDto.builder()
                .categoryId(chatCategoryId)
                .name("일반")
                .communityId(newCommunity.getId())
                .role(ADMIN)
                .type(CHAT)
                .build();

        ChannelRequestDto voiceChannelDto = ChannelRequestDto.builder()
                .categoryId(voiceCategoryId)
                .name("일반")
                .communityId(newCommunity.getId())
                .role(ADMIN)
                .type(VOICE)
                .build();

        createChannel(chatChannelDto);
        createChannel(voiceChannelDto);

        return newCommunity.getId();
    }

    public Long addCommunityMember(Long userId, Long communityId, CommunityRole role) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NO_MEMBER_ERROR));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new ApiException(NO_COMMUNITY_ERROR));

        CommunityMember communityMember = CommunityMember.builder()
                .community(community)
                .member(member)
                .role(role)
                .email(member.getEmail())
                .profileImage(member.getProfileImagePath())
                .introduction(member.getIntroduction())
                .name(member.getName())
                .build();

        CommunityMember newCommunityMember = communityMemberRepository.save(communityMember);

        return newCommunityMember.getId();
    }

    public void validateRole(CommunityRole role){
        if(role != ADMIN){
            throw new ApiException(NO_AUTHORITY_ERROR);
        }
    }

    public Long createCategory(CategoryRequestDto categoryRequestDto) {

        // 권한 검사
        validateRole(categoryRequestDto.getRole());

        Community community = communityRepository.findById(categoryRequestDto.getCommunityId())
                .orElseThrow(() -> new ApiException(NO_COMMUNITY_ERROR));

        Category category = Category.builder()
                .community(community)
                .name(categoryRequestDto.getName())
                .build();

        Category newCategory = categoryRepository.save(category);

        return newCategory.getId();
    }

    public Long createChannel(ChannelRequestDto channelRequestDto) {

        validateRole(channelRequestDto.getRole());

        Category category = categoryRepository.findById(channelRequestDto.getCategoryId())
                .orElseThrow(() -> new ApiException(NO_CATEGORY_ERROR));

        Community community = communityRepository.findById(channelRequestDto.getCommunityId())
                .orElseThrow(() -> new ApiException(NO_COMMUNITY_ERROR));

        Channel channel = Channel.builder()
                .category(category)
                .type(channelRequestDto.getType())
                .name(channelRequestDto.getName())
                .community(community)
                .build();

        Channel newChannel = channelRepository.save(channel);

        return newChannel.getId();
    }

    public List<CommunityResponseDto> showCommunity(Long userId) {

        List<CommunityMember> members = communityMemberRepository.findByMember_Id(userId);

        List<CommunityResponseDto> communityResponseDtos = new ArrayList<>();

        for (CommunityMember member : members) {
            Community community = member.getCommunity();
            communityResponseDtos.add(community.toCommunityResponseDto());
        }

        return communityResponseDtos;
    }

    public CommunityMemberResponseDto getCommunityPersonalProfile(Long userId, Long communityId) {
        CommunityMember communityMember = communityMemberRepository.findByCommunity_IdAndMember_Id(communityId, userId)
                .orElseThrow(() -> new ApiException(NO_COMMUNITY_MEMBER_ERROR));

        return communityMember.toCommunityMemberResponseDto();
    }

    public List<CommunityMemberResponseDto> getCommunityMembers(Long communityId) {
        List<CommunityMember> communityMembers = communityMemberRepository.findByCommunity_Id(communityId);
        List<CommunityMemberResponseDto> communityMemberResponseDtoList = new ArrayList<>();

        for (CommunityMember communityMember : communityMembers) {
            communityMemberResponseDtoList.add(communityMember.toCommunityMemberResponseDto());
        }

        return communityMemberResponseDtoList;
    }

    public List<CategoryResponseDto> getCommunityCategory(Long communityId) {
        List<Category> categorys = categoryRepository.findAllByCommunity_Id(communityId);
        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();
        for (Category category : categorys) {
            categoryResponseDtoList.add(category.toCategoryResponseDto());
        }

        return categoryResponseDtoList;
    }

    public List<ChannelResponseDto> getCommunityChannel(Long communityId) {
        List<Channel> channels = channelRepository.findAllByCommunity_Id(communityId);
        List<ChannelResponseDto> channelResponseDtoList = new ArrayList<>();

        for (Channel channel : channels) {
            channelResponseDtoList.add(channel.toChannelResponseDto());
        }

        return channelResponseDtoList;
    }


    @Transactional
    public String deleteCommunity(DeleteRequestDto deleteRequestDto){

        Long communityId = deleteRequestDto.getCommunityId();
        CommunityRole role = deleteRequestDto.getRole();

        validateRole(role);

        // 채널 삭제 -> 카테고리 삭제 -> 채널 멤버서 삭제 -> 커뮤니티 삭제
        channelRepository.deleteByCommunity_Id(communityId);
        categoryRepository.deleteByCommunity_Id(communityId);
        communityMemberRepository.deleteByCommunity_Id(communityId);
        communityRepository.deleteById(communityId);

        return "OK";
    }

    @Transactional
    public String deleteCommunityCategory(DeleteRequestDto deleteRequestDto){
        Long categoryId = deleteRequestDto.getCategoryId();
        CommunityRole role = deleteRequestDto.getRole();
        validateRole(role);

        // 채널의 카테고리 부분 비움 -> 카테고리 삭제

        List<Channel> channels = channelRepository.findAllByCategory_Id(categoryId);
        for (Channel channel : channels) {
            channel.setCategoryEmpty();
        }

        categoryRepository.deleteById(categoryId);

        return "OK";
    }

    @Transactional
    public String deleteCommunityChannel(DeleteRequestDto deleteRequestDto){
        Long channelId = deleteRequestDto.getChannelId();
        CommunityRole role = deleteRequestDto.getRole();
        validateRole(role);

        channelRepository.deleteById(channelId);
        return "OK";
    }

    @Transactional
    public String modifyCommunity(CommunityModifyRequestDto modifyRequestDto) {
        validateRole(modifyRequestDto.getRole());

        Community community = communityRepository.findById(modifyRequestDto.getCommunityId())
                .orElseThrow(() -> new ApiException(NO_COMMUNITY_ERROR));

        community.modifyName(modifyRequestDto.getName());

        return "OK";
    }

    @Transactional
    public String modifyCommunityCategory(CommunityModifyRequestDto modifyRequestDto) {
        validateRole(modifyRequestDto.getRole());

        Category category = categoryRepository.findById(modifyRequestDto.getCategoryId())
                .orElseThrow(() -> new ApiException(NO_CATEGORY_ERROR));
        category.modifyName(modifyRequestDto.getName());

        return "OK";
    }

    @Transactional
    public String modifyCommunityChannel(CommunityModifyRequestDto modifyRequestDto) {
        validateRole(modifyRequestDto.getRole());

        Channel channel = channelRepository.findById(modifyRequestDto.getChannelId())
                .orElseThrow(() -> new ApiException(NO_CHANNEL_ERROR));

        channel.modifyName(modifyRequestDto.getName());

        return "OK";
    }


}
