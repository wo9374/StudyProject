# UnitTest

로컬단위 테스트(test) / JVM에서 실행되는 테스트 / Android Framework가 필요없음
1. local_1 (일반 클래스 + Junit4 + Truth)
2. local_2 (ViewModel + LiveData + Mockito + Junit4 + Truth)
3. local_3 (ViewModel + LiveData + 비동기로 데이터 불러오는 함수 테스트 + getOrAwaitValue() + Mockito + Junit4 + Truth)

계측테스트(androidTest) / 안드로이드 종속
1. android_1 / Room Unit Test (ViewModel + LiveData + Mockito + Junit4)


# Reference

프로젝트 예제
https://youngest-programming.tistory.com/492

DI를 사용한 Room DAO Unit Test
modelmaker.tistory.com/5

Espresso를 사용한 UI 유닛테스트
youngest-programming.tistory.com/370?category=982808

