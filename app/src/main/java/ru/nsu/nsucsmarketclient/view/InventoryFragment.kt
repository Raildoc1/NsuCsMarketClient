package ru.nsu.nsucsmarketclient.view

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.database.ImagesDao
import ru.nsu.nsucsmarketclient.databinding.FragmentInventoryBinding
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import ru.nsu.nsucsmarketclient.viewmodels.InventoryViewModel
import javax.inject.Inject

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val marketVM : InventoryViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: InventoryRecycleViewAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)

        recyclerView = binding.recycleView

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.isRefreshing = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
        marketVM.setInventoryRefreshedCallback { l ->
            run {
                onItemsReceived(l, view)
                swipeRefreshLayout.isRefreshing = false
            }
        }
        marketVM.forceUpdateInventory()
        marketVM.setWebErrorMessageHandler { s: String ->
            run {
                showErrorMessage(s, view)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            marketVM.forceUpdateInventory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(view: View) {
        recyclerView = binding.recycleView
        recyclerViewAdapter = InventoryRecycleViewAdapter { name : String, id : String -> showDialogMessage(name, id, view) }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun showDialogMessage(name: String, itemId: String, view: View) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)

        alert.setTitle(name)
        alert.setMessage(R.string.set_price_message)

        val input = EditText(view.context)
        alert.setView(input)

        alert.setPositiveButton(R.string.positive_answer) { _, _ ->
            val value: String = input.text.toString()

            try {
                val price = value.toLong()
                marketVM.addToSale(itemId, price)
                swipeRefreshLayout.isRefreshing = true
                marketVM.forceUpdate()
            } catch (e : Exception) { }
        }

        alert.setNegativeButton(R.string.cancel_answer) { _, _ -> }

        alert.show()
    }

    private fun onItemsReceived(items : List<InventoryItemModel>, view: View) {
        marketVM.updateItemsUrls(items) {
            recyclerViewAdapter.updateList(items)
        }
    }

    private fun showErrorMessage(message: String, view: View) {
        view.post {
            val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)

            alert.setTitle("Error :c")
            alert.setMessage(message)

            alert.setPositiveButton(R.string.cancel_answer) { _, _ -> }

            alert.show()
        }
    }
}