package com.opposs.tube.utils;

import android.text.TextUtils;

import com.opposs.tube.entity.FmtStreamMap;
import com.opposs.tube.entity.Resolution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by xcl on 16/2/24.
 *
 *
 * Youtube 下载源解析帮助类
 *
 *
 *
 */
public class YoutubeUtils {

    private static final String NAME_VALUE_SEPARATOR = "=";

    private static final String PARAMETER_SEPARATOR = "&";

    public static HashMap<String, Resolution> Resolutions = null;

    public static final String USERAGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)";

    public static List<Resolution> playResolutions;


    public enum ResolutionNote {
        HD,
        MHD,
        LHD,
        XLHD
    }


    static {
        playResolutions = new ArrayList();
        playResolutions.add(new Resolution("17", "176x144", "3gp", "normal", ResolutionNote.LHD));
        playResolutions.add(new Resolution("36", "320x240", "3gp", "normal", ResolutionNote.LHD));
        playResolutions.add(new Resolution("18", "640x360", "mp4", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("242", "360x240", "webm", "normal", ResolutionNote.LHD));
        playResolutions.add(new Resolution("242", "360x240", "webm", "normal", ResolutionNote.LHD));
        playResolutions.add(new Resolution("243", "480x360", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("243", "480x360", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("43", "640x360", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("244", "640x480", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("245", "640x480", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("167", "640x480", "webm", "video", ResolutionNote.MHD));
        playResolutions.add(new Resolution("246", "640x480", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("247", "720x480", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("44", "854x480", "webm", "normal", ResolutionNote.MHD));
        playResolutions.add(new Resolution("168", "854x480", "webm", "video", ResolutionNote.MHD));
        Resolutions = new HashMap();
        Resolutions.put("5", new Resolution("5", "400x240", "flv", "normal", ResolutionNote.LHD));
        Resolutions.put("6", new Resolution("6", "450x270", "flv", "normal", ResolutionNote.MHD));
        Resolutions.put("17", new Resolution("17", "176x144", "3gp", "normal", ResolutionNote.LHD));
        Resolutions.put("18", new Resolution("18", "640x360", "mp4", "normal", ResolutionNote.MHD));
        Resolutions.put("22", new Resolution("22", "1280x720", "mp4", "normal", ResolutionNote.HD));
        Resolutions.put("34", new Resolution("34", "640x360", "flv", "normal", ResolutionNote.MHD));
        Resolutions.put("35", new Resolution("35", "854x480", "flv", "normal", ResolutionNote.MHD));
        Resolutions.put("36", new Resolution("36", "320x240", "3gp", "normal", ResolutionNote.LHD));
        Resolutions.put("37", new Resolution("37", "1920x1080", "mp4", "normal", ResolutionNote.XLHD));
        Resolutions.put("38", new Resolution("38", "4096x3072", "mp4", "normal", ResolutionNote.XLHD));
        Resolutions.put("43", new Resolution("43", "640x360", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("44", new Resolution("44", "854x480", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("45", new Resolution("45", "1280x720", "webm", "normal", ResolutionNote.HD));
        Resolutions.put("46", new Resolution("46", "1920x1080", "webm", "normal", ResolutionNote.XLHD));
        Resolutions.put("167", new Resolution("167", "640x480", "webm", "video", ResolutionNote.MHD));
        Resolutions.put("168", new Resolution("168", "854x480", "webm", "video", ResolutionNote.MHD));
        Resolutions.put("169", new Resolution("169", "1280x720", "webm", "video", ResolutionNote.HD));
        Resolutions.put("170", new Resolution("170", "1920x1080", "webm", "video", ResolutionNote.XLHD));
        Resolutions.put("242", new Resolution("242", "360x240", "webm", "normal", ResolutionNote.LHD));
        Resolutions.put("243", new Resolution("243", "480x360", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("244", new Resolution("244", "640x480", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("245", new Resolution("245", "640x480", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("246", new Resolution("246", "640x480", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("247", new Resolution("247", "720x480", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("82", new Resolution("82", "360p", "mp4", "normal", ResolutionNote.MHD));
        Resolutions.put("83", new Resolution("83", "480p", "mp4", "normal", ResolutionNote.MHD));
        Resolutions.put("84", new Resolution("84", "720p", "mp4", "normal", ResolutionNote.MHD));
        Resolutions.put("85", new Resolution("85", "1080p", "mp4", "normal", ResolutionNote.MHD));
        Resolutions.put("100", new Resolution("100", "360p", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("101", new Resolution("101", "480p", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("102", new Resolution("102", "720p", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("139", new Resolution("139", "Audio only", "m4a", "normal", ResolutionNote.MHD));
        Resolutions.put("140", new Resolution("140", "Audio only", "m4a", "normal", ResolutionNote.MHD));
        Resolutions.put("141", new Resolution("141", "Audio only", "m4a", "normal", ResolutionNote.MHD));
        Resolutions.put("171", new Resolution("313", "Audio only", "webm", "normal", ResolutionNote.MHD));
        Resolutions.put("172", new Resolution("313", "Audio only", "webm", "normal", ResolutionNote.MHD));
    }


    /**
     * 获取正则内容
     * @param content
     * @param pattern
     * @return
     */
    public static String getRegexString(String content, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(content);
        if (!matcher.find()) {
            return null;
        }
        LogUtils.i("group:" + matcher.group(0));
        return matcher.group(1);
    }


    /**
     * 转换编码
     * @param content
     * @param encoding
     * @return
     */
    public static String decode(String content, String encoding) {
        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        try {
            return URLDecoder.decode(content, encoding);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }




    /**
     * 解析VideoId
     * @param url
     * @return
     */
    public static String extractVideoId(String url) {

        String str = null;

        try {
            Matcher matcher = Pattern.compile("(?:^|[^\\w-]+)([\\w-]{11})(?:[^\\w-]+|$)").matcher(url);
            if (matcher.find()) {
                str = matcher.group(1);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return str;

    }

    /**
     * 解析
     * @param scanner
     * @param encoding
     * @return
     */
    public static FmtStreamMap parseFmtStreamMap(Scanner scanner, String encoding) {

        FmtStreamMap streamMap = new FmtStreamMap();

        scanner.useDelimiter(PARAMETER_SEPARATOR);

        while (scanner.hasNext()) {

            String[] nameValue = scanner.next().split(NAME_VALUE_SEPARATOR);

            if (nameValue.length == 0 || nameValue.length > 2) {
                throw new IllegalArgumentException("bad parameter");
            }

            String name = decode(nameValue[0], encoding);

            String value = null;

            if (nameValue.length == 2) {
                value = decode(nameValue[1], encoding);
            }

            LogUtils.i("YoutubeUtils", "name:" + name + ",values:" + value);

            if (TextUtils.equals("fallback_host", name)) {
                streamMap.fallbackHost = value;
            }
            if (TextUtils.equals("s", name)) {
                streamMap.f602s = value;
            }
            if (TextUtils.equals("itag", name)) {
                streamMap.itag = value;
            }
            if (TextUtils.equals("type", name)) {
                streamMap.type = value;
            }
            if (TextUtils.equals("quality", name)) {
                streamMap.quality = value;
            }
            if (TextUtils.equals("url", name)) {
                streamMap.url = value;
            }
            if (TextUtils.equals("sig", name)) {
                streamMap.sig = value;
            }
            TextUtils.equals("signature", name);
            if (!TextUtils.isEmpty(streamMap.itag)) {
                streamMap.resolution = (Resolution) Resolutions.get(streamMap.itag);
            }
            if (streamMap.resolution != null) {
                streamMap.extension = streamMap.resolution.format;
            }
        }
        return streamMap;
    }




    /**
     * GET请求方法
     * @param url
     * @return
     */
    public static String getContent(String url){

        LogUtils.i("getContent:"+url);

        BufferedReader in = null;

        String result = "";

        try {

            URL localUrl = new URL(url);

            URLConnection conn = localUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");

            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setRequestProperty("user-agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");

            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = in.readLine()) != null) {
                result += line;
            }

            return result;

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

           // MyClosable.close(in);

           try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return null;
    }





}
