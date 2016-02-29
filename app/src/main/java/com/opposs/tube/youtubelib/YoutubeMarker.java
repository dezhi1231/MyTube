package com.opposs.tube.youtubelib;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.opposs.tube.entity.FmtStreamMap;
import com.opposs.tube.http.AsyncTaskAssistant;
import com.opposs.tube.http.HttpRequest;
import com.opposs.tube.http.HttpRequestLister;
import com.opposs.tube.interfaces.IYoutubeMarker;
import com.opposs.tube.utils.LogUtils;
import com.opposs.tube.utils.MyClosable;
import com.opposs.tube.utils.YoutubeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * 解析Youtube资源核心类
 */
public class YoutubeMarker implements IYoutubeMarker {

    public static final int PARSE_SUCCESS = 0;

    public static final int PARSE_ERROR = 1;

    public static final String FUNCCALL = "([$\\w]+)=([$\\w]+)\\(((?:\\w+,?)+)\\)$";

    public static final String JSPLAYER = "ytplayer\\.config\\s*=\\s*([^\\n]+);";

    public static final String OBJCALL = "([$\\w]+).([$\\w]+)\\(((?:\\w+,?)+)\\)$";

    public static final String[] REGEX_PRE = new String[]{"*", ".", "?", "+", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\\", "/"};

    public static final String USERAGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)";

    public static final String WATCHV = "https://www.youtube.com/watch?v=%s";

    private Context context;

    private String vid;

    private boolean isDownload;

    private String tubeContent;

    public ParseListener parseListener;

    private DownloadManager downloadManager;


    public YoutubeMarker(Context context,String vid, boolean isDownload,ParseListener parseListener){

        this.context = context;

        this.vid = vid;

        this.isDownload = isDownload;

        this.parseListener = parseListener;

    }

    public YoutubeMarker(Context context){

        this.context = context;

    }

    /**
     * 解析回调
     */
    public interface ParseListener{

        void onParseComplete(int statusCode,List<FmtStreamMap> result);

    }


    /**
     * 测试下载类
     * @param fmtStreamMap
     */
    public void startDownload(FmtStreamMap fmtStreamMap){

        if(context==null || fmtStreamMap==null){
            return;
        }

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(fmtStreamMap.url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setVisibleInDownloadsUi(true);

        request.addRequestHeader("user-agent", USERAGENT);

        request.setTitle(fmtStreamMap.title);

        //MimeTypeMap.getSingleton().getMimeTypeFromExtension()

        //request.setMimeType()

        request.setDestinationInExternalPublicDir("tube_demo", fmtStreamMap.title+"."+fmtStreamMap.resolution.format);

        downloadManager.enqueue(request);

    }

    /**
     * 解析类
     */
    class YoutubeMarkerParse extends AsyncTask<Void,Void,List<FmtStreamMap>>{

        @Override
        protected List<FmtStreamMap> doInBackground(Void... params) {

            String url = String.format(WATCHV, YoutubeMarker.this.vid);

            String content = YoutubeUtils.getContent(url);

            if(TextUtils.isEmpty(content)){

                LogUtils.i("YoutubeMarkerParse:content is null ");

                return null;
            }

            Matcher matcher = Pattern.compile(YoutubeMarker.JSPLAYER,Pattern.MULTILINE).matcher(content);

            if(matcher.find()){

                LogUtils.i("YoutubeMarkerParse:matcher find() is true");

                try {

                    JSONObject ytplayerConfig  = new JSONObject(matcher.group(1));

                    String html5playerJS = ytplayerConfig.getJSONObject("assets").getString("js");

                    if (html5playerJS.startsWith("//")) {

                        html5playerJS = "http:" + html5playerJS;
                    }

                    JSONObject args = ytplayerConfig.getJSONObject("args");

                    String[] fmtArray = args.getString("url_encoded_fmt_stream_map").split(",");

                    List<FmtStreamMap> streamMaps = new ArrayList();

                    FmtStreamMap parseFmtStreamMap;

                    for (String fmt : fmtArray) {

                        parseFmtStreamMap = YoutubeUtils.parseFmtStreamMap(new Scanner(fmt), "utf-8");

                        parseFmtStreamMap.html5playerJS = html5playerJS;

                        parseFmtStreamMap.videoid = args.optString("video_id");

                        parseFmtStreamMap.title = args.optString("title");

                        if (parseFmtStreamMap.resolution != null) {
                            streamMaps.add(parseFmtStreamMap);
                        }
                    }

                    for (String fmt2 : args.getString("adaptive_fmts").split(",")) {
                        parseFmtStreamMap = YoutubeUtils.parseFmtStreamMap(new Scanner(fmt2), "utf-8");
                        parseFmtStreamMap.html5playerJS = html5playerJS;
                        parseFmtStreamMap.videoid = args.optString("video_id");
                        parseFmtStreamMap.title = args.optString("title");
                        if (parseFmtStreamMap.resolution != null) {
                            streamMaps.add(parseFmtStreamMap);
                        }
                    }

                    LogUtils.i("streamMaps:Size:"+(streamMaps==null?0:streamMaps.size()));

                    return streamMaps;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<FmtStreamMap> fmtStreamMaps) {

            if(fmtStreamMaps!=null && fmtStreamMaps.size()>0){

                LogUtils.i("PARSE_SUCCESS:");

                YoutubeMarker.this.parseListener.onParseComplete(YoutubeMarker.PARSE_SUCCESS, fmtStreamMaps);

                for (FmtStreamMap fmt:fmtStreamMaps) {

                    LogUtils.i("type:"+fmt.type);

                    LogUtils.i("resolution.format:"+fmt.resolution.format);

                    LogUtils.i("resolution.resolution:"+fmt.resolution.resolution);

                    LogUtils.i("resolution.type:"+fmt.resolution.type);


                }
            }else {

                LogUtils.i("PARSE_ERROR:");

                YoutubeMarker.this.parseListener.onParseComplete(YoutubeMarker.PARSE_ERROR, fmtStreamMaps);
            }


            super.onPostExecute(fmtStreamMaps);
        }

    }

    @Override
    public void startParse() {
        new YoutubeMarkerParse().execute();
    }
}
