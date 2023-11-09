package com.AShop.controller;

import com.AShop.dto.MemberFormDto;
import com.AShop.entity.Member;
import com.AShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public  String memberForm(Model model){
        /*회원 가입 페이지로 이동할 수 잇도록 MemberController  클레스에 메소드 작성*/
        model.addAttribute("memberFormDto" , new MemberFormDto());
        return "member/memberForm";
    }


    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto,
                            BindingResult bindingResult, Model model){
        /*검증하려는 객체 앞에 @Valid 선언 후, 파라미터로 bindingResult 객체를 추가.
        * 검사 후 결과는 bindingResult에 담아 줌 . bindingResult.hasError()를 호출하여  에러가 있다면 회원 가입 페이지로 이동*/
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member =Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            /*회원가입 시 중복 회원 가입 예외가 발생하면 에러메세지를 뷰로 전달*/
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){

        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요");
        return "member/memberLoginForm";
    }

    @GetMapping("/memberModify")
    public String showMemberModifyForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // 현재 로그인한 사용자의 이메일을 가져옵니다.
        Member member = memberService.getMemberByEamil(email);

        // 회원 정보를 모델에 추가
        model.addAttribute("member", member);

        return "member/memberModify"; // 사용자에게 보여줄 뷰 페이지의 이름을 리턴합니다.
    }

    @PostMapping("/memberModify")
    public String processMemberModify(@ModelAttribute("member") MemberFormDto memberFormDto, Model model) {
        // 유효성 검사를 생략하고 진행합니다.

        try {
            // 현재 로그인한 사용자의 이메일을 가져옵니다.
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            // MemberService를 활용하여 회원 정보를 수정합니다.
            memberService.modifyMemberByEmail(email, memberFormDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberModify";
        }

        return "redirect:/"; // 수정 완료 후 리다이렉트할 URL을 지정합니다.
    }
}