package com.raineyi.composition.presentation

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.raineyi.composition.R
import com.raineyi.composition.databinding.FragmentGameBinding
import com.raineyi.composition.domain.entity.GameResult
import com.raineyi.composition.domain.entity.Level


class GameFragment : Fragment() {
    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private val viewModelFactory by lazy {
        GameViewModelFactory(level, requireActivity().application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setOnClickListenersToOptions()
    }

    private fun setOnClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel() {
        with(binding) {
            viewModel.formattedTime.observe(viewLifecycleOwner) {
                tvTimer.text = it
            }
            viewModel.question.observe(viewLifecycleOwner) {
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.options[i].toString()
                }
            }
            viewModel.percentOfRightAnswer.observe(viewLifecycleOwner) {
                progressBar.setProgress(it, true)
            }
            viewModel.progressAnswers.observe(viewLifecycleOwner) {
                tvAnswersProgress.text = it
            }
            viewModel.enoughCount.observe(viewLifecycleOwner) {
                tvAnswersProgress.setTextColor(getColorByState(it))
            }
            viewModel.enoughPercent.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                progressBar.progressTintList = ColorStateList.valueOf(color)
            }
            viewModel.minPercent.observe(viewLifecycleOwner) {
                progressBar.secondaryProgress = it
            }
            viewModel.gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            R.color.green_dark
        } else {
            R.color.dark_pink
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun parseArgs() {
        when {
            Build.VERSION.SDK_INT >= 33 -> requireArguments().getParcelable(
                KEY_LEVEL,
                Level::class.java
            )

            else -> requireArguments().getParcelable<Level>(KEY_LEVEL)
        }?.let {
            level = it
        }
    }


    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.getInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}