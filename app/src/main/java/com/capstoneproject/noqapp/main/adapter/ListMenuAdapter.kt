package com.capstoneproject.noqapp.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.noqapp.databinding.ItemRowMenuBinding
import com.capstoneproject.noqapp.model.ItemMenu

class ListMenuAdapter(private val listMenu: ArrayList<ItemMenu>, val clickListener: MenuList) :
    RecyclerView.Adapter<ListMenuAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMenu[position])
    }

    override fun getItemCount(): Int = listMenu.size

    inner class ListViewHolder(private val binding: ItemRowMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: ItemMenu) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(menu.photo)
                    .into(ivMenu)
                tvItemName.text = menu.name
                "Rp. ${menu.price}".also { tvItemPrice.text = it }

                btnAddMenu.setOnClickListener {
                    menu.totalInCart = 1
                    clickListener.addMenu(menu)
                    tvCount.text = menu.totalInCart.toString()
                    btnAddMenu.isVisible = false
                    addMenuLayout.isVisible = true
                }

                imgAdd.setOnClickListener {
                    var total = menu.totalInCart
                    total++
                    menu.totalInCart = total
                    clickListener.updateMenu(menu)
                    tvCount.text = total.toString()
                }

                imgMin.setOnClickListener {
                    var total: Int = menu.totalInCart
                    total--
                    if (total > 0) {
                        menu.totalInCart = total
                        clickListener.updateMenu(menu)
                        tvCount.text = menu.totalInCart.toString()
                    } else {
                        menu.totalInCart = total
                        clickListener.removeMenu(menu)
                        btnAddMenu.isVisible = true
                        addMenuLayout.isVisible = false
                    }
                }
            }
        }
    }

    interface MenuList {
        fun addMenu(menu: ItemMenu)
        fun updateMenu(menu: ItemMenu)
        fun removeMenu(menu: ItemMenu)
    }

}