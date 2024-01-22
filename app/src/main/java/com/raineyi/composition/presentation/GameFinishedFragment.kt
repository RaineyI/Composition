package com.raineyi.composition.presentation

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.raineyi.composition.R
import com.raineyi.composition.databinding.FragmentGameFinishedBinding
import com.raineyi.composition.domain.entity.GameResult
import com.raineyi.composition.domain.entity.GameSettings

class GameFinishedFragment : Fragment() {
    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        bindViews()
    }

    private fun setUpClickListeners(){
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun bindViews(){
        with(binding) {
            imResult.setImageResource(getSmileResId())
            tvRequiredAnswers.text = String.format(
                getString(R.string.required_answers),
                gameResult.gameSettings.minCountOfRightAnswers)

            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameResult.gameSettings.minPercentOfRightAnswers)

            tvScore.text = String.format(
                getString(R.string.your_score),
                gameResult.countOfRightAnswers)

            tvScorePercentage.text = String.format(
                getString(R.string.percentage),
                getPercentOfRightAnswers()
            )
        }
    }

    private fun getSmileResId(): Int{
        return if(gameResult.winner) {
            R.drawable.im_victory
        } else {
            R.drawable.ic_sad
        }
    }

    private fun getPercentOfRightAnswers() = with(gameResult) {
        if(countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        when {
            SDK_INT >= 33 -> requireArguments().getParcelable(
                KEY_GAME_RESULT,
                GameResult::class.java
            )

            else -> requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)
        }?.let {
            gameResult = it
        }
    }


private fun retryGame() {
    requireActivity().supportFragmentManager.popBackStack(
        GameFragment.NAME,
        FragmentManager.POP_BACK_STACK_INCLUSIVE
    )
}

companion object {
    private const val KEY_GAME_RESULT = "game_result"

    fun getInstance(gameResult: GameResult): GameFinishedFragment {
        return GameFinishedFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_GAME_RESULT, gameResult)
            }
        }
    }
}
}