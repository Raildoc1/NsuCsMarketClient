package ru.nsu.nsucsmarketclient

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.nsu.nsucsmarketclient.network.MarketConnectionHandler
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import ru.nsu.nsucsmarketclient.view.InventoryRecycleViewAdapter

import android.widget.EditText
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class InventoryActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: InventoryRecycleViewAdapter
    private lateinit var connection: MarketConnectionHandler
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        initConnection()
        initRecyclerView()
        initRefresh()
        initFAB()
    }

    private fun initRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            connection.updateInventoryItems()
        }
    }

    private fun initConnection() {
        connection = MarketConnectionHandler()
        connection.connect(BuildConfig.MCS_KEY)
        connection.setOnInventoryReceivedListener { l -> setList(l); swipeRefreshLayout.isRefreshing = false }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerViewAdapter = InventoryRecycleViewAdapter { name: String, itemId: String ->
            val alert: AlertDialog.Builder = AlertDialog.Builder(this)

            alert.setTitle(name)
            alert.setMessage("Set price (RUB):")

            val input = EditText(this)
            alert.setView(input)

            alert.setPositiveButton("Ok") { dialog, whichButton ->
                val value: String = input.text.toString()

                try {
                    val price = value.toLong()
                    connection.addToSale(itemId, price)
                } catch (e : Exception) { }
            }

            alert.setNegativeButton("Cancel") { _, _ -> }

            alert.show()
        }

        recyclerView.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun initFAB() {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            connection.stop()
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

    private fun setList(items : List<InventoryItemModel>) {
        recyclerViewAdapter.updateList(items)
        recyclerViewAdapter.notifyDataSetChanged()
    }
}