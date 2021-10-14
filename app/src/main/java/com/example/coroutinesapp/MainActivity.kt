package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var tvadvice: TextView
    private lateinit var advicebtn: Button

    val adviceUrl = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvadvice = findViewById(R.id.tv)
        advicebtn = findViewById(R.id.btn)

        advicebtn.setOnClickListener(){

            requestApi()

        }

    }
    private fun requestApi() {

        CoroutineScope(Dispatchers.IO).launch {

            val data = async {

                fetchRandomAdvice()

            }.await()

            if (data.isNotEmpty())
            {

                updateAdviceText(data)
            }

        }

    }

    private fun fetchRandomAdvice():String{

        var response=""
        try {
            response = URL(adviceUrl).readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")

        }
        return response

    }

    private suspend fun updateAdviceText(data:String) {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            tvadvice.text = advice

        }

    }
}