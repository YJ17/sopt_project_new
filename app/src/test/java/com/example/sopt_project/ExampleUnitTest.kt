package com.example.sopt_project

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        println("Hello World")

        val a = 3
        val b = 5

        sumTestFunction(a,b)
    }

    fun sumTestFunction(a: Int, b: Int){
        println(a.toString() + "+" + b.toString()  + "=" + (a + b).toString())
    }
}
