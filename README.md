# Travel Planner - Backend

여행 경로 플래너의 백엔드 서버입니다.  
Spring Boot 기반으로 구축되었으며, 사용자 인증, 일정 및 출발지 저장, 경로 계산 등의 기능을 제공합니다.

## 주요 기능
- OAuth2 기반 소셜 로그인 (Google)
- JWT 인증 및 사용자 정보 관리
- 출발지 주소 및 좌표 저장/복원
- 일정 장소 저장, 삭제, 정렬 및 최적화
- Kakao 좌표 변환 API 연동
- 한국관광공사 API 연동
- Swagger를 통한 API 문서 제공

## 사용 기술
- Spring Boot 3, Spring Security, OAuth2
- JWT, Spring Data JPA
- Oracle Database
- Swagger OpenAPI
