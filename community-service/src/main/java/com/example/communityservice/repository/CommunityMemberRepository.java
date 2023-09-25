package com.example.communityservice.repository;

import com.example.communityservice.entity.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findByMember_Id(Long id);

    Optional<CommunityMember> findByCommunity_IdAndMember_Id(Long communityId, Long userId);

    List<CommunityMember> findByCommunity_Id(Long id);

    void deleteByCommunity_Id(Long communityId);



}
