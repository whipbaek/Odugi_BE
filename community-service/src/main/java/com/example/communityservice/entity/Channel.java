package com.example.communityservice.entity;

import com.example.communityservice.domain.ChannelType;
import com.example.communityservice.dto.response.ChannelResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.util.annotation.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    // 음성 or 채팅
    @Column(columnDefinition = "TINYINT", length = 1)
    private ChannelType type;


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


    public ChannelResponseDto toChannelResponseDto(){

        if(this.category == null ){
            return ChannelResponseDto.builder()
                    .id(this.id)
                    .categoryId(null)
                    .type(this.type)
                    .name(this.name)
                    .build();
        }

        return ChannelResponseDto.builder()
                .id(this.id)
                .categoryId(this.category.getId())
                .type(this.type)
                .name(this.name)
                .build();
    }

    public void modifyName(String name){
        this.name = name;
    }

    public void setCategoryEmpty(){
        this.category = null;
    }
}
