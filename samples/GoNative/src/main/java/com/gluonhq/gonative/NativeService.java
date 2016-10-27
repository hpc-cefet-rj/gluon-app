package com.gluonhq.gonative;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;

public interface NativeService {
    
    /**
     * Picture Service
     */
    void takePicture();
    
    void retrievePicture();
    
    ObjectProperty<Image> imageProperty();
    
    /**
     * Accelerometer
     */
    void startAccelerometer();
    
    void stopAccelerometer();
    
    DoubleProperty x();
    DoubleProperty y();
    DoubleProperty z();

    /**
     * Vibration
     */
    void vibrate();
}
