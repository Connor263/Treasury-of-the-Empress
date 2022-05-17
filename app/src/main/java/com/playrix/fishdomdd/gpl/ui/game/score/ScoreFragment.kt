package com.playrix.fishdomdd.gpl.ui.game.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.databinding.FragmentScoreBinding
import com.playrix.fishdomdd.gpl.utils.MEMORY_CARDS_SIZE
import com.playrix.fishdomdd.gpl.utils.enums.GameEndType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreFragment : Fragment() {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBinding.inflate(layoutInflater)

        binding.mBtnPlayAgain.setOnClickListener {
            navigateToGameFragment()
        }
        binding.mBtnMenu.setOnClickListener {
            navigateToMenuFragment()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: ScoreFragmentArgs by navArgs()
        val gameResult = navArgs.result
        viewModel.saveResult(gameResult)

        binding.tvGameResult.text = resources.getString(
            when (gameResult.result) {
                GameEndType.WIN -> R.string.you_win
                GameEndType.LOSE -> R.string.you_lose
            }
        )
        binding.tvMistakes.text = resources.getString(R.string.mistakes, gameResult.mistakes)
        binding.matchedCards.text = if (gameResult.matched == MEMORY_CARDS_SIZE) {
            resources.getString(R.string.matched_cards_all)
        } else {
            resources.getString(
                R.string.matched_cards,
                gameResult.matched,
                MEMORY_CARDS_SIZE
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToGameFragment() {
        val action = ScoreFragmentDirections.actionScoreFragmentToGameFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMenuFragment() {
        val action = ScoreFragmentDirections.actionGlobalMenuFragment()
        findNavController().navigate(action)
    }
}