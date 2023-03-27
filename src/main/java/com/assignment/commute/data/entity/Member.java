package com.assignment.commute.data.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                            //  고유id

    @Column(name = "member_id", unique = true)
    private String memberId;                    //  회원id

    @Column(name = "member_pw")
    private String memberPw;                    //  회원비밀번호

    @Column(name = "team")
    private String team;                        // 팀

    @Column(name = "name")
    private String name;                        // 이름

    @Column(name = "role")
    private String role;                        // 권한
//
//    @ManyToOne                          // 밥투표 테이블과 다대일 매핑
//    private DinnerVote dinnerVote;

}
