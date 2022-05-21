package com.capstoneproject.noqapp.admin.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.admin.activity.OrderActivity
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.databinding.ItemRowOrderBinding

class ListOrderAdapter(private val listOrder: ArrayList<Order>) :
    RecyclerView.Adapter<ListOrderAdapter.ListViewHolder>() {

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
                tvItemTimeStamp.text = order.timeStamp
                tvItemName.text = order.username
                "Table Code : ${order.tableCode}".also { tvItemCode.text = it }
                "Total Price : Rp. ${order.price}".also { tvItemPrice.text = it }

                btnDetail.setOnClickListener {
                    val intent = Intent(itemView.context, OrderActivity::class.java)
                    intent.putExtra("Order", order)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(tvItemTimeStamp, "time"),
                            Pair(tvItemCode, "code"),
                            Pair(tvItemName, "name"),
                            Pair(tvItemOrder, "list"),
                            Pair(tvItemPrice, "price")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

}