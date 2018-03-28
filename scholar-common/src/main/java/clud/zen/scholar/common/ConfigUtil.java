package clud.zen.scholar.common;

import clud.zen.scholar.common.env.Env;
import clud.zen.scholar.common.util.ConfigurableConstants;

public class ConfigUtil extends ConfigurableConstants {

    public static final String wxAppId;
    public static final String wxAppSecret;

    static {
        init("application-" + Env.getEnv() + ".properties");
        wxAppId = getProperty("WeChat.appId");
        wxAppSecret = getProperty("WeChat.appSecret");
    }

}
