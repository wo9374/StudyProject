package com.ljb.unittest.local_1

import com.google.common.truth.Truth.assertThat
import com.ljb.unittest.local_1.MyCalc
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * 로컬 단위 테스트 (Android 와 관련 없이 JVM 만 필요한 테스트, 알고리즘 테스트 등등)
 *
 * (일반 클래스 + Junit4 + Truth)
 * */
class MyCalcTest {
    private lateinit var myCalc: MyCalc

    //Test 전 실행될 함수
    @Before
    fun setUp() {
        myCalc = MyCalc() //Test 수행할 Class
    }

    //Test 끝났을 때 실행될 함수
    @After
    fun afterTest(){

    }


    //원하는 함수 호출 후 Truth 테스팅 라이브러리의 assertThat(), isEqualTo()와 같은 함수를 활용하여 내가 원하는 값이 나오는지 테스트
    @Test
    fun calculateCircumference_correctResult() {
        val result = myCalc.calculateCircumference(2.1)

        //Use Truth
        assertThat(result).isEqualTo(13.188)

        //Use JUnit
        //Assert.assertEquals(13.188, result, 0.0) //기대값, 실제값, 오차 범위
    }

    @Test
    fun calculateCircumference_zeroRadius_correctResult() {
        val result = myCalc.calculateCircumference(0.0)
        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun calculateArea_correctResult() {
        val result = myCalc.calculateArea(2.1)
        assertThat(result).isEqualTo(13.8474)
    }

    @Test
    fun calculateArea_zeroRadius_correctResult() {
        val result = myCalc.calculateArea(0.0)
        assertThat(result).isEqualTo(0.0)
    }

    /**
     * UnitTest 에도 네이밍 컨벤션 존재
     * medium.com/@stefanovskyi/unit-test-naming-conventions-dd9208eadbea
     * */
}