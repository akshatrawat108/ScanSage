package com.abhinav.idanalyzer.core.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.abhinav.idanalyzer.feature_id_analyzer.domain.IdAnalyzer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object DataManager {

    private fun createBitmapFromByteArray(data: ByteArray): Bitmap {
        val inputStream = ByteArrayInputStream(data)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun getImageFromByteArray(byteArray: ByteArray): Bitmap{
        return try {
            val outputStream = ByteArrayOutputStream()
            outputStream.write(byteArray)

            val bitmapData = outputStream.toByteArray()
            createBitmapFromByteArray(bitmapData)
        }catch (e: Exception){
            Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
        }
    }

    fun getImageFromChunks(chunks: List<IdAnalyzer>): Bitmap {

        var totalSize = 0
        chunks.forEach {
            totalSize += it.imageData.size
        }

        val byteArray = ByteArray(totalSize)

        var offset = 0
        chunks.forEach {
            System.arraycopy(it.imageData, 0, byteArray, offset, it.imageData.size)
            offset += it.imageData.size
        }

        return getImageFromByteArray(byteArray)
    }

}