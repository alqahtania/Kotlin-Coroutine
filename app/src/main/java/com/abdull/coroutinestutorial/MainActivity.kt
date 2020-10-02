package com.abdull.coroutinestutorial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_coroutine.setOnClickListener{
            main()
        }


    }


    val handler = CoroutineExceptionHandler{_, exception ->
        println("Exception thrown in one of the children: $exception") }

    fun main(){
        val parentJob = CoroutineScope(IO).launch(handler) {
            val jobA = launch {
                val resultA = getResult(1)
                println("resultA $resultA")
            }
            jobA.invokeOnCompletion {
                t ->
                if(t != null){
                    println("Error getting resultA: $t")
                }
            }
            val jobB = launch {
                val resultB = getResult(2)
                println("resultB $resultB")
            }
            jobB.invokeOnCompletion {
                t ->
                if(t != null){
                    println("Error getting resultB: $t")
                }
            }
            val jobC = launch {
                val resultC = getResult(3)
                println("resultC $resultC")
            }
            jobC.invokeOnCompletion {
                t ->
                if(t != null){
                    println("Error getting resultC: $t")
                }
            }

        }
        parentJob.invokeOnCompletion { t ->
            if(t != null){
                println("Parent job failed: $t")
            }
            else{
                println("Parent job SUCCESS")
            }
        }
    }



    suspend fun getResult(number : Int) : Int{
        println("Result number $number running on thread: ${Thread.currentThread().name}")
        delay(number*500L)
        if(number == 2){
//            throw Exception("Error getting result for number $number")
//            cancel(CancellationException("Error getting result for number $number")) // calling cancel from the suspend method won't do anything
            throw CancellationException("Error getting result for number $number") //this is equivalent to calling cancel() on the job its self
        }
        return number*2
    }

    private fun println(message: String){
        Log.d(TAG, " $message")
    }



}