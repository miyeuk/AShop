package com.AShop.service;

import com.AShop.dto.MemberFormDto;
import com.AShop.entity.Member;
import com.AShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
/*비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 선언 로직을 처리하다가 에러 발생시 , 변경된 데이터를 로직 수행 이전의 상태로 콜백
* 빈을 주입하는 방법으로 @AutoWired를 이용하거나 필드 주입(Setter 주입) 생성자 주입을 이용하는 방법이 있다.
* @RequiredArgConstructor 어노테이션은  final이나 @NonNull이 붙은 필드에 생성자를 생성해줌
* 빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하다면 @AutoWired없이 의존성 주입이 가능*/
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        /*이미 가입된 회원일 경우  IllegalStateException   예외 발생*/
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원 입니다.");
        }
    }

    public Member getMemberByEamil(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public Member modifyMemberByEmail(String email, MemberFormDto memberFormDto) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        // 회원 정보를 업데이트합니다.
        member.setName(memberFormDto.getName());
        member.setZipcode(memberFormDto.getZipcode());
        member.setAddress(memberFormDto.getAddress());
        member.setDetailAddr(memberFormDto.getDetailAddr());
        member.setRole(memberFormDto.getRole());
        if (memberFormDto.getPassword() != null) {
            // 비밀번호가 변경되었을 경우, 새로운 비밀번호로 업데이트합니다.
            String newPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(memberFormDto.getPassword());
            member.setPassword(newPassword);
        }

        return memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
        /*UserDetailsService 인터페이스의 loadUserByname()메소드를 오버라이딩
        * 로그인할 유저의 email을 파라미터로 전달 받는다.*/
        Member member = memberRepository.findByEmail(email);
        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                /*UserDetail 을 구현하고 있는 User 객체를 반환해준다.
                * User객체를 생서하기 위해서 생성자로 회원의 이메일 비밀번호, role을 파라미터로 넘겨준다.*/
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
