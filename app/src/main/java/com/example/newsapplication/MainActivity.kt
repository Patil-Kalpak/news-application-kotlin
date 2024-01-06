package com.example.newsapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(),NewsItemClicked {

    private lateinit var mAdapter : NewsListAdapter

    var url  = "https://saurav.tech/NewsAPI/everything/cnn.json"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetch()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter

         var businessButton : Button = findViewById(R.id.button1)
         var healthButton : Button = findViewById(R.id.button2)
         var technologyButton : Button = findViewById(R.id.button3)

        businessButton.setOnClickListener(){
            url = "https://saurav.tech/NewsAPI/top-headlines/category/business/in.json"
            fetch()
        }

        healthButton.setOnClickListener()
        {
            url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
            fetch()
        }


        technologyButton.setOnClickListener()
        {
            url = "https://saurav.tech/NewsAPI/top-headlines/category/technology/in.json"
            fetch()
        }


    }

    fun fetch() {

        val JSON_request = JsonObjectRequest(
            Request.Method.GET, url,null,
           Response.Listener{

               var newsJsonArray = it.getJSONArray("articles")
               val newsArray = ArrayList<News>()
               for(i in 0 until newsJsonArray.length())
               {
                   val newsjsonobject = newsJsonArray.getJSONObject(i)
                   val news = News(
                       newsjsonobject.getString("title"),
                       newsjsonobject.getString("author"),
                       newsjsonobject.getString("url"),
                       newsjsonobject.getString("urlToImage")
                   )
                   newsArray.add(news)
               }
               mAdapter.updateNews(newsArray)

            }
        ) {

            Log.e("here is error", "here is error")
        }


        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(JSON_request)



    }
    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val csIntent = builder.build()
        csIntent.launchUrl(this, Uri.parse(item.url))


    }



}