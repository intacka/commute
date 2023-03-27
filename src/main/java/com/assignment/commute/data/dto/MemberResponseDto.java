package com.assignment.commute.data.dto;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MemberResponseDto {

    private Long id;
    private String memberId;                    //  회원id
    private String memberPw;                    //  회원비밀번호
    private String team;                        // 팀
    private String name;                        // 이름
    private String role;                        // 권한
    private LocalDateTime createdAt;            // 생성날짜
    private LocalDateTime updatedAt;            // 업데이트날짜

}
