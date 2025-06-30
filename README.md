<div id="top"></div>

<div align='center'>

<h1><b>DevQuest (ver.2) - 게이미피케이션 기반 TODO 서비스</b></h1>
<h3><b>프로그래머스 데브코스 5기 6회차 Team 04 - 3차 프로젝트</b></h3>

<img src="DevQuest-service/src/main/resources/static/img/DevQuestLogo.png" alt="DevQuest 로고 이미지" width="300"/>

</div>

<br>

## 0. 목차

1. [프로젝트 소개](#1)
2. [팀원 소개](#2)
3. [개발 일정](#3)
4. [기술 스택](#4)
5. [라이브러리 사용 이유](#5)
6. [컨벤션](#6)
7. [브랜치 및 디렉토리 구조](#7)
8. [주요 기능 소개](#8)
9. [상세 담당 업무](#9)
10. [시작 가이드](#10)

<br />

## <span id="1">🚩 1. 프로젝트 소개</span>

**서비스명**: DevQuest (ver.2) 
**설명**: 기존 DevQuest 버전에 아래의 기능들을 추가하여 업데이트하였습니다.

### 추가된 기능
| 대분류           | 세부 항목           | 설명                                                                |
| ------------- | --------------- | ----------------------------------------------------------------- |
| **로그인 및 서버**  | JWT 인증          | 토큰 기반 인증으로 변경                                                     |
|               | 이메일 서버          | 이메일 전송 기능을 별도의 Kotlin 서버로 구현                                      |
| **일정 관리 캘린더** | FullCalendar 적용 | JavaScript 라이브러리 사용 ([fullcalendar.io](https://fullcalendar.io/)) |
|               | 코딩테스트 연동        | 크롤링을 통한 문제 링크 연동으로 TODO 템플릿 대체                                       |
|               | 목표 enum화        | 목표(goal) 커스텀 제한 → enum 값(코딩테스트, 면접 준비, CS)만 사용                    |
| **LLM**       | AI 챗봇           | 상호 채팅 기능 (모달 형태)                                                  |
|               | AI 성격 설정        | 첫 대화 또는 설정에서 AI 성격 선택 기능 (사용자 화면에서)                               |
| **관리자 기능**    | 대시보드            | 차트 요약 카드, 유저 랭킹 레벨 Top3, 회원 관심사(언어) Top5, 가입/탈퇴일, 인기 목표기업 차트 구현   |
|               | 기업 색상 관리        | DB에 저장된 기업 색상 지정 가능, 사용자 대시보드 목표기업 색상 반영                          |
|               | 회원 관리           | 회원, 관리자, 탈퇴회원 테이블 조회 (기존 기능)                                      |
|               | 기업 유사어 관리       | 기업 유사어(별칭) 지정 → 입력 시 정규화 후 DB 저장                                  |
|               | 업적 관리           | 경험치 기반 업적 추가/수정/삭제 기능 (별도 구현 로직 불필요)                              |

<br>

## <span id="2">🏃 2. 팀원 소개</span>

| 이름       | 역할                                        | GitHub                                             |
| ---------- | ------------------------------------------- | -------------------------------------------------- |
| 채철민     | 팀장 / 마이페이지 / 업적 시스템             | [@moooonbong](https://github.com/moooonbong)       |
| 임서현     | 인덱스 / 회원가입 / 로그인 / 관리자 페이지 | [@nunLSH](https://github.com/nunLSH)              |
| 노선우     | AI 멘탈 관리 기능                           | [@noopy2020](https://github.com/shtjsdn2000)      |
| 김나단     | 목표 기업 TODO / 목표 상세 페이지            | [@nathan960307](https://github.com/nathan960307)  |
| 이초롱     | 대시보드 / 관심 분야 / 알림 정렬 및 필터     | [@0802222](https://github.com/0802222)            |

<br>

## <span id="3">📅 3. 프로젝트 일정</span>

> 개발 기간: 2025.06.11 ~ 2025.06.18 (7일)

<img src="/src/main/resources/static/img/WBS.jpg" alt="DevQuest 일정 구성도" width="600"/>

<br>

## <span id="4">📚 4. 기술 스택</span>

- **Backend**: Java, Kotlin, Spring Boot, Spring Data JPA, Spring Security, JWT, JavaMailSender, QueryDSL, ModelMapper, Coroutine
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Database**: MySQL
- **Version Control**: Git, GitHub

> 협업 툴: Notion, Figma, Zoom, Slack, Zep, Google Sheet

<br>

## <span id="5">❓ 5. 라이브러리 사용 이유</span>

| 라이브러리            | 목적 및 이유                                  |
| ---------------- | ---------------------------------------- |
| Spring Boot      | 빠른 설정과 의존성 관리로 개발 생산성 향상                 |
| Spring Security  | 인증 및 인가 처리, JWT와 연동한 보안 체계 구축            |
| JWT (jjwt)       | 로그인 상태 유지를 위한 토큰 기반 인증 처리                |
| JPA              | 객체 중심의 DB 매핑, 생산성 높은 CRUD 처리             |
| QueryDSL         | 타입 안정성 있는 동적 쿼리 작성                       |
| JavaMailSender   | 회원가입 완료 시 사용자에게 이메일 발송 기능 구현             |
| Kotlin           | 간결하고 효율적인 문법으로 이메일 서버 개발                 |
| Coroutine        | 이메일 발송 시 비동기 처리로 서버 자원 효율화               |
| ModelMapper      | DTO ↔ Entity 간 매핑 자동화로 코드 간결화            |
| Validation       | 사용자 입력값 검증을 통한 데이터 무결성 확보                |
| Lombok           | 반복되는 getter/setter, 생성자 자동화              |
| Thymeleaf        | 템플릿 엔진을 통한 동적 HTML 렌더링                   |
| Thymeleaf Extras | 로그인 상태에 따른 동적 UI 제어 (Spring Security 연동) |
<br>

## <span id="6">🤝 6. 컨벤션</span>

### 커밋 메시지 규칙

| 태그       | 설명                     |
| ---------- | ------------------------ |
| feat       | 기능 추가                |
| fix        | 버그 수정                |
| docs       | 문서 작업                |
| refactor   | 리팩토링                 |
| style      | 코드 스타일 변경         |
| test       | 테스트 코드 관련         |
| chore      | 설정 파일 변경 등 기타   |

> 예시: `(0611) feat: [#이슈번호] 목표 기업 D-DAY 알림 기능 추가`

<br>

## <span id="7">🗂️ 7. 브랜치 및 디렉토리 구조</span>

### 브랜치 전략
- `main`: 배포 브랜치
- `develop`: 통합 개발 브랜치
- `feature/이름-기능명`: 개인 기능 구현용 브랜치

### 디렉토리 구조
📂 DevQuest
├── 📂 config
├── 📂 controller
├── 📂 model
│   ├── 📂 dto
│   ├── 📂 entity
│   ├── 📂 repository
│   └── 📂 service
├── 📂 resources
│   ├── 📂 static
│   ├── 📂 templates
│   └── 📄 application.yml
└── 📄 DevQuestApplication.java

<br>

## <span id="8">💻 8. 주요 기능 소개</span>

- 🔐 회원가입 / 로그인(JWT) / 마이페이지
- 🏆 업적 시스템 기반의 레벨 업 & 경험치 부여
- 🗂 목표 기업 관리 + 기업별 TODO + 마감일(D-day) 알림
- 💬 AI 멘탈 관리 챗봇 (프롬프트 기반)
- 📊 대시보드: 관심 분야, 업적, 주요 알림 통합 시각화
- 🛠 관리자 페이지: 유저 관리, 기업관리, 정렬 및 검색 기능

<br>

## <span id="9">📄 9. 상세 담당 업무</span>

### 🧠 이초롱 (Dashboard 담당)

- 전체 대시보드 구성 및 데이터 매핑
- 사용자별 관심 분야/알림 필터링 기능
- 관리자용 사용자 필터 및 검색 UI
- 카드형 UI 구성, 목표 TODO 연결 처리

### 🧠 김나단 (사용자별 목표 담당)

- 전체 목표, 투두 구성 및 데이터 매핑
- 목표 코딩테스트 선택시 투두로 자동 매핑
- 카드형 UI 구성, 목표 투두 연결 처리
- 풀캘린더 API 사용 하여 목표 별 투두 일정 관리 기능 추가

### 🧠 노선우 (AI챗봇 담당)

- 제미나이 API 적용
- 사용자 맞춤형 AI챗봇 성격부여 기능
- 사용자의 입력을 위한 input구현
- 대화맥락 파악을 위한 session단위 내용저장
- 챗봇 UI창 모달구현 및 스타일 변경
  
### 🧠 채철민 (마이페이지 및 게임적 요소 담당)

- 마이페이지 CRUD 담당
- 게임적 요소 반영
  * 사용자 레벨 취득 기능
  * 사용자 별 업적 취득
  * 업적 및 레벨 획득 시 이벤트 요소
- 관리자 페이지 내 업적 관리

### 🧠 임서현 (로그인 방식 개선(JWT) 및 이메일 발송 서버)

- (3차) JWT 기반 인증/인가 처리 로직 리팩토링
- (3차) 회원가입 완료 시, 이메일 발송 (Kotlin, 별도 서버)
- 인덱스, 회원가입, 로그인 페이지
- 관리자 대시보드 (회원관리)
- 목표 페이지 일부 UI

> 주요 기술: Spring MVC, Thymeleaf, JavaScript, MySQL, DTO 설계, @ModelAttribute 바인딩
> 
---

기타 팀원 담당 기능은 [2. 팀원 소개](#2) 참고

<br>

## <span id="10">🛠️ 시작 가이드</span>


### 1. 프로젝트 클론
$ git clone https://github.com/prgrms-be-devcourse/NBE5-6-2-Team04.git

### 2. IntelliJ로 프로젝트 열기
의존성 자동 로딩 (Maven)

### 3. 로컬 DB 세팅 (application.yml 수정 후 MySQL 연결)

### 4. 실행
Run 'DevQuestApplication' 또는 ./mvnw spring-boot:run
