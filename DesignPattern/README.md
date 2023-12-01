# DesignPattern

### 가장 좋은 Architecture Pattern?
모든 것에는 장점과 단점이 있기 때문에 반드시 무엇을 사용해야 한다는 것은 없다  
최신 디자인을 따르기 보다는 규모와 팀과의 협업을 생각하며 선택  

[패키지 경로](https://github.com/wo9374/StudyProject/tree/main/DesignPattern/app/src/main/java/com/ljb/designpattern)

***

### MVC

<img width="650" height= "250" src="https://github.com/wo9374/StudyProject/assets/55440637/50d05422-406a-4c6c-ad23-5b07258366f4">

Model
- 어플리케이션에 사용되는 데이터와 처리 로직이 담김
- View 또는 Controller와 묶이지 않기때문에 재사용이 가능

View(UI)
- 사용자에게 보여주는 보이는 화면

Controller
- 사용자로부터 입력을 받고, 모델에 의해 View정의
&nbsp;

&nbsp;
#### Android에서는 View와 Controller가 Activity에 포함
#### 이러한 문제점 때문에 Activity에 코드들이 몰리는 현상이 발생
<img width="650" height="450" alt="mvc_2" src="https://github.com/wo9374/StudyProject/assets/55440637/0a616ee2-79e3-4a06-8198-c544c46ef779">

장점
- 구현이 쉽고, 이해하기 쉬우며, 구현 속도도 가장 빠름

단점
- Controller와 View가 결합되어 있기 때문에 UnitTest에 어려움이 있음. UI 로직과 Controller에서 실행하는 로직들을 분리하기 어렵기 때문
- UI변경점이 있을 때, Controller의 코드 변경 또한 불가피
- Model과 View의 의존성이 높기 때문에 앱이 커질수록 스파게티 코드가 될 위험이 높아짐 (MVP의 등장 배경).

***

### MVP

MVP의 특징은 Activity가 View의 역할만 수행

Android에 있어서 MVP는 컴포넌트 분리가 좀더 명확하게 이루어 짐

<img width="650" height= "350" src="https://github.com/wo9374/StudyProject/assets/55440637/e4149ed6-896b-4959-9c09-069b99740538">

Model
- MVC의 모델과 동일

View (UI)
- Activity를 자연스럽게 View의 일부로 간주하게 됨

Presenter
- Controller과 같은 역할을 하나, Interface로 이루어져 UnitTest에 자유로움

<img width="650" height= "400" src="https://github.com/wo9374/StudyProject/assets/55440637/34340b34-f0f5-443d-9b0e-7f7691aa80b2">

이제 Activity단에서 바로 Model을 호출하는 것이 아닌, Presenter에서 View로 전달

장점
- Model과 View의 의존성이 없어졌기 때문에 관련된 코드만 수정이 용이
- UI와 Data파트가 명확히 구분되어 간편하게 코딩 가능

단점
- 애플리케이션이 커질수록 View와 Presenter 사이의 의존성이 강해짐
- 기능이 많아지면 Presenter의 비중이 커지기 때문에 분리하기가 어려움

#### MVC와 다르게 명확하게 컴포넌트가 구분되기 때문에 역할에 따라 분리가 잘된 코드를 볼 수 있지만, 
#### Presenter와 View가 1:1 관계를 갖고 있기 때문에 프로젝트가 커질수록 Presenter또한 비례해서 늘어나게 됨

***

### MVVM

MVVM은 presenter에 의존하지 않고, Observer pattern을 이용해 객체의 변경이 일어날 때마다 UI를 갱신

> Observer Pattern<br>
> 상태 변화가 있을 때마다 메서드를 통하여 관찰 대상자가 직접 옵저버들에게 통지하여 상태를 동기화할 수 있도록 하는 디자인 패턴을 의미<br>
> 즉, 객체에 변화가 일어날 때마다 콜백이 일어나게 됨

<img width="650" height= "400" src="https://github.com/wo9374/StudyProject/assets/55440637/1fdf4e18-61fb-4d12-af5d-60b2afc8b6d2">

Model
- MVC의 모델과 동일

View (UI)
- ViewModel의 데이터들을 구독하고 있다가 객체가 변할 때 UI를 업데이트

ViewModel
- Model과 상호 작용하며, View에 종속되지 않고 1:N 구조를 가짐  
MVVM의 가장 큰 특징은 View가 어떤 종속성도 가지지 않았다는 것  
그래서 ViewModel을 다른 View에서도 활용이 가능

<img width="650" height= "400" src="https://github.com/wo9374/StudyProject/assets/55440637/965616c1-4428-4b5a-8563-d0ea4a38df3f">

장점
- View와 Model이 독립
- 바인딩하기 때문에 코드의 양이 감소
- ViewModel에서 View 코드가 없기 때문에 UnitTest를 쉽게 가능

단점
- 데이터 바인딩이 필수적으로 요구
- 간단한 UI에서 ViewModel을 설계해야 하고, 그만큼 과도한 엔지니어링이 일어날 수 있음
- 다양한 곳에서 많은 데이터를 받기 때문에 관리를 제대로 못 할 경우 버그가 발생

### AAC ViewModel?

> AndroidX에 있는 AAC ViewModel은 MVVM의 ViewModel과 다르다.<br>
> Android에서의 ViewModel은 Fragment와 Activity의 데이터 전달, 화면 전환 시 잃어버리는 데이터를 저장하는 저장소인 셈<br>
> LiveData와 StateFlow를 추가하여 MVVM Architecture를 구현한 것이기 때문에<br>
> 결론은 AAC ViewModel을 쓴다고 해서 MVVM을 사용한다고 할 수 없음<br>
> 마찬가지로 MVVM을 한다고 해서 AAC ViewModel을 사용할 필요는 없기에,<br>
> 어떤 기업은 AAC ViewModel을 사용하지 않고도 MVVM을 구현한다고 함<br>

***

### MVI

MVVM에서 해결하지 못하는 상태 문제와 부수 효과 문제가 제기됨

> 상태 문제<br>
> 프로그레스 바의 상태, 버튼 활성화 상태와 같이 화면에 나타나는 모든 정보를 상태라고 하는데,<br>
> 의도치 않은 방향으로 상태가 제어된다면 이것을 상태 문제라고 함<br>
>
> 부수 효과(side effect)<br>
> 원래의 목적과 다른 효과 또는 부작용이 발생하는 상태를 의미<br>
> 예를 들어 네트워크 통신 때 데이터를 가져오는 함수는 데이터를 못 가져올 수도 있다.<br>
> function add(a, b){a+b+c}는 a와 b를 더한 값의 예상과 다르게 c 때문에 값이 변경될 수도 있다.<br>

<img width="350" height= "700" src="https://github.com/wo9374/StudyProject/assets/55440637/560907ae-4d7b-4d01-97aa-5e7f4fb513c4">

데이터가 로딩되었음에도, 프로그레스 바가 돌고 있다.
MVVM으로 구현하면서 ViewModel에서 상태를 관리하게 되는데 여러 데이터를 분산하다 보니 관리 포인트가 불가피하게 늘어나고, 버그가 발생  

#### 데이터의 흐름을 제어하지 못하는 것이 문제였기 때문에 MVI는 단일 상태 관리와 단방향 데이터 흐름을 통해 MVVM의 문제점을 해결하고자 함

<img width="650" height= "400" src="https://github.com/wo9374/StudyProject/assets/55440637/343bfdc9-5b4c-4ff7-9f73-af2259ab6c80">

위 그림과 같이 단방향으로 흐르는 구조

Model
- 다른 Model과 다르게 상태(State)를 의미
- intent로 전달받은 객체에 맞추어 새로운 불변객체를 Model로 생성

View (UI)
- Model의 결과물인 상태를 구독하고 있다가 변경 시 UI 업데이트를 진행

Intent
- 앱의 상태를 바꾸려는 의도를 의미
- Model에게 앱의 상태를 전달  
&nbsp;

MVI에 따르면, Model에서 호출되는 값은 불변하기에 예상이 가능해야 함  
하지만 서버나 데이터베이스에서 값을 가져올 경우 이 값을 예측할 수 없음

MVI는 이러한 문제를 SideEffect를 통해 제어
> SideEffect<br>
> 백그라운드 작업, 액티비티 전환과 같은 부수적인 작업을 의미<br>
> Android 하단 알림 바 Toast또한 SideEffect로 보는 경우가 많다<br>

<img width="650" height= "400" src="https://github.com/wo9374/StudyProject/assets/55440637/225a3839-2c2e-45bf-8ea5-e732806b99e7">

MVI는 MVP, MVVM과 다르게 I가 실제 컴포넌트를 지칭하지 않음  
어떤 구조로 만들자는 의미 보다는 어떻게 데이터와 상태의 흐름을 어떻게 다룰 것이냐는 패러다임에 가까움  
그래서 MVI의 수많은 예제 코드를 보면 MVVM, MVP 기반으로 작성된 코드를 많이 볼 수 있음 (MVVM 기반으로 연습함)  

장점
- 하나의 State객체만을 바라보기 때문에 상태 충돌이 일어나기 어려움
- 쉬운 데이터의 흐름을 파악
- 불변객체이기 때문에 스레드에 대해 안전

단점
- 러닝 커브가 매우 높음 (기본적으로 MVP와 MVVM을 깔고 시작)
- Model 업데이트를 위해 새로운 인스턴트를 끼워 넣기 때문에 리소스가 낭비될 수 있음
- 작은 변경에도 intent를 거쳐야 함  
