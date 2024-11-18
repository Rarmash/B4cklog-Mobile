package com.rarmash.b4cklog.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.rarmash.b4cklog.adapters.GameAdapter
import com.rarmash.b4cklog.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.rarmash.b4cklog.network.RetrofitClient
import com.rarmash.b4cklog.paging.GamePagingSource

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

        gameAdapter = GameAdapter()  // Используем адаптер для пагинации
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = gameAdapter

        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.layoutManager = layoutManager

        val pager = Pager(
            config = PagingConfig(pageSize = 18, enablePlaceholders = false),
            pagingSourceFactory = { GamePagingSource(RetrofitClient.apiService) }
        ).flow.cachedIn(lifecycleScope)

        lifecycleScope.launch {
            pager.collect { pagingData ->
                gameAdapter.submitData(pagingData)
            }
        }
    }
}
