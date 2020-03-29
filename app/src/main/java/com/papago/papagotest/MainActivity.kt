package com.papago.papagotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val api = NaverApi.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }
    private fun init() {
        btn.setOnClickListener {
            var translateWord = et_query.text.toString()
            if(translateWord != "") {
                val callPostTransferPapago = api.transferPapago(
                    "ko", "en", translateWord
                )

                callPostTransferPapago.enqueue(object : Callback<NaverPapagoResponse> {
                    override fun onResponse(
                        call: Call<NaverPapagoResponse>,
                        response: Response<NaverPapagoResponse>
                    ) {
                        tv_output.text = response.body()?.message?.result?.translatedText
                        Log.d("tag", "성공 : ${response.raw()}")
                    }

                    override fun onFailure(call: Call<NaverPapagoResponse>, t: Throwable) {
                        Log.d("tag", "실패 : $t")
                    }
                })
            }else {
                Toast.makeText(applicationContext, "번역할 단어가 없습니다. 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}