package com.abdull.coroutinestutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_1 = "Result_1"
        private const val RESULT_2 = "Result_2"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_coroutine.setOnClickListener {
            text.text = "Clicked!"
            fakeApiRequest()
        }
    }

    private fun setTextView(input: String) {
        var result = text.text.toString() + "\n$input"
        text.text = result
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setTextView(input)
        }
    }

    private  fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val result1 = async {
                    println("debug: launching job1: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }.await()

                val result2 = async {
                    println("debug: launching job2: ${Thread.currentThread().name}")
                    getResult2FromApi(result1)
                }.await()

                println("debug: got result2 $result2")
            }
            println("debug: total execution time: $executionTime")
        }

    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(result1 : String): String {
        logThread("debug: getResult2FromApi")
        delay(1700)
        if(result1 == RESULT_1){
            return RESULT_2
        }
        throw CancellationException("Result #1 was incorrect!!")
    }

    private fun logThread(methodName: String) {
        println("debug: $methodName: Thread: ${Thread.currentThread().name}")
    }


}