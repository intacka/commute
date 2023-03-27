package com.assignment.commute.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "member_calendar")
public class MemberCalendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   //  고유id

    private String title; // 이름
    private String start; // 날짜
    @ManyToOne                          // 회원 테이블과 다대일 매핑
    @JoinColumn(name = "member_id")     // 외래키이름
    private Member member;

}
