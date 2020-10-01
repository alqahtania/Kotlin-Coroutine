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
            setTextView("Clicked!")

//            fakeApiRequest()
            fakeApiRequestAsyncAwait()
        }
    }

    private fun fakeApiRequest() {
        val startTime = System.currentTimeMillis()
        val parentJob = CoroutineScope(IO).launch {
            val job1 = launch {
                val time1 = measureTimeMillis {
                    println("debug: launching job1 in thread ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread("Got $result1")
                }
                println("debug: complete job1 in $time1")
            }
//            job1.join() // this will make them launch sequentially. If not added they will launch concurrently

            val job2 = launch {
                val time2 = measureTimeMillis {
                    println("debug: launching job2 in thread ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread("Got $result2")
                }
                println("debug: complete job2 in $time2")
            }
        }
        parentJob.invokeOnCompletion {
            println("debug: complete parent job in ${System.currentTimeMillis() - startTime}")
        }

    }

    private fun fakeApiRequestAsyncAwait(){
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val result1: Deferred<String> = async {
                    println("debug: launching result1 current thread: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }

                val result2: Deferred<String> = async {
                    println("debug: launching result2 current thread: ${Thread.currentThread().name}")
                    getResult2FromApi()
                }

                setTextOnMainThread("Got ${result1.await()}") // await() waits for result1 async to return the string
                setTextOnMainThread("Got ${result2.await()}")
            }
            println("debug: total execution time: $executionTime")
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

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("debug: getResult2FromApi")
        delay(1700)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("debug: $methodName: Thread: ${Thread.currentThread().name}")
    }


}