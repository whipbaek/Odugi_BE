package com.example.communityservice.entity;

import com.example.communityservice.domain.CommunityRole;
import com.example.communityservice.dto.response.CommunityMemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommunityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "communitymember_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @Column(columnDefinition = "TINYINT", length = 1)
    private CommunityRole role;

    @Email
    private String email;

    private String profileImage;

    private String introduction;

    @Column(length = 30)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void createdAt(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    @PreUpdate
    public void updatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public CommunityMemberResponseDto toCommunityMemberResponseDto(){
        return CommunityMemberResponseDto.builder()
                .name(this.getName())
                .email(this.getEmail())
                .role(this.getRole())
                .introduction(this.getIntroduction())
                .profileImage(this.getProfileImage())
                .joinedAt(this.getCreatedAt())
                .createdAt(this.getMember().getCreatedAt())
                .build();
    }
}
