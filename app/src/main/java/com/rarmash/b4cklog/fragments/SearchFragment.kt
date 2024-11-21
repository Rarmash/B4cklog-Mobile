package com.rarmash.b4cklog.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.GridLayoutManager
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.adapters.GameAdapter
import com.rarmash.b4cklog.databinding.FragmentSearchBinding
import com.rarmash.b4cklog.network.RetrofitClient
import com.rarmash.b4cklog.paging.SearchPagingSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var gameAdapter: GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameAdapter = GameAdapter { gameId ->
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.searchFragment) {
                val bundle = Bundle().apply {
                    putInt("gameId", gameId)
                }
                navController.navigate(R.id.action_searchFragment_to_gameDetailFragment, bundle)
            }
        }

        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = gameAdapter

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchGames(it) }
                return true
            }
        })
    }

    private fun searchGames(query: String) {
        lifecycleScope.launch {
            Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = { SearchPagingSource(RetrofitClient.apiService, query) }
            ).flow.collectLatest { pagingData ->
                gameAdapter.submitData(pagingData)
            }
        }
    }
}