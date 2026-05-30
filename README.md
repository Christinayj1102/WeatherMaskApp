# AI 기반 날씨 마스크 추천 애플리케이션

2026-1학기 자바 프로그래밍 기말 프로젝트

## 프로젝트 소개

AI 기반 날씨 마스크 추천 애플리케이션은 실시간 날씨 정보와 대기질 정보를 분석하여 사용자에게 적절한 마스크 착용 여부를 추천하는 Java Swing 기반 GUI 프로그램입니다.

본 프로젝트는 기상청 API와 에어코리아 API를 활용하여 현재 기온, 습도, 강수 여부, PM10, PM2.5 등의 정보를 수집합니다. 수집된 데이터를 바탕으로 규칙 기반(Rule-Based) 추천을 수행하며, OpenAI API를 이용하여 사용자가 이해하기 쉬운 자연어 형태의 추천 문장을 생성합니다.

## 주요 기능

### 1. 실시간 날씨 정보 조회

* 기상청 API 활용
* 현재 기온 조회
* 현재 습도 조회
* 강수 형태 조회
* 강수량 조회

### 2. 실시간 대기질 정보 조회

* 에어코리아 API 활용
* PM10 조회
* PM2.5 조회
* 통합대기환경지수(KHAI) 조회
* 대기질 등급 조회

### 3. 마스크 착용 추천

수집된 데이터를 기반으로 마스크 착용 필요도를 판단합니다.

예시

* PM2.5 낮음 → 마스크 착용 필요성 낮음
* PM2.5 보통 → 민감군 마스크 권장
* PM2.5 높음 → KF80 이상 권장
* PM2.5 매우 높음 → KF94 권장

### 4. OpenAI 기반 자연어 추천

규칙 기반 추천 결과를 OpenAI API에 전달하여 자연스러운 영어 추천 문장을 생성합니다.

예시

> The air quality is moderate today, so sensitive individuals may want to wear a mask for extra protection.

### 5. GUI 인터페이스

Java Swing을 이용하여 구현하였습니다.

기능

* 현재 날씨 정보 표시
* 현재 대기질 정보 표시
* Rule-Based Recommendation 표시
* AI Recommendation 표시
* 전체 데이터 새로고침
* AI 추천 문장 재생성

---

## 사용 기술

### Language

* Java

### GUI

* Java Swing

### API

* 기상청 단기실황조회 API
* 에어코리아 대기오염정보 API
* OpenAI API (GPT-4o-mini)

---

## 프로젝트 구조

```text
WeatherMaskApp
├── AirQualityData.java
├── AirQualityService.java
├── Main.java
├── MainFrame.java
├── MaskRecommender.java
├── OpenAIService.java
├── WeatherData.java
├── WeatherService.java
├── .gitignore
└── README.md
```

---

## 실행 방법

### 1. 저장소 복제

```bash
git clone https://github.com/Christinayj1102/WeatherMaskApp.git
cd WeatherMaskApp
```

### 2. config.properties 생성

프로젝트 루트 디렉터리에 아래 파일을 생성합니다.

```properties
KMA_API_KEY=YOUR_KMA_API_KEY
AIR_API_KEY=YOUR_AIRKOREA_API_KEY
OPENAI_API_KEY=YOUR_OPENAI_API_KEY
```

### 3. 컴파일 및 실행

```bash
javac *.java
java Main
```

---

## 주의사항

보안을 위해 API 키가 포함된 `config.properties` 파일은 GitHub에 업로드하지 않았습니다.

프로그램 실행 시 사용자가 직접 API 키를 발급받아 설정해야 합니다.


