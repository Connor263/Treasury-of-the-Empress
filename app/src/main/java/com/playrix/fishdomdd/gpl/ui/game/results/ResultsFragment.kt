package com.playrix.fishdomdd.gpl.ui.game.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.playrix.fishdomdd.gpl.adapter.ResultsAdapter
import com.playrix.fishdomdd.gpl.databinding.FragmentResultsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsFragment : Fragment() {
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: ResultsAdapter

    private val viewModel: ResultsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ResultsAdapter()
        initRv()

        viewModel.gameResults.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRv() = with(binding.rvResults) {
        adapter = mAdapter
        layoutManager = LinearLayoutManager(requireContext())
        setHasFixedSize(true)
    }
}