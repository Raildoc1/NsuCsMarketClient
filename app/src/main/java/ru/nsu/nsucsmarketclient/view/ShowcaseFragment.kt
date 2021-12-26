package ru.nsu.nsucsmarketclient.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.database.ImagesDao
import ru.nsu.nsucsmarketclient.databinding.FragmentShowcaseBinding
import ru.nsu.nsucsmarketclient.network.MarketRequest
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import ru.nsu.nsucsmarketclient.viewmodels.MarketItemsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ShowcaseFragment : Fragment() {

    private var _binding: FragmentShowcaseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val marketVM : MarketItemsViewModel by activityViewModels()

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
                onItemsReceived(l)
                swipeRefreshLayout.isRefreshing = false
            }
        }
        marketVM.forceUpdateShowcase()

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

    private fun onItemsReceived(items : List<ItemModel>) {
        recyclerViewAdapter.updateList(items)
        recyclerViewAdapter.notifyDataSetChanged()
    }
}