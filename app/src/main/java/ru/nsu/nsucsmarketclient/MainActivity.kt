package ru.nsu.nsucsmarketclient

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.nsucsmarketclient.network.MarketConnectionHandler
import ru.nsu.nsucsmarketclient.network.MarketRequest
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import ru.nsu.nsucsmarketclient.view.RecycleViewAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycleView)
        recyclerViewAdapter = RecycleViewAdapter()

        recyclerView.setHasFixedSize(true)
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerViewAdapter

        var connection = MarketConnectionHandler()
        connection.connect(BuildConfig.MCS_KEY)
        connection.setOnItemsReceivedListener { l -> setList(l) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setList(items : List<ItemModel>) {
        recyclerViewAdapter.updateList(items)
        recyclerViewAdapter.notifyDataSetChanged()
    }
}