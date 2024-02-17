package ru.sample.duckapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sample.duckapp.data.DucksApi
import ru.sample.duckapp.domain.Duck


class Ducks1Activity : AppCompatActivity(){

    // Создайте экземпляр объекта Retrofit с базовым URL
    val retrofit = Retrofit.Builder()
        .baseUrl("https://random-d.uk/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val ducksApi = retrofit.create(DucksApi::class.java)

    fun changeImageDuck(url: String) {
        val img = findViewById<ImageView>(R.id.duckImage)
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.stock_duck) // Опционально: изображение, которое будет отображаться во время загрузки
            .error(R.drawable.stock_duck) // Опционально: изображение, которое будет отображаться в случае ошибки загрузки
            .into(img)
        val number_text = findViewById<TextView>(R.id.duck_id)
        number_text.setText(url.split("/").last().split(".").first())

    }

    fun changeDuck(){
        val img = findViewById<ImageView>(R.id.duckImage)
        val new_duck= ducksApi.getRandomDuck()
        // Вы можете асинхронно выполнить запрос
        new_duck.enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful) {
                    val duck: Duck? = response.body()
                    if (duck != null) {
                        changeImageDuck(duck.url)
                    }
                } else {
                    // Обработайте неуспешный запрос
                }
            }

            override fun onFailure(call: Call<Duck>, t: Throwable) {
                // Обработайте ошибку
            }
        })
    }


    fun changeConcrentDuck(id: String) {
        val url = "https://random-d.uk/api/v2/" + id + ".jpg"
        changeImageDuck(url)
        return
        val retrofit = Retrofit.Builder()
            .baseUrl("https://random-d.uk/api/v2/") // Замените на ваш базовый URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ducksApi = retrofit.create(DucksApi::class.java)

        val img = findViewById<ImageView>(R.id.duckImage)
        img.setImageResource(R.drawable.stock_duck)
        val call = ducksApi.getConcrentDuck(id) // Здесь 5 будет подставлено вместо {num}

// Выполнение асинхронного запроса
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val bitmap = BitmapFactory.decodeStream(responseBody.byteStream())
                        img.setImageBitmap(bitmap)
                    }
                } else {
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Обработка ошибки
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ducks1_activity)
        changeDuck()
    }

    fun onDucksNext1ButtonClick(view: android.view.View) {
        val id = findViewById<android.widget.EditText>(R.id.duckNumber)
        if (!id.text.isEmpty()) {
            try {
                id.text.toString().toInt()
                changeConcrentDuck(id.text.toString())
                id.setText("")
            } catch (e: NumberFormatException) {
                id.setText("")
                val text = "Сюда можно вводить только целые числа!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
                changeDuck()
            }

        }

        else {
            changeDuck()
        }
    }

}