package com.assignment.commute.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "rest")
public class Rest extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                            //  고유id

    @Column(name = "rest_start_time")
    private LocalTime restStartTime;                    //  휴식시작시간

    @Column(name = "rest_stop_time")
    private LocalTime restStopTime;                    //  휴식종료시간

    @Column(name = "rest_reason")
    private String restReason;                    //  휴식조건

    @Column(name = "rest_date")
    private LocalDate restDate;                       // 휴식날짜

    @ManyToOne                              // Commute 테이블과 다대일 매핑
    @JoinColumn(name = "member_id")        // 외래키이름
    private Member member;

}
