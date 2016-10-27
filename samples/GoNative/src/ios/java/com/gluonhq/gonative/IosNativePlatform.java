package com.gluonhq.gonative;

public class IosNativePlatform extends GoNativePlatform {

    private IosNativeService androidNativeService;
    
    @Override
    public NativeService getNativeService() {
        if (androidNativeService == null) {
            androidNativeService = new IosNativeService();
        }
        return androidNativeService;
    }
    
}