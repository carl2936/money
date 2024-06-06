package com.tukorea.Fearow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class MainListAdapter(val context: Context, val productList: ArrayList<Product>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_home, null)

        val Photo = view.findViewById<ImageView>(R.id.PhotoImg)
        val Title = view.findViewById<TextView>(R.id.TitleTv)
        val Location = view.findViewById<TextView>(R.id.LocationTv)
        val Date = view.findViewById<TextView>(R.id.DateTv)
        val Price = view.findViewById<TextView>(R.id.PriceTv)

        val product = productList[position]
        val resourceId = context.resources.getIdentifier(product.Photo, "drawable", context.packageName)
        Photo.setImageResource(resourceId)
        Title.text = product.Title
        Location.text = product.Location
        Date.text = product.Date
        Price.text = product.Price

        return view
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return productList.size
    }

    data class Product(val Title: String, val Location: String, val Date: String, val Price: String, val Photo: String)
}
