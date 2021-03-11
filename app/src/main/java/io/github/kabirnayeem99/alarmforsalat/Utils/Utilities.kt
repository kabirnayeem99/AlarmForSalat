package io.github.kabirnayeem99.alarmforsalat.Utils

import android.util.Log
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.MapsFragment
import java.io.InputStream
import java.nio.charset.Charset

object Utilities {

    private const val TAG = "Utilities"

    fun fileToJson(inputStream: InputStream): String? {
        var json: String? = null
        try {
            // InputStream is used to read data from a source
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
            return json
        } catch (e: Exception) {
            Log.e(TAG, "onViewCreated: $e")
        }
        return json
    }
}