package com.AShop.service;

import com.AShop.dto.MemberFormDto;
import com.AShop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
/*테스트 클레스에 @Transactional 을 선언할 경우, 테스트 실행 후 롤백 처리가 됨 ,이를 통해 같은 메소드를 반복적으로 테스트를 할 수 있다.*/
@TestPropertySource(locations = "classpath:application.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        /* 회원 정보를 입력한  Member 엔티티를 만드는 메소드를 작성*/
        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("김민혁");
        memberFormDto.setAddress("서울시 강서구 화곡동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    /*Junit 의 Assertions 클래스의 assertEquals메소드를 이용하여 저장하려고 요청했던 값과 실제 저장된 데이터를 비교
    * 첫번째 파라미터에는 기대 값 , 두 번째 파라미터에는 실제로 저장된 값을 넣어준다.*/
    public void  saveMemberTest(){
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        assertEquals(member.getEmail() , saveMember.getEmail());
        assertEquals(member.getName() , saveMember.getName());
        assertEquals(member.getAddress() , saveMember.getAddress());
        assertEquals(member.getPassword(), saveMember.getPassword());
        assertEquals(member.getRole(), saveMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2= createMember();
        memberService.saveMember(member1);


        Throwable e =assertThrows(IllegalStateException.class, ()->{
            memberService.saveMember(member2); });
        /*Junit의  assertThrows 메소드를 이용하면 예외처리 테슽트 가능 첫번째 파라미터에는 발생할 예외 타입을 넣는다*/

        assertEquals("이미 가입된 회원 입니다.", e.getMessage());
        /*발생한 예외 메세지가 예상 결과와 맞는지 검증한다.*/
    }
}