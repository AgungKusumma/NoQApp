package com.capstoneproject.noqapp.admin.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.admin.activity.OrderActivity
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.databinding.ItemRowOrderBinding

class ListOrderAdapter : RecyclerView.Adapter<ListOrderAdapter.ListViewHolder>() {

    private var listOrder = ArrayList<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListOrder(menu: ArrayList<Order>) {
        listOrder.clear()
        listOrder.addAll(menu)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listOrder[position])
    }

    override fun getItemCount(): Int = listOrder.size

    class ListViewHolder(private val binding: ItemRowOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                "Status : ${order.status}".also { tvItemStatus.text = it }
                "Table Code : ${order.tableId}".also { tvItemCode.text = it }
                "Total Price : Rp. ${order.totalPrice}".also { tvItemPrice.text = it }

                btnDetail.setOnClickListener {
                    val intent = Intent(itemView.context, OrderActivity::class.java)
                    intent.putExtra("Order", order.orderId)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}