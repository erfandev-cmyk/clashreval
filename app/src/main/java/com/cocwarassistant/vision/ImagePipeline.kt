package com.cocwarassistant.vision

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import kotlin.math.min

data class PreprocessedImage(val bitmap: Bitmap, val scale: Float)
object ImagePipeline {
    fun normalize(source: Bitmap, maxDimension: Int = 1600): PreprocessedImage {
        val scale=min(1f, maxDimension.toFloat()/maxOf(source.width, source.height).toFloat())
        if (scale==1f) return PreprocessedImage(source,1f)
        return PreprocessedImage(Bitmap.createScaledBitmap(source,(source.width*scale).toInt(),(source.height*scale).toInt(),true),scale)
    }
    fun contrast(source: Bitmap, amount: Float=1.1f): Bitmap {
        val out=source.copy(Bitmap.Config.ARGB_8888,true); val canvas=Canvas(out); val p=Paint(); p.color=Color.argb((amount.coerceIn(0.2f,2f)*255).toInt(),255,255,255); canvas.drawBitmap(source,Matrix(),p); return out
    }
}
