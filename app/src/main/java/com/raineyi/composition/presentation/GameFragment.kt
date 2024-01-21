package com.raineyi.composition.presentation

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.raineyi.composition.R
import com.raineyi.composition.databinding.FragmentGameBinding
import com.raineyi.composition.domain.entity.GameResult
import com.raineyi.composition.domain.entity.GameSettings
import com.raineyi.composition.domain.entity.Level


class GameFragment : Fragment() {
    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    lateinit var viewModel: GameViewModel

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
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        binding.tvQuestion.setOnClickListener {
            launchGameFinishedFragment(
                GameResult(
                    true,
                    10,
                    10,
                    GameSettings(10, 10, 50, 50)
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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