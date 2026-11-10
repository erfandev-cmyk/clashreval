package com.cocwarassistant.recognition

import android.content.Context
import android.graphics.Bitmap
import com.cocwarassistant.core.*
import java.io.File

interface LocalDetector { suspend fun detect(bitmap: Bitmap): DetectionResult }
data class DetectionResult(val detections: List<DetectedBuilding>, val modelVersion: String?, val warnings: List<String>)

class ModelAssetDetector(private val context: Context): LocalDetector {
    override suspend fun detect(bitmap: Bitmap): DetectionResult {
        val modelDir = File(context.filesDir, "models")
        val model = modelDir.listFiles()?.firstOrNull { it.extension.lowercase() in setOf("tflite","onnx") }
        return if (model == null) DetectionResult(emptyList(), null, listOf("No trained object-detection model is bundled. Recognition is unavailable until a compatible model is installed. Use manual building correction to create a tactical plan."))
        else DetectionResult(emptyList(), model.name, listOf("Model asset detected, but no runtime-specific adapter is registered for ${model.name}. Add the adapter in LocalDetectorFactory before enabling model inference."))
    }
}

object RecognitionSchema {
    const val VERSION = "1.0"
    val classes = listOf("town_hall","eagle_artillery","inferno_tower","x_bow","scattershot","monolith","spell_tower","air_defense","archer_tower","wizard_tower","cannon","mortar","bomb_tower","hidden_tesla","wall","resource","army","guardian","revenge_tower","super_wizard_tower")
}
