package com.example.communityservice.repository;

import com.example.communityservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByCommunity_Id(Long id);

    @Modifying
    @Override
    void deleteById(Long categoryId);

    @Modifying
    void deleteByCommunity_Id(Long communityId);




}
