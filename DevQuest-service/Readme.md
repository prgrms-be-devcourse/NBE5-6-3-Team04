<div id="top"></div>

<div align='center'>

<h1><b>DevQuest - 게이미피케이션 기반 TODO 서비스</b></h1>
<h3><b>프로그래머스 5기 6회차 Team 04 프로젝트</b></h3>

<img src="/src/main/resources/static/img/DevQuestLogo.png" alt="DevQuest 로고 이미지"/>

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
**설명**: 게이미피케이션 요소를 적용한 Todo 서비스 구현

<br>

## <span id="2">🏃 2. 팀원 소개</span>

| 이름       | 역할                                    | GitHub                                             |
| -------- | -----------------------                   | ------------------------------------------------   |
| 채철민 (팀장) | 마이페이지 / 업적 시스템                | [@moooonbong](https://github.com/moooonbong)       |
| 임서현        | 인덱스, 회원가입, 로그인, 관리자 페이지 | [@nunLSH](https://github.com/nunLSH)                |
| 노선우        | AI 기능                               | [@noopy2020](https://github.com/shtjsdn2000)     |
| 김나단        | 목표 기업 TODO                         | [@nathan960307](https://github.com/nathan960307) |
| 이초롱        | 대시보드                               | [@0802222](https://github.com/0802222)           |

<br>

## <span id="3">📅 3. 프로젝트 일정</span>

> 프로젝트 기간: 2025.04.25 - 2025.05.19 (25일)
 
<img src="/src/main/resources/static/img/WBS.jpg" alt="DevQuest 로고 이미지"/>

<br>

## <span id="4">📚 4. 기술 스택</span>

> Java / Spring Boot / JPA / MySQL / Thymeleaf / HTML & CSS / JS

* 버전 관리: Git, GitHub
* 협업 툴: Notion, Figma, Zoom, zep, slack, google sheet

<br>

## <span id="5">❓ 5. 라이브러리 사용 이유</span>

* Spring Boot: 빠른 서버 개발 환경 구축
* JPA: ORM 기반의 DB 매핑
* Thymeleaf: 동적 HTML 렌더링
* Lombok: 반복 코드 제거
* H2 DB: 테스트용 임시 데이터베이스

<br>

## <span id="6">🤝 6. 컨벤션</span>

### 커밋 컨벤션

| 타입       | 설명                
| -------- | --------------        
| feat     | 기능 추가              
| fix      | 버그 수정              
| docs     | 문서 관련              
| refactor | 코드 리팩토링           
| style    | 스타일 수정             
| test     | 테스트 관련             
| chore    | 기타 설정, 빌드 작업 등 

예시: `feat: 대시보드 알림 정렬 기능 추가`

<br>

## <span id="7">🗂️ 7. 브랜치 및 디렉토리 구조</span>

> 브랜치 전략:

* `main`: 배포용 안정 브랜치
* `develop`: 통합 개발 브랜치
* `이름/기능` : 개인 브랜치 안에서 기능별로 구현 

> 디렉토리 구조

```
📂 DevQuest
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
├── 📂 config
└── 📄 DevQuestApplication.java
```

<br>

## <span id="8">💻 8. 주요 기능 소개</span>

* 회원가입 / 로그인 / 마이페이지
* 업적 시스템 기반의 레벨 & 경험치
* 목표 기업 TODO 관리 (D-day 기반 알림)
* AI 기능을 통해 사용자 멘탈 관리리
* 관리자용 유저 관리 페이지

<br>

## <span id="9">📄 9. 상세 담당 업무</span>

### 채철민 (팀장)

* 마이페이지, 업적 시스템, 레벨 계산, 경험치 반영 기능 구현 

### 임서현

* 메인 인덱스, 서비스 소개, 회원가입/로그인 기능, 관리자 페이지 구현

### 노선우

* AI 멘탈 케어 챗봇 기능 적용, AI 프롬프트 엔진니어링


### 김나단

* 목표 기업, 기업별 목표, 목표 별 TODO 로직, 목표 페이지 구현

### 이초롱

* 대시보드 구현, 주요 알림, 관심 분야, 관리자 페이지 정렬, 검색 기능 구현

<br>

## <span id="10">🛠️ 시작 가이드</span>

```bash
# 1. 프로젝트 클론
$ git clone https://github.com/prgrms-be-devcourse/NBE5-6-2-Team04.git

# 2. IntelliJ에서 열기 & 의존성 설치
- Maven Reload 후 Run 'DevQuestApplication'

# 3. H2 콘솔 확인 또는 MySQL 연결 설정 후 테스트
```

<br>

<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white;'>▲</a></p>
