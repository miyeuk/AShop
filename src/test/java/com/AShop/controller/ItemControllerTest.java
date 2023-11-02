package com.AShop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

import static  org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin" , roles = "ADMIN")//현재회원의 이름이 admin이고 role이 ADMIN인 유저가 로그인 된 상태로 테스트를 할 수 있도록 하는 어노테이션
    public void itemFormTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isOk());
        //상품등록 페이지에 요청을 get요청 보냄
        //요청과 응답 메세지를 확인 할 수 있도록 콘솔창에 출력
        //응답 상태 코드가 정상인지 확인
    }

    @Test
    @DisplayName("상품등록 페이지 일반 회원 접근 테스트")
    @WithMockUser(username = "user" , roles = "USER")//현재 인증 사용자의 Role을 User로 세팅
    public void  itemFormNotAdminTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden());
        //상품등록 페이지 진입 요청시 Forbidden 예외가 발생하면 테스트가 성공적으로 통과
    }
}