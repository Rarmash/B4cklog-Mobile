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
import androidx.paging.cachedIn
import androidx.recyclerview.widget.GridLayoutManager
import com.rarmash.b4cklog.R
import com.rarmash.b4cklog.adapters.GameAdapter
import com.rarmash.b4cklog.databinding.FragmentHomeBinding
import com.rarmash.b4cklog.network.RetrofitClient
import com.rarmash.b4cklog.paging.GamePagingSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var gameAdapter: GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameAdapter = GameAdapter { gameId ->
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.homeFragment) {
                val bundle = Bundle().apply {
                    putInt("gameId", gameId)
                }
                navController.navigate(R.id.action_homeFragment_to_gameDetailFragment, bundle)
            }
        }

        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = gameAdapter

        val pager = Pager(
            config = PagingConfig(pageSize = 18, enablePlaceholders = false),
            pagingSourceFactory = { GamePagingSource(RetrofitClient.apiService) }
        ).flow.cachedIn(lifecycleScope)

        lifecycleScope.launch {
            pager.collectLatest { pagingData ->
                gameAdapter.submitData(pagingData)
            }
        }
    }
}
