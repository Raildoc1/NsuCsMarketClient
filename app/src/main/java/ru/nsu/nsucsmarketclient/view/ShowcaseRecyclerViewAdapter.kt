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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import ru.nsu.nsucsmarketclient.database.ImagesDao
import java.io.InputStream
import java.lang.NullPointerException
import java.net.URL

class ShowcaseRecyclerViewAdapter(private val imagesDao : ImagesDao) : RecyclerView.Adapter<ShowcaseRecyclerViewAdapter.ViewHolder> () {

    private val dataSet = ArrayList<ItemModel>()
    private lateinit var context : Context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val icon: ImageView = view.findViewById(R.id.ivItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sale_item_row, parent, false)

        context = parent.context;

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = dataSet[position]

        holder.name.text = item.market_hash_name
        holder.price.text = item.price.toString()
        
        if(item.url == "none") {
            Picasso.with(context)
                .load(R.drawable.ic_baseline_photo_camera_24)
                .into(holder.icon)
        }

        Picasso.with(context)
            .load(item.url)
            .error(R.drawable.ic_baseline_photo_camera_24)
            .into(holder.icon)
    }

    override fun getItemCount() = dataSet.size;

    fun updateList(data: List<ItemModel>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }
}