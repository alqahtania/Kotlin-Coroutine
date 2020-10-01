package com.abdull.coroutinestutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_coroutine.setOnClickListener {
            text.text = "Clicked!"
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest() {
        CoroutineScope(Main).launch {
            println("Launching the job on thread: ${Thread.currentThread().name}")
            val result1 = getResult()
            println("debug: Result1 is $result1")
            val result2 = getResult()
            println("debug: Result2 is $result2")
            val result3 = getResult()
            println("debug: Result3 is $result3")
            val result4 = getResult()
            println("debug: Result4 is $result4")
            val result5 = getResult()
            println("debug: Result5 is $result5")

        }

        CoroutineScope(Main).launch {
            delay(1000)
            //runBlocking will block the entire thread until it finishes leading the results above to suspend
            runBlocking {
                println("Blocking thread: ${Thread.currentThread().name}")
                delay(5000)
                println("Done blocking thread ${Thread.currentThread().name}")
            }
        }
    }


    private suspend fun getResult() : Int{
//        println("debug: getResult() running in thread ${Thread.currentThread().name}")
        delay(1000)
        return Random.nextInt(0, 11)

    }


}