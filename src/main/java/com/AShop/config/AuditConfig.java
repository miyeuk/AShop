package com.AShop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;



// Auditing 기능을 사용하기 위해 Config 파일을 생성
@Configuration
@EnableJpaAuditing //jPA의 Auditing 기능을 활성화 함
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){//등록자와 수정자를 처리해주는 AuditorAware을 빈으로 등록
        return new AuditorAwareImpl();
    }
}
