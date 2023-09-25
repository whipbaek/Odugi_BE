package com.example.communityservice.repository;

import com.example.communityservice.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findAllByCommunity_Id(Long id);

    @Modifying
    @Override
    void deleteById(Long channelId);
    @Modifying
    void deleteByCategory_Id(Long categoryId);
    @Modifying
    void deleteByCommunity_Id(Long communityId);

    @Modifying
    List<Channel> findAllByCategory_Id(Long categoryId);




}
