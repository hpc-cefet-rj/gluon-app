/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gluonhq.gonative.views;

import com.gluonhq.charm.down.common.JavaFXPlatform;
import com.gluonhq.charm.down.common.PlatformFactory;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.gonative.GoNative;
import com.gluonhq.gonative.GoNativePlatformFactory;
import com.gluonhq.gonative.NativeService;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleButton;
import javafx.util.StringConverter;

public class AccelPresenter {

    @FXML
    private View accel;

    @FXML
    private ToggleButton switchAccel;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private LineChart<Number, Number> chart;
    
    public void initialize() {
        final NativeService nativeService = GoNativePlatformFactory.getPlatform().getNativeService();

        accel.setShowTransitionFactory(BounceInRightTransition::new);
        
        accel.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(GoNative.MENU_LAYER)));
                appBar.setTitleText("Accelerometer");
            }
        });
        
        switchAccel.selectedProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                nativeService.startAccelerometer();
            } else {
                nativeService.stopAccelerometer();
            }
        });
        
        PlatformFactory.getPlatform().setOnLifecycleEvent(param -> {
                switch (param) {
                    case PAUSE:
                            if (switchAccel.isSelected()) {
                                nativeService.stopAccelerometer();
                            }
                            break;
                    case RESUME:
                            if (switchAccel.isSelected()) {
                                nativeService.startAccelerometer();
                            }
                            break;
                }
                return null;
            });
        
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        xAxis.setTickLabelFormatter(new StringConverter<Number>(){

            @Override
            public String toString(Number t) {
                return new SimpleDateFormat("HH:mm:ss").format(new Date(t.longValue()));
            }

            @Override
            public Number fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });

        final int maxSize = JavaFXPlatform.isDesktop() ? 200 : 100;
        XYChart.Series<Number, Number> xSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> ySeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> zSeries = new XYChart.Series<>();
        xSeries.setName("X-Axis");
        ySeries.setName("Y-Axis");
        zSeries.setName("Z-Axis");
        chart.setTitle(JavaFXPlatform.isAndroid() ? "Accelerometer" : "Fake Accelerometer");
        chart.getData().addAll(xSeries, ySeries, zSeries);
        nativeService.x().addListener((obs, ov, nv) -> {
            xSeries.getData().add(new XYChart.Data<>(System.currentTimeMillis(), nv));
            if (xSeries.getData().size() > maxSize) {
                xSeries.getData().remove(0);
            }
        });
        nativeService.y().addListener((obs, ov, nv) -> {
            ySeries.getData().add(new XYChart.Data<>(System.currentTimeMillis(), nv));
            if (ySeries.getData().size() > maxSize) {
                ySeries.getData().remove(0);
            }
        });
        nativeService.z().addListener((obs, ov, nv) -> {
            zSeries.getData().add(new XYChart.Data<>(System.currentTimeMillis(), nv));
            if (zSeries.getData().size() > maxSize) {
                zSeries.getData().remove(0);
            }
        });
    }    
    
}
