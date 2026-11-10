# Detector dataset format

Directory layout:

```
dataset/
  images/train/
  images/val/
  labels/train/
  labels/val/
  classes.txt
```

Labels use one YOLO row per object:
`class_id center_x center_y width height`, normalized to 0..1.

`classes.txt` must match `RecognitionSchema.classes` exactly. Keep screenshots licensed for redistribution and remove player identifiers when appropriate.

Recommended training process:
1. Collect diverse war/scouting screenshots across TH1–TH18 and device aspect ratios.
2. Annotate every target building class plus walls where useful.
3. Split by base layout, not by near-duplicate screenshots.
4. Train a detector outside this Android repository.
5. Export to a mobile-compatible `.tflite` or `.onnx` model.
6. Validate precision/recall per class and publish model metadata.
7. Add an Android runtime adapter in `recognition/` and test coordinate scaling.
