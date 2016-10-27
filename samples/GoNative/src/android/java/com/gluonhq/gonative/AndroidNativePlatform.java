package com.gluonhq.gonative;

public class AndroidNativePlatform extends GoNativePlatform {

    private AndroidNativeService androidNativeService;
    
    @Override
    public NativeService getNativeService() {
        if (androidNativeService == null) {
            androidNativeService = new AndroidNativeService();
        }
        return androidNativeService;
    }
    
}