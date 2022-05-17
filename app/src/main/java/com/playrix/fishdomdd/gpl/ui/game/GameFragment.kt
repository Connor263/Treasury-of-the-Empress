package com.playrix.fishdomdd.gpl.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.adapter.MemoryCardAdapter
import com.playrix.fishdomdd.gpl.data.game.model.GameEndResult
import com.playrix.fishdomdd.gpl.data.game.model.MemoryCard
import com.playrix.fishdomdd.gpl.databinding.FragmentGameBinding
import com.playrix.fishdomdd.gpl.utils.enums.CardBoardOptions
import com.playrix.fishdomdd.gpl.utils.enums.GameEndType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment(), MemoryCardAdapter.TreGameCardAdapterInterface {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    private lateinit var mAdapter: MemoryCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.background.alpha = 90
        mAdapter = MemoryCardAdapter(this)

        viewModel.setMemoryCards()

        initRv()
        viewModel.mistakes.observe(viewLifecycleOwner) {
            binding.tvMistakes.text = resources.getString(R.string.mistakes, it)
        }

        viewModel.cards.observe(viewLifecycleOwner) { list ->
            mAdapter.submitList(list.map {
                it.copy()
            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is GameViewModel.GameEvent.GameEnd -> {
                            navigateToScoreFragment(state.gameEndType)
                            viewModel.sendGameEndEvent(null, null)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: MemoryCard) {
        viewModel.setItemSelected(item)
    }

    override fun compareItems(firstCard: MemoryCard, secondCard: MemoryCard) {
        viewModel.processMemoryCards(firstCard, secondCard)
    }


    private fun initRv() = with(binding.rvCards) {
        adapter = mAdapter
        layoutManager = GridLayoutManager(
            requireContext(),
            when (viewModel.boardOption.value) {
                CardBoardOptions.OPTION_4x5, CardBoardOptions.OPTION_4x4 -> 4
                CardBoardOptions.OPTION_3x3 -> 3
                CardBoardOptions.OPTION_3x2 -> 2
                null -> 4
            },
            LinearLayoutManager.VERTICAL,
            false
        )
        setHasFixedSize(true)
    }

    private fun navigateToScoreFragment(gameEndType: GameEndType) {
        val action = GameFragmentDirections.actionGameFragmentToScoreFragment(
            result = GameEndResult(
                result = gameEndType,
                matched = viewModel.matched.value!!,
                mistakes = viewModel.mistakes.value!!,
                boardOption = viewModel.boardOption.value!!
            )
        )
        findNavController().navigate(action)
    }
}