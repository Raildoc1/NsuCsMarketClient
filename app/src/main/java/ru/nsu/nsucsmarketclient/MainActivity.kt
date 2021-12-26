package ru.nsu.nsucsmarketclient

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.nsu.nsucsmarketclient.databinding.ActivityMainBinding
import ru.nsu.nsucsmarketclient.viewmodels.MarketItemsViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val marketVM : MarketItemsViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        marketVM.setConnectionListeners()
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

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var recyclerViewAdapter: RecycleViewAdapter
//    private lateinit var connection: MarketConnectionHandler
//    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        initConnection()
//        initRecyclerView()
//        initRefresh()
//        initFAB()
//        fillTestDatabase()
//    }
//
//    private fun initRefresh() {
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
//        swipeRefreshLayout.setOnRefreshListener {
//            connection.updateSaleItems()
//        }
//    }
//
//    private fun initConnection() {
//        connection = MarketConnectionHandler()
//        connection.connect(BuildConfig.MCS_KEY)
//        connection.setOnItemsReceivedListener { l -> setList(l); swipeRefreshLayout.isRefreshing = false }
//    }
//
//    private fun initRecyclerView() {
//        recyclerView = findViewById(R.id.recycleView)
//        recyclerViewAdapter = RecycleViewAdapter()
//        recyclerView.setHasFixedSize(true)
//        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.adapter = recyclerViewAdapter
//    }
//
//    private fun initFAB() {
//        var fab = findViewById<FloatingActionButton>(R.id.fab)
//        fab.setOnClickListener {
//            var intent = Intent(this, InventoryActivity::class.java)
//            connection.stop()
//            startActivity(intent)
//        }
//    }
//
//    private fun fillTestDatabase() {
//        lifecycleScope.launch (Dispatchers.IO) {
//            val imagesDao = AppDatabase.getDatabase(application).imagesDao()
//
//            imagesDao.insertAll(
//                ImageRef(
//                    "4656080287_519977179",
//                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpopuP1FA957PfMYTxW09izh4WZg8j5Nr_Yg2Yf68Qh3uuZpI_w0VC1-BFlNj-iI9SUIQBvZl2Bq1G6w-vv0Z7qvJ_Bm2wj5HfXJQyDPg"
//                ),
//                ImageRef(
//                    "4571592871_519977179",
//                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpopbuyLgNv1fX3eSR96NmlkZKfqPX4PLTcqWZU7Mxkh6eQ89Wt0Qbj_Es-YG76IoHBcwZqaQ2E_VK8lLrugpK76J-awSRgvSV0-z-DyE7TLl_x"
//                ),
//                ImageRef(
//                    "4571593888_519977179",
//                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou6ryFBRw7OnNcy9D7927q5KOk8j5Nr_Yg2Yf7ZIh0u-Q89rzi1fgqRJrYWnxdYeccQc6YlqB-VfswO_njMe5vZubzGwj5Hfi35zU2w"
//                ),
//                ImageRef(
//                    "3106076656_0",
//                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXU5A1PIYQNqhpOSV-fRPasw8rsUFJ5KBFZv668FFUxnaPLJz5H74y1xtTcz6etNumIx29U6Zd3j7yQoYih3lG1-UJqY27xJIeLMlhpaD9Aclo"
//                ),
//                ImageRef(
//                    "1989281736_302028390",
//                    "IzMF03bi9WpSBq-S-ekoE33L-iLqGFHVaU25ZzQNQcXdB2ozio1RrlIWFK3UfvMYB8UsvjiMXojflsZalyxSh31CIyHz2GZ-KuFpPsrTzBG0pPSEEEvycTKKfXSJTA88RLBYZm_d-Df2s7udQ2ydQLl5S18FL_BSp2wca8vca0E5hZlLpWL-lEtxEQQlZ8lSeR-30ylKNehznyD_8PAlXw"
//                )
//            )
//        }
//    }
//
//    private fun setList(items : List<ItemModel>) {
//        recyclerViewAdapter.updateList(items)
//        recyclerViewAdapter.notifyDataSetChanged()
//    }
}