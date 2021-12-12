package ru.nsu.nsucsmarketclient.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.network.models.ItemModel

class RecycleViewAdapter : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> () {

    private val dataSet = ArrayList<ItemModel>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName);
        val price: TextView = view.findViewById(R.id.tvPrice);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sale_item_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataSet[position].market_hash_name;
        holder.price.text = dataSet[position].price.toString();
    }

    override fun getItemCount() = dataSet.size;

    fun updateList(data: List<ItemModel>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }
}