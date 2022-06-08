package com.capstoneproject.noqapp.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.noqapp.databinding.ItemRowUserOrderBinding
import com.capstoneproject.noqapp.model.MenuModel
import java.text.NumberFormat
import java.util.*

class ListUserOrderAdapter(private val listMenu: List<MenuModel>) :
    RecyclerView.Adapter<ListUserOrderAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ListUserOrderAdapter.ListViewHolder {
        val view =
            ItemRowUserOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListUserOrderAdapter.ListViewHolder, position: Int) {
        holder.bind(listMenu[position])
    }

    override fun getItemCount(): Int = listMenu.size

    inner class ListViewHolder(private val binding: ItemRowUserOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: MenuModel) {
            val localeID = Locale("in", "ID")
            val nf: NumberFormat = NumberFormat.getInstance(localeID)
            val priceMenu = nf.format(menu.price)

            binding.apply {
                Glide.with(itemView.context)
                    .load(menu.photoUrl)
                    .into(ivMenu)
                tvItemName.text = menu.name
                ("Rp. $priceMenu").also { tvItemPrice.text = it }
                tvItemQuantity.text = menu.totalInCart.toString()
            }
        }
    }
}