package com.ljb.unittest.local_2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ljb.unittest.local_1.Calculations
import com.ljb.unittest.local_2.CalcViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * 로컬 단위 테스트 (Android 와 관련 없이 JVM 만 필요한 테스트, 알고리즘 테스트 등등)
 *
 * (ViewModel + LiveData + Mockito + Junit4 + Truth)
 * */
class CalcViewModelTest{
    private lateinit var calcViewModel: CalcViewModel
    private lateinit var calculations: Calculations

    /**
     * InstantTaskExecutorRule
     *
     * 모든 작업들을 동기적(synchronous)하게 해주어 동기화에 신경쓰지 않게 해주어 좋고 LiveData 테스트시 필수로 사용 된다고 생각
     * */
    @get:Rule //규칙 추가
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    //백그라운드 작업과 연관된 모든 아키텍처 컴포넌트들을 같은(한개의) 스레드에서 실행되게 해서 테스트 결과들이 동기적으로 실행 되게 함



    /**
     * Mockito는 Test Double(여기서 Double은 대역이란 뜻) 중 하나이며
     * Mock은 호출에 대한 기대를 명세하고, 해당 내용에 따라 동작하도록 프로그래밍 된 객체
     *
     * 필요한 객체를 Mocking해서 이 객체의 어떤 함수를 호출하면 이런 것들을 반환하게 할거다 정할 수 있다 (파라미터나 리턴 값의 타입은 맞춰 주기)
     * */
    @Before
    fun setUp(){
        calculations = mock(Calculations::class.java)   //내가 Mocking 할 클래스를 먼저 mock()
        `when`(calculations.calculateArea(2.1)).thenReturn(13.8474)         //Mocking한 클래스를 `when`()안에 실행할 함수를 호출
        `when`(calculations.calculateCircumference(1.0)).thenReturn(6.28)   //thenReturn()에는 내가 `when`과 똑같은 함수 호출시 반환할 값을 명시
        //thenAnswer, thenThrow 등등 여러 메소드 제공
        //이때 Mockito에는 보통 구현객체가 아닌 인터페이스를 주로 사용 / 객체를 Mockito when에 넣으면 에러 난다고 함

        calcViewModel = CalcViewModel(calculations)
    }

    @Test
    fun calculateArea_radiusSent_updateLiveData(){
        calcViewModel.calculateArea(2.1)
        val result = calcViewModel.areaValue.value
        assertThat(result).isEqualTo("13.8474")
    }

    @Test
    fun calculateCircumference_radiusSent_updateLiveData(){
        calcViewModel.calculateCircumference(1.0)
        val result = calcViewModel.circumferenceValue.value
        assertThat(result).isEqualTo("6.28")
    }
}