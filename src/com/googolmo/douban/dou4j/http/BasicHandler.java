package com.googolmo.douban.dou4j.http;

import com.googolmo.douban.dou4j.util.Configuration;
import org.apache.http.NameValuePair;

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
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * User: googolmo
 * Date: 13-2-9
 * Time: 上午11:11
 */
public class BasicHandler extends IOHandler {

    public BasicHandler() {
    }

    @Override
    public Response fetchData(String url, Method method, List<NameValuePair> params, String accessToken) {
        int code = 200;

        try {
            URL aUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) aUrl.openConnection();
            try {
                connection.setDoInput(true);
                if ("POST".equals(method.name()) || "PUT".equals(method.name())) {
                    connection.setDoOutput(true);
                    if (params != null) {
                        OutputStream os = connection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getQuery(params));
                        writer.close();
                        os.close();
                    }

                }
                connection.setRequestProperty("Accept-Encoding", "gzip");
                connection.setConnectTimeout(Configuration.getConnectionTimeout());
                connection.setReadTimeout(Configuration.getReadTimeout());
                connection.setRequestProperty("User-Agent", Configuration.getUserAgent());

                if (accessToken != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                }

                connection.setRequestMethod(method.name());



                connection.connect();

                code = connection.getResponseCode();
                boolean isGzip = connection.getContentEncoding().equalsIgnoreCase("gzip");
                if (code >= 300) {
                    return new Response(readStream(connection.getErrorStream(), isGzip), code, getMessageByCode(code));
                } else {
                    InputStream inputStream = connection.getInputStream();
                    return new Response(readStream(inputStream, isGzip), code, connection.getResponseMessage());
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

    @Override
    public Response fetchDataMultipartMime(String url, String accessToken, MultipartParameter... parameters) {
        int code = 200;

        try {
            URL aUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) aUrl.openConnection();
            try {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept-Encoding", "gzip");
                connection.setConnectTimeout(Configuration.getConnectionTimeout());
                connection.setReadTimeout(Configuration.getReadTimeout());
                connection.setRequestProperty("User-Agent", Configuration.getUserAgent());
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                if (accessToken != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                }

                connection.connect();

                OutputStream outputStream = connection.getOutputStream();

                StringBuffer startBoundaryBuilder = new StringBuffer("--")
                        .append(BOUNDARY)
                        .append("\r\n");

                outputStream.write(startBoundaryBuilder.toString().getBytes());

                for (MultipartParameter parameter : parameters) {
                    StringBuffer formDataBuilder = new StringBuffer()
                            .append("Content-Disposition: form-data; name=\"")
                            .append(parameter.getName())
                            .append("\"; filename=\"")
                            .append(parameter.getName())
                            .append("\"\r\n")
                            .append("Content-Type: ")
                            .append(parameter.getContentType())
                            .append("\r\n\r\n");
                    outputStream.write(formDataBuilder.toString().getBytes());
                    outputStream.write(parameter.getContent());
                }

                StringBuilder endBoundaryBuilder = new StringBuilder("\r\n--")
                        .append(BOUNDARY)
                        .append("--\r\n");

//                if (params != null) {
//                    BufferedWriter writer = new BufferedWriter(
//                            new OutputStreamWriter(outputStream, "UTF-8"));
//                    writer.write(getQuery(params));
//                    writer.close();
//                }
                outputStream.write(endBoundaryBuilder.toString().getBytes());

                outputStream.flush();
                outputStream.close();

                code = connection.getResponseCode();
                boolean isGzip = connection.getContentEncoding().equalsIgnoreCase("gzip");
                if (code >= 300) {
                    return new Response(readStream(connection.getErrorStream(), isGzip), code, getMessageByCode(code));
                } else {
                    InputStream inputStream = connection.getInputStream();
                    return new Response(readStream(inputStream, isGzip), code, connection.getResponseMessage());
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


    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

//    private String getQuery(String url, List<NameValuePair> params) throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder(url);
//        boolean first = true;
//        if (url.contains("?")) {
//            first = false;
//        }
//
//
//        for (NameValuePair pair : params)
//        {
//            if (first) {
//                first = false;
//                result.append("?");
//            } else {
//                result.append("&");
//            }
//
//
//            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
//        }
//
//        return result.toString();
//    }

    private String readStream(InputStream inputStream) throws IOException {
        return readStream(inputStream, false);
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

    private static String BOUNDARY = "----------gc0p4Jq0M2Yt08jU534c0p";
}
