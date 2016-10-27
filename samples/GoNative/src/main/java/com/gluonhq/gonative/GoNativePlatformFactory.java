package com.gluonhq.gonative;

import com.gluonhq.charm.down.common.JavaFXPlatform;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoNativePlatformFactory {
    
    public static GoNativePlatform getPlatform() {
        try {
            return (GoNativePlatform) Class.forName(getPlatformClassName()).newInstance();
        } catch (Throwable ex) {
            Logger.getLogger(GoNativePlatformFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private static String getPlatformClassName() {
        switch (JavaFXPlatform.getCurrent()) {
            case ANDROID: return "com.gluonhq.gonative.AndroidNativePlatform";
            case IOS: return "com.gluonhq.gonative.IosNativePlatform";
            default : return "com.gluonhq.gonative.DesktopNativePlatform";
        }
    }
}
