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
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import ru.nsu.nsucsmarketclient.database.AppDatabase
import java.io.InputStream
import java.net.URL

class RecycleViewAdapter : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> () {

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

    fun updateList(data: List<ItemModel>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }
}