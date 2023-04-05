# 서비스 소개
회사원들의 근태를 관리와 식사메뉴선정, 식사인원배치 시스템을 가진 프로그램입니다.:)<br>
총 3개의 Page로 이루어져 있습니다.<br>
- **출퇴근시스템** - 직원들의 근태,휴식관리를 할 수 있습니다. 실시간으로 나의 근무시간, 휴식시간, 퇴근예정시간을 알 수 있습니다.<br>
- **밥투표시스템** - 저녁식사 메뉴 선정을 돕는 Page 입니다. 직원들끼리 식사 메뉴를 투표하여, 3개까지 선정할 수 있습니다.<br>
- **우정의밥시스템** - 다른팀 직원들과 식사를 할 수 있게 인원배치를 도와주는 Page 입니다.<br>



# 핵심 기능 소개

|출근|
|---|
|![출근](https://user-images.githubusercontent.com/70586428/229975791-3d42f937-1138-4f7e-a804-65a0bdbe13d1.gif)|

|휴식시작 & 종료|
|---|
|![휴식시작 종료](https://user-images.githubusercontent.com/70586428/229976123-34272dd6-c500-4228-8c74-c8023240b5fa.gif)|

|페이징 처리|
|---|
|![페이징](https://user-images.githubusercontent.com/70586428/229976202-955cea21-1e6d-4ed1-bdb8-da8d6f9761d9.gif)|

|퇴근|
|---|
|![퇴근](https://user-images.githubusercontent.com/70586428/229976271-e7dd20be-359a-419c-a6eb-617578753214.gif)|

|통계 : 팀별조회|
|---|
|![통계팀별조회](https://user-images.githubusercontent.com/70586428/229976442-25687904-20ef-4f56-b85c-3dcb944c649b.gif)|

|통계 : 달력 & 팀별조회|
|---|
|![통계달력조회](https://user-images.githubusercontent.com/70586428/229976457-e4fe78fe-da20-4612-b373-300ab55af944.gif)|

|밥투표 추가|
|---|
|![밥투표추가](https://user-images.githubusercontent.com/70586428/229976530-390d4a64-1b36-4659-9a47-fbb8e8609709.gif)|

|밥투표 투표 & 툴팁|
|---|
|![밥투표투표툴팁](https://user-images.githubusercontent.com/70586428/229976546-be59933b-5f0f-4130-b313-de9d3236a66d.gif)|


|우정의 밥 : 생성 & 삭제|
|---|
|![우정의밥](https://user-images.githubusercontent.com/70586428/229976553-0da4f2c2-9c73-4e02-a328-7011ebd14dc7.gif)|


|로그인 & 로그아웃|
|---|
|![로그인로그아웃](https://user-images.githubusercontent.com/70586428/229976611-2746e9f4-6b8c-4085-be1c-a823a0ea7079.gif)|

|회원가입|
|---|
|![회원가입](https://user-images.githubusercontent.com/70586428/229976607-3e8d57d8-7910-4c3d-a5d0-d9557a4c09ea.gif)|



### 주요기능
- 출근, 퇴근 기능
  - 각 버튼을 누를 시, 누른 시간이 DB에 기록됩니다.
  - 출근시간이 10:00가 넘어간다면, 지각 사유를 입력하도록 팝업창이 뜨고, 근무시간이 7시간 미만이라면 퇴근 비고를 입력하도록 팝업창이 뜹니다.
- 휴식시작, 휴식종료 기능
  - 휴식시작시 시간이 DB에 저장되며, 실시간으로 휴식중인 시간이 사용자에게 보여집니다.
  - 휴식중에는 실시간으로 보여지던 근무시간은 멈춰있습니다.
  - 휴식은 사유를 입력할 수 있고, 휴식 종료를 누른다면 DB에 시간이 저장됩니다.
  - 휴식 종료시 실시간으로 보여지던 휴식시간은 멈춰있게 되며, 근무시간이 다시 실시간으로 count되어 보여집니다.
- 페이징 처리
  - 출퇴근 데이터가 10개 이상부터 페이징 처리가 됩니다.
  - first, last 버튼을 누른다면 맨처음과 맨끝으로 이동하게 됩니다.
- 통계현황 조회
  - 원하는 날짜와 원하는 팀별로 출근현황을 조회할 수 있습니다.
  - 상단에는 해당 데이터의 출근인원이 몇명인지 조회됩니다.
- 밥투표 추가, 투표 기능 (툴팁포함)
  - 원하는 밥 메뉴를 추가할 수 있으며, 원하는 메뉴에 투표할 수 있습니다.
  - 투표를 했더라도, 재투표를 하여 다시 투표할 수 있습니ㅏㄷ.
  - 투표한 인원을 툴팁을 통하여 누가 투표했는지 알 수 있습니다.
- 우정의밥
  - 각각 다른 팀끼리 4명(혹은 3명) 씩 한날짜에 배치되게 됩니다.
  - 최대한 같은팀 인원이 배치되지 않고 1,2명씩 배치되지않게합니다 (ex - 총인원이 5명이라면 3명/2명으로 떨어지도록)
- 로그인 & 로그아웃 기능
  - 로그인시, id기억 버튼을 누르면 쿠키에 id값이 저장됩니다.
- 회원가입 기능
  - 회원가입 시, id는 4자리 이상 허용됩니다.
  - 모든 조건이 일치해야 회원가입이 완료됩니다.





### 👁️프로젝트 규칙

- (월~금) 09:00 개발 시작, 18:00 개발 종료
- 개발 시작 전, 소요 일정을 먼저 계획
    - 계획했던 기간내에 최대한 끝낼 수 있도록 개발
- 매일 개발일지를 작성하여 Notion에 문서화
- 매주 수요일, 금요일
    - 개발자 친구들에게 코드리뷰를 받는 시간을 가짐
    - 현재 개발하는 프로젝트를 점검하고, 계획 수정 & 회고의 시간을 가짐

# Tech Stacks
### Frontend
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white"> <img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jQuery&logoColor=white"> <img src="https://img.shields.io/badge/CSS-F43059?style=for-the-badge&logo=CSS&logoColor=white"> <img src="https://img.shields.io/badge/HTML-302683?style=for-the-badge&logo=HTML&logoColor=white">
### Backend
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Maven-CC0000?style=for-the-badge&logo=Maven&logoColor=white"> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"> <img src="https://img.shields.io/badge/Python-EE4C2C?style=for-the-badge&logo=Python&logoColor=white">
### DB & Infra
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white"> <img src="https://img.shields.io/badge/JPA-02A8EF?style=for-the-badge&logo=JPA&logoColor=white"> <img src="https://img.shields.io/badge/JPQL-ED145B?style=for-the-badge&logo=JPQL&logoColor=white"> <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/AWS Elastik Beanstalk-232F3E?style=for-the-badge&logo=Amazon AWS&logoColor=white">

# Member
|Back-end & Front-end|
|:---:|
|<img src="https://user-images.githubusercontent.com/70586428/197694674-88686917-38b4-4d9c-8a6e-93367fb56055.jpg" width="100"/>|
|[안인택](https://github.com/intacka)|

# Wiki
[Wiki 보러가기](https://shrouded-marigold-0d2.notion.site/WORK-COMMUTE-07c6a68d058749c0a7fe5e76b46f5668)
