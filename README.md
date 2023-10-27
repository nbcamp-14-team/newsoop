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

## 📼 **구현 클래스 & 상세기능**

### **1) HomeFragment**

<img width="200" alt="finder6" src="https://github.com/Android-Team-13-Maniacs/android_project_maniacs/assets/106515742/20340abc-de8e-4bd8-b22a-29757b185f0e">


* **YouTube API**를 사용하여 동영상과, title 출력

- **상단 배너** 인기 비디오 리스트 와 타이틀 출력
  - 3초마다 **자동으로 스크롤** 되고 , 인디케이터를 부착하여 몇번째 영상인지 표시

- **다양한 카테고리** 추천 Video 및 타이틀 출력 (가로 슬라이드)
  - 스피너를 선택하여 **카테고리 이동** 가능

- 선택한 카데고리에 맞춰 **채널 추천** (가로 슬라이드)

- 모든 아이템은 클릭할시 **디테일 페이지로 이동**
  
- NullPointerException 예외 처리하여 영상을 불러오지 못한 경우 Toast Message 출력

</br>

### **2) SearchFragment**

<img width="200" alt="finder1" src="https://github.com/Android-Team-13-Maniacs/android_project_maniacs/assets/106515742/ad55c86f-99da-43c1-9b17-811ef87eb456"><img width="200" alt="finder5" src="https://github.com/Android-Team-13-Maniacs/android_project_maniacs/assets/106515742/a3a1cf8c-cdf2-4b96-9c48-3cfdeb29d42e">



- SearchView 로 **원하는 키워드 검색** 가능

- 동영상이 없을때는 "검색해주세요" 라는 **키워드 노출**

- **버튼 클릭**으로 원하는 순서대로 **정렬** 가능 (DATE, RATING, TITLE COUNT)

- 동영상 검색 결과를 아래로 스크롤 하면 페이지가 하나씩 추가되도록하여, **무한 스크롤 기능 구현**

- **Floating Button** 클릭 시 최상단 이동(아래로 슬라이드시에는 보이지 않음, 상단슬라이드시 노출)

- 모든 아이템은 클릭할시 **디테일 페이지로 이동**

- NullPointerException 예외 처리하여 영상을 불러오지 못한 경우 Toast Message 출력

</br>

### **3) DetailActivtiy**

<img width="200" alt="finder7" src="https://github.com/Android-Team-13-Maniacs/android_project_maniacs/assets/106515742/8abb4f5e-cd07-4e4d-9c95-71ff7ff2ce78">


* **YouTube API**를 사용
  - **영상 제목**, **설명**, **게시 일자 출력** / Channel API 활용 **채널 썸네일** 출력

- My List (+) 버튼 클릭 시 해당 영상이 **My VIDEOS 에 저장**

- Like 버튼 클릭 시 **좋아요** 기능 구현

- Share 버튼 클릭시  **Android의 공유** 인텐트를 사용하여 다른 앱으로 비디오 제목 전송

- Detail page 시작과 종료시 **Fade Effect** 추가

- NullPointerException 예외 처리하여 영상을 불러오지 못한 경우 Toast Message 출력

</br>

### **4) MyPageFragment**

<img width="200" alt="finder4" src="https://github.com/Android-Team-13-Maniacs/android_project_maniacs/assets/106515742/119fb69e-670c-4246-9993-6fdb5c1339e1"><img width="200" alt="finder3" src="https://github.com/Android-Team-13-Maniacs/android_project_maniacs/assets/106515742/6cfb20fb-cdef-46c9-85c5-24ca7218eae6">



- **My List(+) 추가**를 선택한 Video 리스트 출력 

- 상단 수정 버튼으로 **프로필 이름 및 사진 수정** 가능
  - 둘중 하나만 수정해도 반영되도록 구현
  - 닉네임은 10자이하로만 할 수 있도록 예외처리
  - 닉네임을 입력하지 않고 저장했을시, "닉네임을 입력해주세요" 라는 팝업문구가 뜨게됨
  - 다이얼로그 취소버튼 클릭시 정말 취소 하겠냐는 팝업문구 띄워짐

- My Video 및 좋아요 Shared Preference 저장 되어 있어서 어플을 종료해도 화면에 반영됨

- NullPointerException 예외 처리하여 영상을 불러오지 못한 경우 Toast Message 출력

</br>



