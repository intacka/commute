package com.assignment.commute.data.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@DynamicInsert
@Setter
@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "dinner_vote")
public class DinnerVote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dinner_vote_id")
    private Integer dinnerVoteId;                   //  고유id

    @Column(name = "dinner_date", nullable = false)
    private LocalDate dinnerDate;                //  날짜

    @Column(name = "menu_time", nullable = false)
    private String menuTime;                     // 메뉴,시간

    @Column(name = "members")
    private String members;

    @Column(name = "cnt_members")
    @ColumnDefault("0")
    private Integer cntMembers;

    @Builder
    public DinnerVote(LocalDate dinnerDate, String menuTime, String members) {
        this.dinnerDate = dinnerDate;
        this.menuTime = menuTime;
        this.members = members;
    }

    public DinnerVote() {

    }

//    @Builder
//    public Book(String bookName, String author, Integer price) {
//        this.bookName = bookName;
//        this.author = author;
//        this.price = price;
//    }


}
