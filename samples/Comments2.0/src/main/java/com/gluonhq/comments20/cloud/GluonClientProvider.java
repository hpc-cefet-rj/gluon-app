package com.gluonhq.comments20.cloud;

import com.gluonhq.connect.gluoncloud.AuthenticationMode;
import com.gluonhq.connect.gluoncloud.GluonClient;
import com.gluonhq.connect.gluoncloud.GluonClientBuilder;
import com.gluonhq.connect.gluoncloud.GluonCredentials;

/** To get a valid GluonClient it is required signing in here 
 * http://gluonhq.com/products/cloud/register/
 * with a valid email address and the name of the application.
 * 
 * A valid password will be provided.
 * 
 * With the email address and this password, 
 * login here http://portal.gluonhq.com/rest/login
 * to see the pair key/secret strings
 */
public class GluonClientProvider {

    /* 
        Important: Provide your valid key and secret strings
    */
    private static final String APPKEY =   "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
    private static final String APPSECRET =   "XXXXXXXX-XXXX-XXXX-XXXX-ZZXXXXXXXXXX";

    private static GluonClient gluonClient;

    public static GluonClient getGluonClient() {
        if (gluonClient == null) {
            gluonClient = GluonClientBuilder.create()
                    .credentials(new GluonCredentials(APPKEY, APPSECRET))
                    .authenticationMode(AuthenticationMode.USER)
                    .build();
        }
        return gluonClient;
    }

}
