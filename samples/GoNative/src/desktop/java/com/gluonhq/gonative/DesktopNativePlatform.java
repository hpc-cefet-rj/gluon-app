package com.gluonhq.gonative;

public class DesktopNativePlatform extends GoNativePlatform {

    private DesktopNativeService androidNativeService;
    
    @Override
    public NativeService getNativeService() {
        if (androidNativeService == null) {
            androidNativeService = new DesktopNativeService();
        }
        return androidNativeService;
    }
    
}