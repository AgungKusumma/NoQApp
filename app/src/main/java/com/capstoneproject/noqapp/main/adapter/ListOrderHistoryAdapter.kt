package com.capstoneproject.noqapp.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.databinding.ItemRowOrderBinding
import com.capstoneproject.noqapp.main.activity.DetailHistoryActivity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ListOrderHistoryAdapter : RecyclerView.Adapter<ListOrderHistoryAdapter.ListViewHolder>() {

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
            val localeID = Locale("in", "ID")
            val nf: NumberFormat = NumberFormat.getInstance(localeID)
            val totalPrice = nf.format(order.totalPrice)

            binding.apply {
                "Status : ${order.status}".also { tvItemStatus.text = it }
                "Table Code : ${order.tableId}".also { tvItemCode.text = it }
                "Total Price : Rp. $totalPrice".also { tvItemPrice.text = it }

                btnDetail.setOnClickListener {
                    val intent = Intent(itemView.context, DetailHistoryActivity::class.java)
                    intent.putExtra("Order", order.orderId)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}