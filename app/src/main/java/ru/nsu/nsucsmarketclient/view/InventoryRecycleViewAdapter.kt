package ru.nsu.nsucsmarketclient.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.database.AppDatabase
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import java.io.InputStream
import java.net.URL

class InventoryRecycleViewAdapter(private val onItemClick: (String, String) -> Unit) : RecyclerView.Adapter<InventoryRecycleViewAdapter.ViewHolder> () {

    private val dataSet = ArrayList<InventoryItemModel>()
    private lateinit var context : Context

    class ViewHolder(view: View, private val onItemClick: (String, String) -> Unit) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val icon: ImageView = view.findViewById(R.id.ivItem)

        fun bind(itemName: String, itemId: String) {
            itemView.setOnClickListener{ onItemClick(itemName, itemId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sale_item_row, parent, false)

        context = parent.context;

        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.name.text = item.market_hash_name
        holder.bind(item.market_hash_name, item.id)
        Thread {
            var d : Drawable? = try {
                val imagesDao = AppDatabase.getDatabase(context).imagesDao()
                val ref = imagesDao.findByName("${item.classid}_${item.instanceid}")
                var url = URL("https://steamcommunity-a.akamaihd.net/economy/image/${ref.ref}")
                var input : InputStream = url.openStream()
                Drawable.createFromStream(input, "steam")
            } catch (e : Exception) {
                Log.d("Database", "Failed to find ${item.classid}_${item.instanceid} -> ${e.message}");
                AppCompatResources.getDrawable(context, R.drawable.ic_baseline_photo_camera_24)
            }

            Handler(Looper.getMainLooper()).post {
                holder.icon.setImageDrawable(d)
            }
        }.start()
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