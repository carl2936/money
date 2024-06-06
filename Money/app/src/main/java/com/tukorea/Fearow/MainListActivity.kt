package com.tukorea.Fearow

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainListActivity : AppCompatActivity() {
    var productList = arrayListOf<MainListAdapter.Product>() // Product 클래스를 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mainListView = findViewById<ListView>(R.id.mainListView)
        val productAdapter = MainListAdapter(this, productList)
        mainListView.adapter = productAdapter

        var productList = arrayListOf<MainListAdapter.Product>(
            MainListAdapter.Product("동", "정왕동", "4주","40,000", "data1"),
            MainListAdapter.Product("달러", "배곧동", "1개월","10,000", "data2"),
            MainListAdapter.Product("엔화", "거북섬", "3일", "70,000","data3"),
            MainListAdapter.Product("원화", "오이도", "5시간", "50,000","data4"))
    }
}
