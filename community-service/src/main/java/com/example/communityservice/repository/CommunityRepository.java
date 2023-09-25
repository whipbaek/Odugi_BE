package com.example.communityservice.repository;

import com.example.communityservice.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Modifying
    @Override
    void deleteById(Long id);
}
