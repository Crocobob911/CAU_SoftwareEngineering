# CAU_SoftwareEngineering

CAU 소프트웨어공학 02분반 Team 4

# 게임의 구성

## 게임 흐름
1. UI를 통한 게임 세팅(플레이어수, 말 수 설정)
2. GameManager 생성 후 시작
3. 각 플레이어 턴 반복

## 용어
턴 내부에 List<YutResult> pendingMoves와 int Action 이 있음
List<YutResult> pendingMoves 는 남은 움직임 수
int Action 은 윷 던지기 남은 수
Move와 Action은 자유롭게 순서 배치 가능

## 맵 구성 아이디어
어떤 맵이라도 1 2 3 4 5(종점, 분기로 나뉜다)