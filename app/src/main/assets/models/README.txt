MODEL ASSET SLOT
================
No fake or untrained detector is shipped.

Install a compatible detector under the app-managed model directory at runtime, or add a licensed model asset here.
Expected model formats: .tflite or .onnx.

The application exposes RecognitionSchema.classes as the canonical class list and ModelAssetDetector as the integration boundary.
A production detector adapter must map model outputs to DetectedBuilding with normalized/bitmap coordinates and confidence scores.
