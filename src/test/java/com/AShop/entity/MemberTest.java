package com.AShop.entity;

import com.AShop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Audtiting 테스트")
    @WithMockUser(username = "minhyeuk" , roles = "User")
    //스프링시큐리티에서 제공하는 어노테이션으로 지정한 사용자가 로그인 한 상태라고 가정하고 테스트 진행
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("등록 시간 :" + member.getRegTime());
        System.out.println("수정 시간 :" + member.getUpdateTime());
        System.out.println("등록한 사람 :" + member.getCreatedBy());
        System.out.println("수정한 사람 :" + member.getModifiedBy());
        //등록 시간 :2023-10-31T17:34:09.490041
        //수정 시간 :2023-10-31T17:34:09.490041
        //등록한 사람 :minhyeuk
        //수정한 사람 :minhyeuk
    }


}