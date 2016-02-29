package com.opposs.tube.youtubelib;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.opposs.tube.entity.FmtStreamMap;
import com.opposs.tube.utils.LogUtils;
import com.opposs.tube.utils.YoutubeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xcl on 16/2/27.
 *
 * 改为识别后，进程开始
 *
 *
 *
 *
 *
 */
public class YoutubeMarkerVersion2 extends AsyncTask<Void,Void,List<FmtStreamMap>> {

    public static final String FUNCCALL = "([$\\w]+)=([$\\w]+)\\(((?:\\w+,?)+)\\)$";

    public static final String JSPLAYER = "ytplayer\\.config\\s*=\\s*([^\\n]+);";

    public static final String OBJCALL = "([$\\w]+).([$\\w]+)\\(((?:\\w+,?)+)\\)$";

    public static final String[] REGEX_PRE = new String[]{"*", ".", "?", "+", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\\", "/"};

    public static final String USERAGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)";

    public static final String WATCHV = "https://www.youtube.com/watch?v=%s";

    private Context context;

    private String vid;

    public ParseListener parseListener;

    public YoutubeMarkerVersion2(Context context,String vid,ParseListener parseListener){

        this.context = context;

        this.vid = vid;

        this.parseListener = parseListener;

    }

    @Override
    protected List<FmtStreamMap> doInBackground(Void... params) {


        if(TextUtils.isEmpty(this.vid)){

            LogUtils.i("doInBackground,error,vid is null ");

            return null;

        }

        String url = String.format(WATCHV, this.vid);

        String content = YoutubeUtils.getContent(url);

        if(TextUtils.isEmpty(content)){

            LogUtils.i("YoutubeMarkerParse:content is null ");

            return null;
        }

        Matcher matcher = Pattern.compile(YoutubeMarker.JSPLAYER, Pattern.MULTILINE).matcher(content);

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

    /**
     * 解析回调
     */
    public interface ParseListener{

        void onParseComplete(int statusCode,List<FmtStreamMap> result);

    }



    @Override
    protected void onPostExecute(List<FmtStreamMap> fmtStreamMaps) {

        if(fmtStreamMaps!=null && fmtStreamMaps.size()>0){

            LogUtils.i("PARSE_SUCCESS:");

            this.parseListener.onParseComplete(YoutubeMarker.PARSE_SUCCESS, fmtStreamMaps);

            for (FmtStreamMap fmt:fmtStreamMaps) {

                LogUtils.i("type:"+fmt.type);

                LogUtils.i("resolution.format:"+fmt.resolution.format);

                LogUtils.i("resolution.resolution:"+fmt.resolution.resolution);

                LogUtils.i("resolution.type:"+fmt.resolution.type);


            }
        }else {

            LogUtils.i("PARSE_ERROR:");

            this.parseListener.onParseComplete(YoutubeMarker.PARSE_ERROR, fmtStreamMaps);
        }


        super.onPostExecute(fmtStreamMaps);
    }
}
