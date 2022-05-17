package com.playrix.fishdomdd.gpl.ui.game.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.databinding.FragmentOptionsBinding
import com.playrix.fishdomdd.gpl.utils.enums.CardBoardOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionsFragment : Fragment() {
    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OptionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionsBinding.inflate(layoutInflater)

        binding.mBtnSubmit.setOnClickListener {
            saveOption()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setBufferOption()
        viewModel.bufferOption.observe(viewLifecycleOwner) {
            when (it) {
                CardBoardOptions.OPTION_4x5 -> binding.rb1.isChecked = true
                CardBoardOptions.OPTION_4x4 -> binding.rb2.isChecked = true
                CardBoardOptions.OPTION_3x3 -> binding.rb3.isChecked = true
                CardBoardOptions.OPTION_3x2 -> binding.rb4.isChecked = true
                null -> {}
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, rb ->
            viewModel.saveBufferOption(
                when (rb) {
                    R.id.rb_1 -> CardBoardOptions.OPTION_4x5
                    R.id.rb_2 -> CardBoardOptions.OPTION_4x4
                    R.id.rb_3 -> CardBoardOptions.OPTION_3x3
                    R.id.rb_4 -> CardBoardOptions.OPTION_3x2
                    else -> CardBoardOptions.OPTION_4x5
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveOption() {
        viewModel.saveOption()
        findNavController().navigateUp()
    }
}