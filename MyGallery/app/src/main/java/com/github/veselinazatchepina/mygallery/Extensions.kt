package com.github.veselinazatchepina.mygallery

import android.content.Context
import android.support.annotation.LayoutRes
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.regex.Pattern

//TODO set title
fun String.setFirstVowelColor(context: Context): Spannable {
    var newText: Spannable = SpannableString(this)
    if (!this.isEmpty() || this != "") {
        val index = getFirstVowelIndex(this)
        newText = SpannableString(this)
        newText.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.colorAccent)),
                index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return newText
    }
    return newText
}

/**
 * Method returns index of first vowel in title (for english language)
 *
 * @param text     current text
 * @return index of first vowel
 */
private fun getFirstVowelIndex(text: String): Int {
    val patternString = "(?i:[aeiouy]).*"
    return getIndex(patternString, text)
}

private fun getIndex(patternString: String, text: String): Int {
    val p = Pattern.compile(patternString)
    val m = p.matcher(text)
    return if (m.find()) {
        m.start()
    } else {
        -1
    }
}

infix fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
