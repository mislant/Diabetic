package com.diabetic.ui.screen.statistic

import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import java.io.OutputStream

class CreateReport : ActivityResultContracts.CreateDocument(
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
) {
    companion object {
        fun handle(result: Uri?, context: Context, save: (OutputStream) -> Unit) {
            if (result === null) {
                return
            }

            val stream = context.contentResolver
                .openOutputStream(result)

            if (stream === null) {
                return
            }

            save(stream)
        }
    }
}