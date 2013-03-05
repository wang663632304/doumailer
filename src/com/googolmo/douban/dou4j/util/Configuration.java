package com.googolmo.douban.dou4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * User: googolmo
 * Date: 13-2-8
 * Time: 上午11:41
 */
public class Configuration {
    private static Properties defaultProperty;

    static {
        init();
    }

    /*
    package
     */
    static void init() {
        defaultProperty = new Properties();

        defaultProperty = new Properties();
        defaultProperty.setProperty("dou4j.debug", "true");
//        defaultProperty.setProperty("dou4j.source", Douban.CONSUMER_KEY);
        //defaultProperty.setProperty("dou4j.clientVersion","");
        defaultProperty.setProperty("dou4j.http.userAgent", "dou4j http://api.douban.com/ /{dou4j.clientVersion}");
        //defaultProperty.setProperty("dou4j.user","");
        //defaultProperty.setProperty("dou4j.password","");
        defaultProperty.setProperty("dou4j.http.useSSL", "false");
        //defaultProperty.setProperty("dou4j.http.proxyHost","");
        defaultProperty.setProperty("dou4j.http.proxyHost.fallback", "http.proxyHost");
        //defaultProperty.setProperty("dou4j.http.proxyUser","");
        //defaultProperty.setProperty("dou4j.http.proxyPassword","");
        //defaultProperty.setProperty("dou4j.http.proxyPort","");
        defaultProperty.setProperty("dou4j.http.proxyPort.fallback", "http.proxyPort");
        defaultProperty.setProperty("dou4j.http.connectionTimeout", "20000");
        defaultProperty.setProperty("dou4j.http.readTimeout", "120000");
        defaultProperty.setProperty("dou4j.http.retryCount", "3");
        defaultProperty.setProperty("dou4j.http.retryIntervalSecs", "10");
        //defaultProperty.setProperty("dou4j.oauth.consumerKey","");
        //defaultProperty.setProperty("dou4j.oauth.consumerSecret","");
        defaultProperty.setProperty("dou4j.async.numThreads", "1");
//        defaultProperty.setProperty("dou4j.clientVersion", Version.getVersion());
        try {
            // Android platform should have dalvik.system.VMRuntime in the classpath.
            // @see http://developer.android.com/reference/dalvik/system/VMRuntime.html
            Class.forName("dalvik.system.VMRuntime");
            defaultProperty.setProperty("dou4j.dalvik", "true");
        } catch (ClassNotFoundException cnfe) {
            defaultProperty.setProperty("dou4j.dalvik", "false");
        }
        DALVIK = getBoolean("dou4j.dalvik");

    }

    private static boolean loadProperties(Properties props, String path) {
        try {
            File file = new File(path);
            if(file.exists() && file.isFile()){
                props.load(new FileInputStream(file));
                return true;
            }
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean DALVIK;


    public static boolean isDalvik() {
        return DALVIK;
    }

    public static boolean useSSL() {
        return getBoolean("dou4j.http.useSSL");
    }
    public static String getScheme(){
        return useSSL() ? "https://" : "http://";
    }

    public static String getCilentVersion() {
        return getProperty("dou4j.clientVersion");
    }

    public static String getCilentVersion(String clientVersion) {
        return getProperty("dou4j.clientVersion", clientVersion);
    }

    public static String getSource() {
        return getProperty("dou4j.source");
    }

    public static String getSource(String source) {
        return getProperty("dou4j.source", source);
    }

    public static String getProxyHost() {
        return getProperty("dou4j.http.proxyHost");
    }

    public static String getProxyHost(String proxyHost) {
        return getProperty("dou4j.http.proxyHost", proxyHost);
    }

    public static String getProxyUser() {
        return getProperty("dou4j.http.proxyUser");
    }

    public static String getProxyUser(String user) {
        return getProperty("dou4j.http.proxyUser", user);
    }

    public static String getClientURL() {
        return getProperty("dou4j.clientURL");
    }

    public static String getClientURL(String clientURL) {
        return getProperty("dou4j.clientURL", clientURL);
    }

    public static String getProxyPassword() {
        return getProperty("dou4j.http.proxyPassword");
    }

    public static String getProxyPassword(String password) {
        return getProperty("dou4j.http.proxyPassword", password);
    }

    public static int getProxyPort() {
        return getIntProperty("dou4j.http.proxyPort");
    }

    public static int getProxyPort(int port) {
        return getIntProperty("dou4j.http.proxyPort", port);
    }

    public static int getConnectionTimeout() {
        return getIntProperty("dou4j.http.connectionTimeout");
    }

    public static int getConnectionTimeout(int connectionTimeout) {
        return getIntProperty("dou4j.http.connectionTimeout", connectionTimeout);
    }

    public static int getReadTimeout() {
        return getIntProperty("dou4j.http.readTimeout");
    }

    public static int getReadTimeout(int readTimeout) {
        return getIntProperty("dou4j.http.readTimeout", readTimeout);
    }

    public static int getRetryCount() {
        return getIntProperty("dou4j.http.retryCount");
    }

    public static int getRetryCount(int retryCount) {
        return getIntProperty("dou4j.http.retryCount", retryCount);
    }

    public static int getRetryIntervalSecs() {
        return getIntProperty("dou4j.http.retryIntervalSecs");
    }

    public static int getRetryIntervalSecs(int retryIntervalSecs) {
        return getIntProperty("dou4j.http.retryIntervalSecs", retryIntervalSecs);
    }

    public static String getUser() {
        return getProperty("dou4j.user");
    }

    public static String getUser(String userId) {
        return getProperty("dou4j.user", userId);
    }

    public static String getPassword() {
        return getProperty("dou4j.password");
    }

    public static String getPassword(String password) {
        return getProperty("dou4j.password", password);
    }

    public static String getUserAgent() {
        return getProperty("dou4j.http.userAgent");
    }

    public static String getUserAgent(String userAgent) {
        return getProperty("dou4j.http.userAgent", userAgent);
    }

    public static String getOAuthConsumerKey() {
        return getProperty("dou4j.oauth.consumerKey");
    }

    public static String getOAuthConsumerKey(String consumerKey) {
        return getProperty("dou4j.oauth.consumerKey", consumerKey);
    }

    public static String getOAuthConsumerSecret() {
        return getProperty("dou4j.oauth.consumerSecret");
    }

    public static String getOAuthConsumerSecret(String consumerSecret) {
        return getProperty("dou4j.oauth.consumerSecret", consumerSecret);
    }

    public static boolean getBoolean(String name) {
        String value = getProperty(name);
        return Boolean.valueOf(value);
    }

    public static int getIntProperty(String name) {
        String value = getProperty(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static int getIntProperty(String name, int fallbackValue) {
        String value = getProperty(name, String.valueOf(fallbackValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static long getLongProperty(String name) {
        String value = getProperty(name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static String getProperty(String name) {
        return getProperty(name, null);
    }

    public static String getProperty(String name, String fallbackValue) {
        String value;
        try {
            value = System.getProperty(name, fallbackValue);
            if (null == value) {
                value = defaultProperty.getProperty(name);
            }
            if (null == value) {
                String fallback = defaultProperty.getProperty(name + ".fallback");
                if (null != fallback) {
                    value = System.getProperty(fallback);
                }
            }
        } catch (AccessControlException ace) {
            // Unsigned applet cannot access System properties
            value = fallbackValue;
        }
        return replace(value);
    }

    private static String replace(String value) {
        if (null == value) {
            return value;
        }
        String newValue = value;
        int openBrace = 0;
        if (-1 != (openBrace = value.indexOf("{", openBrace))) {
            int closeBrace = value.indexOf("}", openBrace);
            if (closeBrace > (openBrace + 1)) {
                String name = value.substring(openBrace + 1, closeBrace);
                if (name.length() > 0) {
                    newValue = value.substring(0, openBrace) + getProperty(name)
                            + value.substring(closeBrace + 1);

                }
            }
        }
        if (newValue.equals(value)) {
            return value;
        } else {
            return replace(newValue);
        }
    }

    public static int getNumberOfAsyncThreads() {
        return getIntProperty("dou4j.async.numThreads");
    }

    public static boolean getDebug() {
        return getBoolean("dou4j.debug");

    }
}
