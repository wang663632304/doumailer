package com.googolmo.douban.dou4a.http;

import com.googolmo.douban.dou4a.util.Configuration;
import org.apache.http.NameValuePair;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * User: googolmo
 * Date: 13-2-9
 * Time: 上午11:11
 */
public class BasicHandler {
    public static final String CHARSET_UTF8 = "utf-8";
    public static final String CONTENT_TYPE_FORM_URLENCODE = "application/x-www-form-urlencoded";
    public static final String CONNECTION_KEEPALIVE = "Keep-Alive";
    private static boolean KEEP_ALIVE = true;
    private static final String BOUNDARYSTR = "abc123abc123defdefdef123";
    private static final String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
    private Map<String, String> headers;
    private boolean useDNS;
    private boolean useGzip;
    private String userAgent;
    private String acceptCharset;
    private Method method;
    private boolean keepAlive;
    private String url;
    private int connectTimeout;
    private int readTimeout;


    public BasicHandler(String url, Method method) {
        this.url = url;
        this.method = method;
        init();
    }

    private void init() {
        this.headers = new HashMap<String, String>();
        useDNS = false;
        this.useGzip = true;
        this.userAgent = "douban-api/googolmo 1.0";
        this.acceptCharset = CHARSET_UTF8;
        connectTimeout = 1000 * 15;
        readTimeout = 1000 * 15;
        keepAlive = KEEP_ALIVE;
    }

    public void setRequestProperty(String key, String value) {
        headers.put(key, value);
    }

    public void setOAuth(String accessToken) {
        headers.put("Authorization", "Bearer " + accessToken);
    }

    public void setUseCustomDNS(boolean value) {
        useDNS = value;
    }

    public void setUseGzip(boolean value) {
        this.useGzip = value;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setAcceptCharset(String value) {
        this.acceptCharset = value;
    }

    public void setKeepAlive(boolean value) {
        this.keepAlive = value;
    }

    public static void disableKeepAlive() {
        KEEP_ALIVE = false;
        System.setProperty("http.keepAlive", "false");
    }

    public void setReadTimeout(int value) {
        this.readTimeout = value;
    }

    public void setConnectTimeout(int value) {
        this.connectTimeout = value;
    }



    public Response fetchData(List<NameValuePair> params) {
        int code = 200;

        try {
            URL aUrl = new URL(url);
            HttpURLConnection connection;
            connection = (HttpURLConnection) aUrl.openConnection();
            try {

                connection.setConnectTimeout(this.connectTimeout);
                connection.setReadTimeout(this.readTimeout);
                connection.setRequestMethod(method.name());

                if (useGzip) {
                    connection.setRequestProperty("Accept-Encoding", "gzip");
                }
                if (userAgent != null) {
                        connection.setRequestProperty("User-Agent", userAgent);
                }

                if (acceptCharset != null) {
                    connection.setRequestProperty("Accept-Charset", acceptCharset);
                } else {
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                }

                connection.setRequestProperty("Content-Type", CONTENT_TYPE_FORM_URLENCODE);

                if (keepAlive) {
                    connection.setRequestProperty("Connection", CONNECTION_KEEPALIVE);
                }

                //设置Headers
                if (headers != null) {
                    for (String key :headers.keySet()) {
                        connection.setRequestProperty(key, headers.get(key));
                    }
                }

                //TODO DNS解析

                connection.setDoInput(true);
                if (method.name().equals("POST") || method.name().equals("PUT")) {

                    connection.setDoOutput(true);
                    if (params != null) {
                        OutputStream os = connection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getQuery(params));
                        writer.close();
                        os.close();
                    }

                }

                connection.connect();

                code = connection.getResponseCode();
                boolean isGzip = false;
                if (connection.getContentEncoding() != null) {
                    isGzip = connection.getContentEncoding().equalsIgnoreCase("gzip");
                }

                if (code > 300) {
                    return new Response(readStream(connection.getErrorStream(), isGzip), code, getMessageByCode(code));
                } else {
                    return new Response(readStream(connection.getInputStream(), isGzip), code, connection.getResponseMessage());
                }

            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new Response("", 400, "Malformed URL: " + url);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response("", 500, e.getMessage());
        }
    }


    public Response fetchDataMultipartMime(List<NameValuePair> params, MultipartParameter... parameters) {
        int code = 200;

        try {
            URL aUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) aUrl.openConnection();
            try {
                connection.setUseCaches(false);
                connection.setConnectTimeout(this.connectTimeout);
                connection.setReadTimeout(this.readTimeout);
                connection.setRequestMethod(method.name());

                if (useGzip) {
                    connection.setRequestProperty("Accept-Encoding", "gzip");
                }
                if (userAgent != null) {
                    connection.setRequestProperty("User-Agent", userAgent);
                }

                if (acceptCharset != null) {
                    connection.setRequestProperty("Accept-Charset", acceptCharset);
                } else {
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                }

                connection.setRequestProperty("Connection", "keep-alive");

                if (keepAlive) {
                    connection.setRequestProperty("Connection", "");
                }

                //设置Headers
                if (headers != null) {
                    for (String key : headers.keySet()) {
                        connection.setRequestProperty(key, headers.get(key));
                    }
                }

                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARYSTR);

                OutputStream outputStream = connection.getOutputStream();

                BufferedOutputStream out = new BufferedOutputStream(outputStream);

                int length = 0;

                StringBuilder formDataBuilder = new StringBuilder();

                for (NameValuePair pair : params) {
                    formDataBuilder.append(BOUNDARY);
                    formDataBuilder.append("Content-Disposition:form-data;name=\"");
                    formDataBuilder.append(pair.getName());
                    formDataBuilder.append("\"\r\n\r\n");
                    formDataBuilder.append(pair.getValue());
                    formDataBuilder.append("\r\n");
                }

                length += formDataBuilder.toString().getBytes().length;
                out.write(formDataBuilder.toString().getBytes());

                for (MultipartParameter parameter : parameters) {
                    StringBuilder formDataBuffer = new StringBuilder();

                    formDataBuffer.append(BOUNDARY)
                            .append("Content-Disposition:form-data;name=\"")
                            .append(parameter.getName())
                            .append("\";filename=\"")
                            .append(parameter.getName())
                            .append("\"\r\n")
                            .append("Content-Type:")
                            .append("image/").append(parameter.getContentType())
                            .append("\r\n\r\n");
                    out.write(formDataBuffer.toString().getBytes());
                    length += formDataBuffer.toString().getBytes().length;
                    out.write(parameter.getContent());
                    length += parameter.getContent().length;

                }


                byte[] endData = ("\r\n--" + BOUNDARYSTR + "--\r\n").getBytes();
                length += endData.length;

                out.write(endData);

                connection.setRequestProperty("Content-Length", String.valueOf(length));
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                out.flush();
                out.close();

                outputStream.close();


                boolean isGzip = false;
                if (connection.getContentEncoding() != null) {
                    isGzip = connection.getContentEncoding().equalsIgnoreCase("gzip");
                }
                code = connection.getResponseCode();
                if (code > 300) {
                    return new Response(readStream(connection.getErrorStream(), isGzip), code, getMessageByCode(code));
                } else {
                    return new Response(readStream(connection.getInputStream(), isGzip), code, connection.getResponseMessage());
                }
            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            return new Response("", 400, "Malformed URL: " + url);
        } catch (IOException e) {
            return new Response("", 500, e.getMessage());
        }
    }


    public String getQuery(List<NameValuePair> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (NameValuePair pair : params) {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }
                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * Reads input stream and returns it's contents as String
     *
     * @param inputStream input stream to be readed
     * @return Stream's content
     * @throws IOException
     */
    private String readStream(InputStream inputStream, boolean isGZIP) throws IOException {
        StringWriter responseWriter = new StringWriter();

        char[] buf = new char[1024];
        int l = 0;

        if (null != inputStream && isGZIP) {
            // the response is gzipped
            inputStream = new GZIPInputStream(inputStream);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        while ((l = inputStreamReader.read(buf)) > 0) {
            responseWriter.write(buf, 0, l);
        }

        responseWriter.flush();
        responseWriter.close();
        return responseWriter.getBuffer().toString();
    }

    /**
     * Returns message for code
     *
     * @param code code
     * @return Message
     */
    private String getMessageByCode(int code) {
        switch (code) {
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 405:
                return "Method Not Allowed";
            case 500:
                return "Internal Server Error";
            default:
                return "Unknown";
        }
    }
}
