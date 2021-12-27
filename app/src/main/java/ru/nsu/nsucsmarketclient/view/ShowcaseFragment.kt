package ru.nsu.nsucsmarketclient.view

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.database.ImagesDao
import ru.nsu.nsucsmarketclient.databinding.FragmentShowcaseBinding
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import ru.nsu.nsucsmarketclient.viewmodels.InventoryViewModel
import ru.nsu.nsucsmarketclient.viewmodels.ShowcaseViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ShowcaseFragment : Fragment() {

    private var _binding: FragmentShowcaseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val marketVM : ShowcaseViewModel by activityViewModels()

    @Inject
    lateinit var imagesDao: ImagesDao

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: ShowcaseRecyclerViewAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowcaseBinding.inflate(inflater, container, false)

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.isRefreshing = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        marketVM.setShowcaseRefreshedCallback { l ->
            run {
                onItemsReceived(l, view)
                swipeRefreshLayout.isRefreshing = false
            }
        }
        marketVM.forceUpdateShowcase()

        marketVM.setWebErrorMessageHandler { s: String ->
            run {
                showErrorMessage(s, view)
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_open_inventory)
        }

        swipeRefreshLayout.setOnRefreshListener {
            marketVM.forceUpdateShowcase()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        recyclerView = binding.recycleView
        recyclerViewAdapter = ShowcaseRecyclerViewAdapter(imagesDao)
        recyclerView.setHasFixedSize(true)
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun onItemsReceived(items : List<ItemModel>, view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in items) {
                try {
                    val ref = imagesDao.findByName("${i.classid}_${i.instanceid}")
                    i.url = "https://steamcommunity-a.akamaihd.net/economy/image/${ref.ref}"
                } catch (e : Exception) {
                    i.url = "none"
                }
            }
            view.post {
                recyclerViewAdapter.updateList(items)
            }
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