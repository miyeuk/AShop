package com.AShop.config;

import com.AShop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
/*WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnalbleWebSecurity를 선언하면
* SpringSecurityFilterChain이 자동으로 포함된다 .
* WebSecurityConfigurerAdapter를 상속받아서 메소드 오버라이딩을 통한 보안 설정을 커스터 마이징 할 수 있다.*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        /*http요청에 대한 보안을 설정한다. 페이지 권한 설정 , 로그인 페이지 설정,로그아웃 메소드 등에 대한 설정을 작성.*/

        http.formLogin()
                .loginPage("/members/login")//로그인 페이지 URL을 설정
                .defaultSuccessUrl("/")//로그인 성공 시 이동할 URl 설정
                .usernameParameter("email")// 로그인 시 사용할 파라미터 이름으로 email 지정
                .failureUrl("/members/login/error")//로그인 실패시 이동할 URL을 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher
                        ("/members/logout"))//로그아웃 URL을 설정
                .logoutSuccessUrl("/")//로그아웃 성공시 이동할 URL 설정
        ;
        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/","/members/**", "/item/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
        //시큐리티 처리에  HttpServletRequest를 이용한다는 것을 의미
        //PermitAll을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근 할 수 있도록 설정
        //메인 페이지, 회원 관련 URL, 뒤에서 만들 상품상세 페이지,상품 이미지를 불러오는 경로가 이에 해당.
        // /admin으로 시작하는 경로는 해당 계정이 ADMIN Role 일 경우에만 접근 가능하도록 설정
        //위에 설정경로를 제외한 나머지 경로들은 모두 인증을 요구함


        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        //인증되지 않은 사용자가 리로스에 접근하였을 때 수행되는 핸들러를 등록
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /*비빌번호를 DB에 그대로 저장 했을 경우, DB가 해킹 당하면 고객의 회원 정보가 그대로 노출될 수 있어 이를 해결하기 위해
    * BCryptPasswordEncoder를 빈으로 등록하여 사용*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws  Exception{
        /*Spring security에서 인증은 AuthenticationManagerBuilder에서 이루어 지며
        * AuthenticationManagerBuilder가 AuthenticationManager를 생성*/
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
        /*userDetailsService 를 구현하고 있는 객체로 memberService를 지정 해주며 비밀번호 암호를 위해 passwordEncoder를 지정 */
    }


    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/***", "/js/**", "/img/**");

        //static 디렉토리의 하위 파일은 인증을 무시하도록 설정
    }

}
