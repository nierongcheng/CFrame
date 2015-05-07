package com.codi.frame.utils;

import com.codi.frame.model.FormFile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Title: UploadUtil
 * Description: 实现文件上传的工具类
 */
public class UploadUtil {

    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";
    public static final String IMG_TYPE = "img[]";
    public static final String AUDIO_TYPE = "audio[]";
    public static final String FILE_CONTENT_TYPE = "application/octet-stream";

    private static final String SEND_TYPE = "POST";
    private static final int CONNECT_TIME_OUT = 5000;            //连接超时时间
    private static final int READ_TIME_OUT = 120000;            //超时时间
    private static final String CHARSET = "utf-8";        //编码格式

    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String BOUNDARY = "--------------codi";

    private static final int UPLOAD_SUCCESS = 200;

    /**
     * @param requestURL 上传路径
     * @param params     请求参数 key为参数名，value为参数值
     * @param files      要上传的文件
     * @return
     */
    public static String post(HashMap<String, String> header, String requestURL, Map<String, String> params, FormFile... files) {

        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setRequestMethod(SEND_TYPE);
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            if (header != null) {
                Set<Entry<String, String>> entrys = header.entrySet();
                for (Entry<String, String> entry : entrys) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());

            if (params != null && params.size() > 0) {
                StringBuilder sBuffer = new StringBuilder();
                //上传表单参数部分
                for (Entry<String, String> entry : params.entrySet()) {
                    sBuffer.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sBuffer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(LINE_END);
                    sBuffer.append("Content-Type: text/plain; charset=").append(CHARSET).append(LINE_END).append(LINE_END);
                    sBuffer.append(entry.getValue());
                    sBuffer.append(LINE_END);
                }
                outStream.write(sBuffer.toString().getBytes());
            }

            if (files != null && files.length > 0) {
                //上传文件部分
                for (FormFile formFile : files) {

                    File file = new File(formFile.filePath);
                    if (!file.exists()) {
                        continue;
                    }

                    StringBuilder fileBuffer = new StringBuilder();
                    fileBuffer.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    fileBuffer.append("Content-Disposition: form-data; name=\"").append(formFile.formName).append("\"; filename=\"")
                            .append(new File(formFile.filePath).getName()).append("\"").append(LINE_END);
                    fileBuffer.append("Content-Type: ").append(formFile.contentType).append("; charset=").append(CHARSET).append(LINE_END).append(LINE_END);
                    outStream.write(fileBuffer.toString().getBytes());

                    InputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[32 * 1024];
                    int len = 0;
                    while ((len = fileInputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    fileInputStream.close();
                    outStream.write(LINE_END.getBytes());
                }
            }

            String endFlag = PREFIX + BOUNDARY + PREFIX + LINE_END;
            outStream.write(endFlag.getBytes());

            outStream.flush();
            outStream.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == UPLOAD_SUCCESS) {
                InputStream inStream = conn.getInputStream();
                StringBuffer response = new StringBuffer();
                int resLen = 0;
                while ((resLen = inStream.read()) != -1) {
                    response.append((char) resLen);
                }
                return SUCCESS;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return FAILURE;
    }
}
