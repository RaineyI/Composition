package com.raineyi.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.raineyi.composition.R
import com.raineyi.composition.domain.entity.GameResult


interface onOptionClickListener{
    fun onOptionClick(option: Int)
}
@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_answers),
        count
    )
}

@BindingAdapter("yourScore")
fun bindYourScore(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.your_score),
        score
    )
}

@BindingAdapter("requiredPercent")
fun bindRequiredPercent(textView: TextView, percentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        percentage
    )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.percentage),
        getPercentOfRightAnswers(gameResult)
    )
}

private fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestions == 0) {
        0
    } else {
        ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("imageWinner")
fun bindImageWinner(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getSmileResId(winner))
}

private fun getSmileResId(winner: Boolean): Int {
    return if (winner) {
        R.drawable.im_victory
    } else {
        R.drawable.ic_sad
    }
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("progressAnswersColor")
fun bindProgressAnswersColor(textView: TextView, goodState: Boolean) {
    textView.setTextColor(getColorByState(textView.context, goodState))
}

@BindingAdapter("progressPercentColor")
fun bindProgressBarColor(progressBar: ProgressBar, goodState: Boolean) {
    progressBar.progressTintList =
        ColorStateList.valueOf(getColorByState(progressBar.context, goodState))
}

private fun getColorByState(context: Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        R.color.green_dark
    } else {
        R.color.dark_pink
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("progressPercent")
fun bindPercent(progressBar: ProgressBar, percent: Int) {
    progressBar.setProgress(percent, true)
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: onOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

