package com.assignment.commute.data.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "commute")
public class Commute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    //  고유id

    @Column(name = "date")              // 날짜 LocalDate
    private LocalDate commuteDate;

    @Column(name = "start_time")        // 출근시간 LocalTime
    private LocalTime startTime;

    @Column(name = "leave_time")        // 퇴근시간 LocalTime
    private LocalTime leaveTime;

    @Column(name = "last_rest_time")        // 최종휴식시간
    private LocalTime lastRestTime;

    @Column(name = "working_time")      // 근무시간 : 퇴근시간-출근시간
    private LocalTime workingTime;

    @Column(name = "tardy_time")        // 지각시간 : 현재시간이 10:00 이후일경우, 현재시간-10:00
    private LocalTime tardyTime;

    @Column(name = "tardy_reason")      // 지각사유
    private String tardyReason;

    @Column(name = "leave_note")        // 퇴근비고
    private String leaveNote;

//    @Column(columnDefinition = "Time default 00:00:00")
//    private LocalTime restStartTime;    // 휴식버튼 눌렀던시간

    @ManyToOne                          // 회원 테이블과 다대일 매핑
    @JoinColumn(name = "member_id")     // 외래키이름
    private Member member;

}
