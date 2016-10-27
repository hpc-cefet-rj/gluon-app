package com.gluonhq.gonative;

import com.github.sarxos.webcam.Webcam;
import com.gluonhq.charm.glisten.animation.ShakeTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DesktopNativeService implements NativeService {

    private final ObjectProperty<Image> fxImage = new SimpleObjectProperty<>(
            new Image(getClass().getResourceAsStream("/icon.png")));
    
    private final AnimationTimer timer;
    private long startTime = 0l;
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final DoubleProperty z = new SimpleDoubleProperty();
    private final Random random;
    
    public DesktopNativeService() {
        random = new Random();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - startTime > 100_000_000) {
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
        /**
         * Based on Sarxos project
         * https://github.com/sarxos/webcam-capture
         */
        new Thread(() -> {
            Webcam webcam = Webcam.getDefault();
            if (webcam != null) {
                try {
                    webcam.setViewSize(new Dimension(640, 480));
                } catch (IllegalArgumentException iae) {
                    System.out.println("Error: " + iae.getMessage());
                }
		webcam.open();
                BufferedImage bf = webcam.getImage();
                if (bf != null) {
                    WritableImage writableImage = SwingFXUtils.toFXImage(bf, null);
                    Platform.runLater(() -> fxImage.set(writableImage));
                }
                webcam.close();
            }
        }).start();
    }

    @Override
    public void retrievePicture() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        fileChooser.setTitle("Select a Picture...");

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            fxImage.set(new Image("file://"+file.getAbsolutePath()));
        }
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
        ShakeTransition shake = new ShakeTransition(MobileApplication.getInstance().getGlassPane());
        shake.playFromStart();
    }
}
