package com.example.weatherappsync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 전체주소 - https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=Asw3z8UHmL0yTDaGu0zke%2FR5RbLXMQPiDVfqUqcioOOYpM2JVWx9Dzat3YOBZM3ba5JoUWTP47DrZR4ZmISeag%3D%3D&pageNo=1&numOfRows=10&dataType=JSON&base_date=20230126&base_time=0500&nx=55&ny=127
// 인증키 - Asw3z8UHmL0yTDaGu0zke%2FR5RbLXMQPiDVfqUqcioOOYpM2JVWx9Dzat3YOBZM3ba5JoUWTP47DrZR4ZmISeag%3D%3D
// BASE_URL - https://apis.data.go.kr/1360000/VilageFcstInfoService/
// @GET - getVilageFcst?serviceKey=Asw3z8UHmL0yTDaGu0zke%2FR5RbLXMQPiDVfqUqcioOOYpM2JVWx9Dzat3YOBZM3ba5JoUWTP47DrZR4ZmISeag%3D%3D&pageNo=1&numOfRows=10&dataType=JSON&base_date=20230126&base_time=0500&nx=55&ny=127

val data_type = "JSON"
val num_of_rows = 10
val page_no = 1
val base_date = 20230126
val base_time = 1100
val nx = "55"
val ny = "127"

data class WEATHER (
    val response : RESPONSE
)
data class RESPONSE (
    val header : HEADER,
    val body : BODY
)
data class HEADER(
    val resultCode : Int,
    val resultMsg : String
)
data class BODY(
    val dataType : String,
    val items : ITEMS
)
data class ITEMS(
    val item : List<ITEM>
)
data class ITEM(
    val baseData : Int,
    val baseTime : Int,
    val category : String
)

interface WeatherInterface {
    @GET("getVilageFcst?Asw3z8UHmL0yTDaGu0zke%2FR5RbLXMQPiDVfqUqcioOOYpM2JVWx9Dzat3YOBZM3ba5JoUWTP47DrZR4ZmISeag%3D%3D")
    fun getWeather(
        @Query("dataType") dataType: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("baseDate") baseDate: Int,
        @Query("baseTime") baseTime: Int,
        @Query("nx") nx: String,
        @Query("ny") ny: String
    ) : Call<WEATHER>
}

private val retrofit = Retrofit.Builder()
    .baseUrl("https://apis.data.go.kr/1360000/VilageFcstInfoService/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object ApiObject {
    val retrofitService : WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val call = ApiObject.retrofitService.getWeather(
            data_type,
            num_of_rows,
            page_no,
            base_date,
            base_time,
            nx,
            ny
        )
        call.enqueue(object : Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    Log.d("api1", response.body().toString())
                    Log.d("api2", response.body()!!.response.body.items.item.toString())
                    Log.d("api3", response.body()!!.response.body.items.item[0].category)
                }
            }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api4 : ", t.toString())
            }
        })
    }
}