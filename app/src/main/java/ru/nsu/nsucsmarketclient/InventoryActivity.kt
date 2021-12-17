package ru.nsu.nsucsmarketclient

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            alert.setMessage(R.string.set_price_message)

            val input = EditText(this)
            alert.setView(input)

            alert.setPositiveButton(R.string.positive_answer) { _, _ ->
                val value: String = input.text.toString()

                try {
                    val price = value.toLong()
                    connection.addToSale(itemId, price)
                    swipeRefreshLayout.isRefreshing = true
                    connection.updateInventoryItems()
                } catch (e : Exception) { }
            }

            alert.setNegativeButton(R.string.cancel_answer) { _, _ -> }

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

    private fun setList(items : List<InventoryItemModel>) {
        recyclerViewAdapter.updateList(items)
        recyclerViewAdapter.notifyDataSetChanged()
    }
}