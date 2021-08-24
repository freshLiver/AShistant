package com.freshliver.ashistant.assistant;

import android.content.Intent;


public interface CropImageViewInterface {
    void resetCropImageView(Intent newIntent);
    void cropImage();
    void flipHorizontally();
    void flipVertically();
    void rotateLeft90();
    void rotateRight90();
    void saveCroppedArea();
    void shareCroppedArea();
    void uploadCroppedArea();
}
