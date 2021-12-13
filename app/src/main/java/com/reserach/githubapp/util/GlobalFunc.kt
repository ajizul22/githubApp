package com.reserach.githubapp.util

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.reserach.githubapp.databinding.DialogErrorMessageBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GlobalFunc {

    fun FORMAT_DATE_DD_MMM_YYYY(inputDate: String): String? {
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.US)

        val date: Date = inputFormat.parse(inputDate)
        val outputText: String = outputFormat.format(date)

        return outputText
    }

}