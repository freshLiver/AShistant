package com.freshliver.ashistant.assistant;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface CropImageViewInterface {
    void resetCropImageView(@Nullable Intent newIntent);
    void cropImage();
    void flipHorizontally();
    void flipVertically();
    void rotateLeft90();
    void rotateRight90();
    void saveCroppedArea();
    void shareCroppedArea();
}
