<img width="547" alt="스크린샷 2025-01-13 오전 10 23 14" src="https://github.com/user-attachments/assets/caeba053-3c8d-4074-bb24-171363ada084" /># 🚚 **아웃소싱 배달앱 프로젝트**
- `Spring Boot`를 활용한 '배달앱' 협업 프로젝트 입니다.
- 개발기간: 25.01.07 ~ 25.01.13


✨ **주요 기능**
- 고객(CUSTOMER) / 사장님(OWNER) 으로 **회원가입**, **로그인**
- 사장님 권한으로 **가게**/**메뉴** 관리 기능
- 고객이 메뉴 **주문**을 하면 사장님이 수락을 하고 `조리중 → 배달중 → 배달 완료` 순으로 배달 상태 변경
- 배달 완료된 주문건에 대해서 **별점**(1~5점)으로 리뷰 가능


## 👷 **Team**

| 팀장  | 팀원  | 팀원  | 팀원  | 팀원  |
|:---:|:---:|:---:|:---:|:---:|
| 임수민 | 조민재 | 신가을 | 김신우 | 이정우 | 


## 🔧 **기술 스택**

- `backend`
    - `Java`, `SpringBoot`, `gradle`, `Spring Data JPA`,  `MySQL`
- `Tool`
    - `IntelliJ`, `Github`, `Notion`, `Slack`


## 🎨 **와이어 프레임**


![wireframe](https://www.figma.com/design/yTN2yv0KwTZJHCOnrf7z3A/1%EC%A1%B0?node-id=0-1&p=f&t=scGVg0T2i6E56PJV-0)


## 📝 **ERD**

```mermaid
---
title:Delivery
---
erDiagram
%% 양방향 관계 (실선 양쪽 화살표)
    Store ||--o{ Menu : "has (양방향)"
    Store ||--o{ Review : "has (양방향)"
    Store ||--o{ Order : "has (양방향)"
    %% 단방향 관계 (점선 단방향 화살표)
    User ||--o{ Store : "단방향"
    User ||--o{ Order : "단방향"
    User ||--o{ Review : "단방향"
    Menu ||--o{ Order : "단방향"
    Order ||--o{ OrderLog : "단방향"
    Order ||--o{ Review : "단방향"
    User {
        Long userId PK
        String nickname UK "닉네임"
        String email UK "이메일"
        String password "암호화된 비밀번호"
        String role "CUSTOMER/OWNER"
        boolean isDeleted "소프트 딜리트"
    }
    Store {
        Long storeId PK
        Long userId FK "User 참조"
        String storeName "가게명"
        String tel "전화번호"
        String address "주소"
        LocalTime openTime "오픈 시간"
        LocalTime closeTime "마감 시간"
        int minOrderAmount "최소 주문 금액"
        boolean isDeleted "폐업 여부, 소프트 딜리트"
    }
    Menu {
        Long MenuId PK
        Long storeId FK "Store 참조"
        String menuName "메뉴명"
        int price "가격"
        boolean isDeleted "소프트 딜리트"
    }
    Order {
        Long orderId PK
        Long userId FK "User 참조"
        Long storeId FK "Store 참조"
        Long menuId FK "Menu 참조"
        String orderStatus "주문상태"
        int cart "수량"
        int totalAmount "총 금액"
        LocalDateTime orderTime "주문 시간"
    }
    Review {
        Long reviewId PK
        Long orderId FK "Order 참조"
        Long userId FK "User 참조"
        Long storeId FK "Store 참조"
        int rating "별점(1-5)"
        String content "리뷰 내용"
        LocalDateTime createdAt "리뷰 작성일"
    }
    OrderLog {
        Long orderLogId PK
        Long orderId FK "Order 참조"
        Long storeId FK "Store 참조"
        String orderLogStatus "주문상태"
        LocalDateTime loggedAt "로그 시각"
    }
```

## 📄 **API 명세서**
