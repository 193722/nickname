package clud.zen.scholar.common.env;

import clud.zen.scholar.common.exception.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.util.ArrayList;

public enum Env {
    online,
    pre,
    net,
    test,
    dev,
    local;

    public static final String ENV_KEY = "spring.profiles.active";
    public static final String ENV_ENV_KEY = "SPRING_PROFILES_ACTIVE";
    private static Env currEnv = null;
    private static String currEnvStr = null;
    private static final Logger logger = LoggerFactory.getLogger(Env.class.getName());
    private static String port = null;
    private static final String ERROR_PORT = "-1";
    private static final String httpProtocol = "http";

    private Env() {
    }

    public static Env getCurrEnv() {
        if (currEnv == null) {
            getEnv();
        }

        return currEnv;
    }

    public static String getEnv() {
        if (currEnvStr == null) {
            currEnvStr = System.getProperty(ENV_KEY);
            if (currEnvStr == null) {
                currEnvStr = System.getenv(ENV_ENV_KEY);
            }

            if (currEnvStr == null) {
                throw Exceptions.newRuntimeException("需要配置系统变量spring.profiles.active或者环境变量SPRING_PROFILES_ACTIVE");
            }

            try {
                currEnv = valueOf(currEnvStr);
            } catch (IllegalArgumentException var1) {
                ;
            }
        }

        return currEnvStr;
    }

    public static boolean isOnline() {
        return is(online);
    }

    public static boolean isLocal() {
        return is(local);
    }

    public static boolean isDev() {
        return is(dev);
    }

    public static boolean isTest() {
        return is(test);
    }

    public static boolean isPre() {
        return is(pre);
    }

    public static boolean isNet() {
        return is(net);
    }

    public static boolean is(Env env) {
        if (env == null) {
            throw Exceptions.newRuntimeException("env不能为null");
        } else {
            if (currEnv == null) {
                getEnv();
            }

            return env == currEnv;
        }
    }

    public static String getPort() {
        if (port == null) {
            try {
                if (System.getProperty("server.port") != null) {
                    port = System.getProperty("server.port");
                    return port;
                }

                initPortByMBean(new ObjectName("Catalina", "type", "Service"));
                initPortByMBean(new ObjectName("Tomcat", "type", "Service"));
            } catch (Exception var1) {
                logger.warn("获取端口失败:{}", var1.getMessage());
            }

            if (port == null) {
                port = ERROR_PORT;
            }
        }

        return port;
    }

    public boolean isInList(Env... values) {
        Env[] arr$ = values;
        int len$ = values.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Env e = arr$[i$];
            if (this == e) {
                return true;
            }
        }

        return false;
    }

    public static boolean isErrorPort(String port) {
        return ERROR_PORT.equals(port);
    }

    private static void initPortByMBean(ObjectName name) {
        if (port == null) {
            ArrayList<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer((String) null);
            if (!mBeanServers.isEmpty()) {
                MBeanServer mBeanServer = (MBeanServer) mBeanServers.get(0);

                try {
                    ObjectName[] objNames = (ObjectName[]) ((ObjectName[]) mBeanServer.getAttribute(name, "connectorNames"));
                    ObjectName[] arr$ = objNames;
                    int len$ = objNames.length;

                    for (int i$ = 0; i$ < len$; ++i$) {
                        ObjectName on = arr$[i$];
                        String protocol = (String) mBeanServer.getAttribute(on, "protocol");
                        if (protocol.toLowerCase().contains("http")) {
                            Integer p = (Integer) mBeanServer.getAttribute(on, "localPort");
                            if (p != null) {
                                port = String.valueOf(p);
                                logger.info("获取服务端口成功:{}", port);
                            }
                        }
                    }
                } catch (Exception var10) {
                    logger.warn("通过{}获取服务端口失败", name.toString());
                }
            }
        }

    }
}