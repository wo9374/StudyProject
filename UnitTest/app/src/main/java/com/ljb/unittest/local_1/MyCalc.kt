package com.ljb.unittest.local_1


interface Calculations {
    fun calculateCircumference(radius: Double) : Double //원 둘레
    fun calculateArea(radius: Double): Double           //면적
}
class MyCalc : Calculations {
    private val pi = 3.14

    override fun calculateCircumference(radius: Double): Double =
        2 * pi * radius

    override fun calculateArea(radius: Double): Double =
        pi * radius *radius
}