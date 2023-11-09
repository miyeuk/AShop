# AShop

Three Null Shop을 위한 Spring Boot JPA 및 Spring Security 프로젝트입니다.

## 개요

이 프로젝트는 Three Null Shop을 위해 Spring Data JPA 및 Spring Security를 통합한 Spring Boot 애플리케이션입니다. Spring Boot 스타터, Thymeleaf, QueryDSL, H2 데이터베이스 (테스트용), MySQL 커넥터, Lombok 등의 종속성이 포함되어 있습니다.

## 전제 조건

- Java 11
- MySQL 데이터베이스

## 프로젝트 구조

- **GroupId:** com.AShop
- **ArtifactId:** Ashop
- **Version:** 0.0.1-SNAPSHOT
- **Description:** Three Null Shop을 위한 Spring Boot JPA 및 Spring Security

## 종속성

- Spring Boot Data JPA, Thymeleaf, Web, Security 스타터
- 런타임 개발 지원을 위한 DevTools
- Spring Boot 및 Spring Security 테스트 종속성
- 데이터 영속성을 위한 H2 데이터베이스 및 MySQL 커넥터
- 간편한 Java 개발을 위한 Lombok
- 타입 안전한 쿼리를 위한 QueryDSL
- 향상된 Thymeleaf 템플릿을 위한 Thymeleaf 레이아웃 다이얼렉트
- 데이터 유효성 검사 및 매핑을 위한 Validation 및 ModelMapper

## 빌드 설정

프로젝트는 빌드 도구로 Maven을 사용합니다. 주요 플러그인은 다음과 같습니다.

- `spring-boot-maven-plugin`: 애플리케이션을 패키징하기 위한 플러그인
- `apt-maven-plugin`: QueryDSL 어노테이션 처리를 위한 플러그인

## 빌드 및 실행

1. 저장소 복제:

   ```bash
   git clone https://github.com/your-username/Ashop.git
