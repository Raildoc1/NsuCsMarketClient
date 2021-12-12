package ru.nsu.nsucsmarketclient.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import ru.nsu.nsucsmarketclient.network.models.ItemModel

class InventoryRecycleViewAdapter : RecyclerView.Adapter<InventoryRecycleViewAdapter.ViewHolder> () {

    private val dataSet = ArrayList<InventoryItemModel>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sale_item_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataSet[position].market_hash_name;
    }

    override fun getItemCount() = dataSet.size;

    fun updateList(data: List<InventoryItemModel>) {
        dataSet.clear()

        data.forEach {
            if (it.tradable == 1L) {
                dataSet.add(it)
            }
        }

        notifyDataSetChanged()
    }
}