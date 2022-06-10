package com.capstoneproject.noqapp.admin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.admin.model.DetailOrderModel
import com.capstoneproject.noqapp.databinding.ItemRowDetailOrderBinding
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

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
            val localeID = Locale("in", "ID")
            val nf: NumberFormat = NumberFormat.getInstance(localeID)
            val menuPrice = nf.format(order.menuPrice)
            val subTotal = nf.format(order.subtotal)

            binding.apply {
                "Menu Name : ${order.menuName}".also { tvMenuName.text = it }
                "Price : Rp. $menuPrice".also { tvPrice.text = it }
                "Quantity : ${order.amount}".also { tvQuantity.text = it }
                "SubTotal : Rp. $subTotal".also { tvSubtotalPrice.text = it }
            }
        }
    }

}