package com.abdull.coroutinestutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            //Scopes: IO, Main, Default
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
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

    private suspend fun fakeApiRequest() {
        val result1 = getResult1FromApi()
        setTextOnMainThread(result1)

        val result2 = getResult2FromApi()
        setTextOnMainThread(result2)

        println("debug: $result1")
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("debug: getResult2FromApi")
        delay(5000)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("debug: $methodName: Thread: ${Thread.currentThread().name}")
    }


}