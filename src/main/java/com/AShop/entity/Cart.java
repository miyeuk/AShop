package com.AShop.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity{

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)//회원 엔티티와 일대일로 매핑
    @JoinColumn(name="member_id")
    //매핑할 외래키 지정 name 속성에는 매핑할 외래키의 이름을 설정
    //JoinColumn의 name을 명시하지 않으면 JPA가 알아서 ID를 찾지만
    // 컬럼명이 원하는 대로 생성 되지 않을 수 있기에 직접 지명
    private Member member;


    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }




}
