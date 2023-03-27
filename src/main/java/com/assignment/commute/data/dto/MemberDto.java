package com.assignment.commute.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String memberId;                    //  회원id
    private String memberPw;                    //  회원비밀번호
    private String team;                        // 팀
    private String name;                        // 이름

    private String role;                        // 권한
    private Long id;
}
