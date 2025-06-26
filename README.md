<div id="top"></div>

<div align='center'>

<h1><b>DevQuest - 게이미피케이션 기반 TODO 서비스</b></h1>
<h3><b>프로그래머스 데브코스 5기 6회차 Team 04 - 3차 프로젝트</b></h3>

<img src="/src/main/resources/static/img/DevQuestLogo.png" alt="DevQuest 로고 이미지" width="300"/>

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

**서비스명**: DevQuest  
**설명**: 게이미피케이션 요소(레벨, 업적, 경험치 등)를 접목한 개발자 지향 TODO 서비스로, 목표 기업 및 기술 성장 관리를 돕습니다.

<br>

## <span id="2">🏃 2. 팀원 소개</span>

| 이름       | 역할                                        | GitHub                                             |
| ---------- | ------------------------------------------- | -------------------------------------------------- |
| 채철민     | 팀장 / 마이페이지 / 업적 시스템             | [@moooonbong](https://github.com/moooonbong)       |
| 임서현     | 메인 인덱스 / 회원가입 / 로그인 / 관리자 페이지 | [@nunLSH](https://github.com/nunLSH)              |
| 노선우     | AI 멘탈 관리 기능                           | [@noopy2020](https://github.com/shtjsdn2000)      |
| 김나단     | 목표 기업 TODO / 목표 상세 페이지            | [@nathan960307](https://github.com/nathan960307)  |
| 이초롱     | 대시보드 / 관심 분야 / 알림 정렬 및 필터     | [@0802222](https://github.com/0802222)            |

<br>

## <span id="3">📅 3. 프로젝트 일정</span>

> 개발 기간: 2025.04.25 ~ 2025.05.19 (약 3.5주)

<img src="/src/main/resources/static/img/WBS.jpg" alt="DevQuest 일정 구성도" width="600"/>

<br>

## <span id="4">📚 4. 기술 스택</span>

- **Backend**: Java, Spring Boot, Spring Data JPA
- **Frontend**: HTML, CSS, JavaScript, Thymeleaf
- **Database**: MySQL, H2 (개발용)
- **Infra**: AWS EC2, GitHub Actions (CI), GitHub

> 협업 툴: Notion, Figma, Zoom, Slack, Zep, Google Sheet

<br>

## <span id="5">❓ 5. 라이브러리 사용 이유</span>

| 라이브러리   | 목적 및 이유 |
| ------------ | ------------ |
| Spring Boot  | 빠른 설정과 의존성 관리로 개발 생산성 향상 |
| JPA          | 객체 중심의 DB 매핑, 생산성 높은 CRUD 처리 |
| Thymeleaf    | 템플릿 엔진을 통한 동적 HTML 렌더링 |
| Lombok       | 반복되는 getter/setter, 생성자 자동화 |
| H2 DB        | 테스트용 인메모리 DB 환경 구성 |

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

> 예시: `feat: 목표 기업 D-DAY 알림 기능 추가`

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

- 🔐 회원가입 / 로그인 / 마이페이지
- 🏆 업적 시스템 기반의 레벨 업 & 경험치 부여
- 🗂 목표 기업 관리 + 기업별 TODO + 마감일(D-day) 알림
- 💬 AI 멘탈 관리 챗봇 (프롬프트 기반)
- 📊 대시보드: 관심 분야, 업적, 주요 알림 통합 시각화
- 🛠 관리자 페이지: 유저 관리, 정렬 및 검색 기능

<br>

## <span id="9">📄 9. 상세 담당 업무</span>

### 🧠 이초롱 (Dashboard 담당)

- 전체 대시보드 구성 및 데이터 매핑
- 사용자별 관심 분야/알림 필터링 기능
- 관리자용 사용자 필터 및 검색 UI
- 카드형 UI 구성, 목표 TODO 연결 처리

> 주요 기술: Spring MVC, Thymeleaf, JavaScript, MySQL, DTO 설계, @ModelAttribute 바인딩

---

기타 팀원 담당 기능은 [2. 팀원 소개](#2) 참고

<br>

## <span id="10">🛠️ 시작 가이드</span>


# 1. 프로젝트 클론
$ git clone https://github.com/prgrms-be-devcourse/NBE5-6-2-Team04.git

# 2. IntelliJ로 프로젝트 열기
#    의존성 자동 로딩 (Maven)

# 3. 로컬 DB 세팅 (H2 또는 application.yml 수정 후 MySQL 연결)

# 4. 실행
Run 'DevQuestApplication' 또는 ./mvnw spring-boot:run
