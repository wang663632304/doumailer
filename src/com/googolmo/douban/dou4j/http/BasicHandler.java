package com.googolmo.douban.dou4j.http;

import com.googolmo.douban.dou4j.util.Configuration;
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
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * User: googolmo
 * Date: 13-2-9
 * Time: 上午11:11
 */
public class BasicHandler extends IOHandler {
    public static final String BOUNDARYSTR = "abc123abc123defdefdef123";
    private static final String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";

    public BasicHandler() {
    }

    @Override
    public Response fetchData(String url, Method method, List<NameValuePair> params, List<NameValuePair> headers) {
        int code = 200;

        try {
            URL aUrl = new URL(url);
            HttpURLConnection connection;
            connection = (HttpURLConnection) aUrl.openConnection();
            try {

                connection.setConnectTimeout(Configuration.getConnectionTimeout());
                connection.setReadTimeout(Configuration.getReadTimeout());
                connection.setRequestMethod(method.name());

                connection.setRequestProperty("Accept-Encoding", "gzip");
                connection.setRequestProperty("User-Agent", Configuration.getUserAgent());
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //TODO DNS解析
                //设置Headers
                if (headers != null) {
                    for (NameValuePair pair : headers) {
                        connection.setRequestProperty(pair.getName(), pair.getValue());
                    }
                }

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

    @Override
    public Response fetchDataMultipartMime(String url, Method method, List<NameValuePair> params, List<NameValuePair> headers, MultipartParameter... parameters) {
        int code = 200;

        try {
            URL aUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)aUrl.openConnection();
            try{
                connection.setUseCaches(false);
                connection.setConnectTimeout(Configuration.getConnectionTimeout());
                connection.setReadTimeout(Configuration.getReadTimeout());
                connection.setRequestMethod(method.name());

                connection.setRequestProperty("Accept-Encoding", "gzip");
                connection.setRequestProperty("User-Agent", Configuration.getUserAgent());
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setRequestProperty("Connection", "keep-alive");

                //设置Headers
                if (headers != null) {
                    for (NameValuePair pair : headers) {
                        connection.setRequestProperty(pair.getName(), pair.getValue());
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

                for(MultipartParameter parameter : parameters) {
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
