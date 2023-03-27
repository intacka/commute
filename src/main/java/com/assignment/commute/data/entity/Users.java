package com.assignment.commute.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iduser;                            //  고유id

    @Column(name = "username", unique = true)
    private String username;                    //  회원id

    @Column(name = "password")
    private String password;                    //  회원비밀번호

    @Column(name = "role")
    private String role;                        // 권한

    @Column(name = "etc")
    private String etc;                        // 권한







}
