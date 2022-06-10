package com.capstoneproject.noqapp.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.databinding.ItemRowMenuRecommendationBinding
import com.capstoneproject.noqapp.model.MenuModel
import java.text.NumberFormat
import java.util.*

class ListMenuRecommendAdapter :
    RecyclerView.Adapter<ListMenuRecommendAdapter.ListViewHolder>() {

    private var listMenu = ArrayList<MenuModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListMenu(menu: ArrayList<MenuModel>) {
        listMenu.clear()
        listMenu.addAll(menu)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowMenuRecommendationBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMenu[position])
    }

    override fun getItemCount(): Int = listMenu.size

    inner class ListViewHolder(private val binding: ItemRowMenuRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: MenuModel) {
            val localeID = Locale("in", "ID")
            val nf: NumberFormat = NumberFormat.getInstance(localeID)
            val priceMenu = nf.format(menu.price)

            binding.apply {
                Glide.with(itemView.context)
                    .load(menu.photoUrl)
                    .placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default)
                    .into(ivMenu)
                tvItemName.text = menu.name
                ("Rp. $priceMenu").also { tvItemPrice.text = it }
                ("Ordered ${menu.ordered} Times").also { tvItemOrdered.text = it }

            }
        }
    }

}