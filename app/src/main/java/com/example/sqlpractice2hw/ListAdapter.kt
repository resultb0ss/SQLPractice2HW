package com.example.sqlpractice2hw

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.sqlpractice2hw.databinding.ItemListViewBinding

class ListAdapter(context: Context, products: MutableList<Product>) :
    ArrayAdapter<Product>(context, R.layout.item_list_view, products) {

    private lateinit var binding: ItemListViewBinding

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val product = getItem(position)

        binding = if (convertView == null) {
            ItemListViewBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemListViewBinding.bind(convertView)
        }

        binding.itemListViewIdProductTV.text = "№ ${product?.id}"
        binding.itemListViewNameProductTV.text = product?.name
        binding.itemListViewWeightTV.text = "${product?.weight} кг."
        binding.itemListViewPriceTV.text = "${product?.price} руб."

        return binding.root
    }
}