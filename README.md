![header](https://capsule-render.vercel.app/api?type=waving&color=0:6e45e2,100:88d3ce&height=290&section=header&text=[NBCamp]%2014조%20NEWSOOP&fontColor=ffffff&fontSize=50&animation=blink&fontAlignY=38&desc=[내배캠]%20최종%20프로젝트)

##  🦭**프로젝트 소개**
팀명  :NEWSOOP

프로젝트 소개 :  NEWSOOP은  뉴스의 원하는 기사을 검색하고 즐길 수 있는 어플리케이션입니다.   

키워드 검색, 인기 기사, 카테고리 기사를 통해  여러분의 취향에 맞는 콘텐츠를 찾을 수 있습니다. 

| 이름   | 역할 | MBTI        | BLOG                                               | GitHub                                                  | 
| ------ | ---- | ---------- | -------------------------------------------------- | -------------------------------------------------------- |
| 조광희 | 팀장 | INTP        | (https://velog.io/@rising1/)  | (https://github.com/ckh124)                |
| 정현식 | 팀원 | ISTP        | (https://velog.io/@jeans1241)       | (https://github.com/junghyunsick/git-test) |
| 이슬비 | 팀원 | ISTJ        | (https://velog.io/@ouowinnie)       | (https://github.com/Seulbi-Lee-project)                |
| 이승훈 | 팀원 | ENTP        | (https://velog.io/@thundevistan) | (https://github.com/lsshhh)          |


</br>

**🐒기능**

- 다양한 뉴스 플랫폼 검색 및 통합
  
- 키워드 검색 및 정확한 필터링 옵션 제공
  
- 콘텐츠 북마크 기능으로 관심 있는 콘텐츠 저장

- 개인화된 추천 시스템을 통한 콘텐츠 추천



##  🦧**기술 스택**
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=white"/>
<img src="https://img.shields.io/badge/AndroidStudio-3DDC84?style=flat-square&logo=AndroidStudio&logoColor=white"/>
<img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white">

</br>

##  🐐**와이어프레임**

<"<a href='https://ifh.cc/v-lf2Rgd' target='_blank'><img src='https://ifh.cc/g/lf2Rgd.png' border='0'></a>">

</br>



* #### 🐏**GIT**

- [x] **git add / commit / push 활용**

- [x]  **git 브랜치/ PR / merge 활용**

- [x] **github pull request에서 Code review 활용**

</br>

##  **구현 클래스 & 상세기능**

### **1) HomeFragment**
<img width="150" alt="finder6" src="https://ifh.cc/g/LByqsc.png"><img width="150" alt="finder5" src="https://ifh.cc/g/HwgF4A.jpg"><img width="150" alt="finder5" src="https://ifh.cc/g/YZKpjh.jpg">

- 메인 액티비티 구성, 네이버 뉴스 api 받아오기
- 메인 프래그먼트 화면 구성(viewtype활용)
- 로그인시 추천 기사 항목 추가
  
  

</br>
### **2) LogInActivity**

<img width="150" alt="finder1" src="https://ifh.cc/g/6J3tXN.png"><img width="150" alt="finder5" src="https://ifh.cc/g/gvaCQf.png"><img width="150" alt="finder5" src="https://ifh.cc/g/fnX1gz.png"><img width="150" alt="finder5" src="https://ifh.cc/g/fR13A4.png">

- 로그인, 회원가입화면 구성
- 소셜로그인 기능
- 회원 정보 파이어베이스에 저장하기

</br>

### **2) SearchFragment**

<img width="150" alt="finder1" src="https://ifh.cc/g/Or8ptg.png"><img width="150" alt="finder5" src="https://ifh.cc/g/Rdh9qb.png"><img width="150" alt="finder5" src="https://ifh.cc/g/Xhazhr.png"><img width="150" alt="finder5" src="https://ifh.cc/g/4BQ3NH.jpg">

- 검색화면 구성
- 키워드 검색 기능
- 검색 기능
- 태그 기능
- 무한 스크롤 기능
</br>

### **3) DetailActivtiy**

<img width="150" alt="finder7" src="https://ifh.cc/g/zmzZ4h.png"><img width="150" alt="finder7" src="https://ifh.cc/g/o091S5.jpg">


- 상세페이지 기능 구현(MVVM패턴 적용)
- 홈화면, 검색화면에서 기사 클릭시 상세페이지로 데이터 받기
- 음성인식 기능
- 공유하기 기능
- 북마크 기능 클릭시 즐겨찾기 페이지에 추가 및 FireStore에 데이터 저장 및 저장이 되어있으면 삭제 - 로그인이 안되어있는 상태로 북마크 버튼 클릭시 로그인 페이지로 이동
- 원문 보기 클릭시 원문URL로 이동

</br>

### **4) MyPageFragment**

<img width="150" src="https://ifh.cc/g/kjdZDc.png"><img width="150" alt="finder3" src="https://ifh.cc/g/yAppvj.jpg"><img width="150" alt="finder3" src="https://ifh.cc/g/2QymG5.png"><img width="150" alt="finder3" src="https://ifh.cc/g/D02PfT.jpg"><img width="150" alt="finder3" src="https://ifh.cc/g/kNYgLb.png">



- **My List(+) 추가**를 선택한 기사 리스트 출력 
- MVVM 패턴 적용
- 로그인시 Firestore에 저장되어있는 데이터를 즐겨찾기페이지에 바로 적용
- 구글 로그인시 Firestore에 저장되어있는 데이터를 즐겨찾기페이지에 바로 적용
- 로그아웃 버튼 클릭시 즐겨찾기 페이지를 클리어하고 로그인UI 로 변경
- 앱종료시 자동 로그아웃 기능
- 카테고리 선택
- 프로필 사진 바꾸기 기능

</br>

### **5)Newspaper Fragment**

<img width="150" src="https://ifh.cc/g/jDCyr6.png"><img width="150" alt="finder3" src="https://ifh.cc/g/PXDttB.jpg">

- 신문사별 & 카테고리별 기사 제공

</br>


### **6)Debate Fragment**

<img width="150" src="https://ifh.cc/g/kd2H3v.png"><img width="150" alt="finder3" src="https://ifh.cc/g/XqC718.png">

- 토론글 올리기 & 삭제하기
- 투표하기 기능
- 댓글 쓰기 기능

</br>

### **7)Setting Activity**

<img width="150" src="https://ifh.cc/g/3tq1gD.png">

- 오픈소스 라이센스
- 문의 메일 보내기 기능

</br>
</br>
</br>
