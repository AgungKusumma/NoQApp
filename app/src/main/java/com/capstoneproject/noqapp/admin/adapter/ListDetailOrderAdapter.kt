package com.capstoneproject.noqapp.admin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.admin.model.DetailOrderModel
import com.capstoneproject.noqapp.databinding.ItemRowDetailOrderBinding

class ListDetailOrderAdapter : RecyclerView.Adapter<ListDetailOrderAdapter.ListViewHolder>() {

    private var listOrder = ArrayList<DetailOrderModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListOrder(menu: ArrayList<DetailOrderModel>) {
        listOrder.clear()
        listOrder.addAll(menu)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDetailOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listOrder[position])
    }

    override fun getItemCount(): Int = listOrder.size

    class ListViewHolder(private val binding: ItemRowDetailOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: DetailOrderModel) {
            binding.apply {
                "Menu ID : ${order.menuId}".also { tvIdMenu.text = it }
                "Price : Rp. ${order.menuPrice}".also { tvPrice.text = it }
                "Quantity : ${order.amount}".also { tvQuantity.text = it }
                "SubTotal : Rp. ${order.subtotal}".also { tvSubtotalPrice.text = it }
            }
        }
    }

}