package com.AShop.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Notice extends BaseEntity{


    @Id
    @GeneratedValue
    @Column(name = "idx")
    private int idx;

    @Column(name = "nTitle")
    private String nTitle;

    @ManyToOne
    @JoinColumn(name = "member_id")
    public Member member;

    @Column(name = "content")
    private String content;



}

