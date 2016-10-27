package com.gluonhq.gonative;

import com.gluonhq.charm.down.common.PicturesService;
import com.gluonhq.charm.down.common.PlatformFactory;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.robovm.apple.audiotoolbox.AudioServices;

public class IosNativeService implements NativeService {

    private final ObjectProperty<Image> fxImage = new SimpleObjectProperty<>(
            new Image(getClass().getResourceAsStream("/icon.png")));
    
    private final PicturesService picturesService;

    private final AnimationTimer timer;
    private long startTime = 0l;
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final DoubleProperty z = new SimpleDoubleProperty();
    private final Random random;
    
    public IosNativeService() {
        picturesService = PlatformFactory.getPlatform().getPicturesService();
        random = new Random();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - startTime > 200_000_000) {
                    x.set(random.nextDouble() * 0.3);
                    y.set(random.nextDouble() * 0.4);
                    z.set(random.nextDouble() * 1.5);
                    startTime = now;
                }
            }
            
        };
    }
    
    @Override
    public void takePicture() {
        picturesService
                .retrievePicture(PicturesService.PictureSource.CAMERA)
                .addListener((obs, ov, nv) -> fxImage.set(nv));
    }

    @Override
    public void retrievePicture() {
        picturesService
                .retrievePicture(PicturesService.PictureSource.GALLERY)
                .addListener((obs, ov, nv) -> fxImage.set(nv));
    }

    @Override
    public ObjectProperty<Image> imageProperty() {
        return fxImage;
    }
    
    @Override
    public void startAccelerometer() {
        timer.start();
    }
    
    @Override
    public void stopAccelerometer() {
        timer.stop();
    }

    @Override
    public DoubleProperty x() {
        return x;
    }

    @Override
    public DoubleProperty y() {
        return y;
    }

    @Override
    public DoubleProperty z() {
        return z;
    }

    @Override
    public void vibrate() {
        AudioServices.playSystemSound(AudioServices.SystemSoundVibrate);
    }
    
}
