package com.AShop.entity;

import com.AShop.dto.MemberFormDto;
import com.AShop.repository.CartRepository;
import com.AShop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class CartTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager em;

    public Member creatMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("김민혁");
        memberFormDto.setAddress("서울시 금천구 독산동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회")
    public void findCartAndMemberTest(){
        Member member = creatMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();
        //JPA는 영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날때 flush를 호출하여 DB에 반영
        //회원 엔티티와 장바구니 엔티티를 영속성 컨텍스트에 저장 후 엔티티 매니저로부터 강제로
        // flush를 호출하여 DB에 반영
        em.clear();
        //JPA는 영속성 컨텍스트로부터 엔티티를 조회 후 컨텍스트에 엔티티가 없을 경우 dB조회
        //실제 데이터베이스에서 장바구니 엔티티를 가지고 올때 회원 엔티티도 같이 가지고 오는지 보기 위해
        //영속성 컨텍스트를 비움

        Cart savedCart = cartRepository.findById(cart.getId())//처음에 저장된 장바구니 엔티티 조회
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId(), member.getId());
        //처음에 저장한 member 엔티티의 id와 savedCart에 매핑된 member엔티티의 id를 비교
    }
}