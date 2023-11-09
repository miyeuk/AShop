package com.AShop.controller;

import com.AShop.dto.MemberFormDto;
import com.AShop.entity.Member;
import com.AShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
}