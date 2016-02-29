package com.opposs.tube.mytube;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.ActionItemBadgeAdder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.typeface.IIcon;
import com.opposs.tube.entity.FmtStreamMap;
import com.opposs.tube.utils.LogUtils;
import com.opposs.tube.utils.YoutubeUtils;
import com.opposs.tube.view.YoutubeWebView;
import com.opposs.tube.youtubelib.YoutubeMarker;
import com.opposs.tube.youtubelib.YoutubeMarkerVersion2;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int MSG_HIDE_VIEW = 2;

    private static final int MSG_SHOW_VIEW = 1;

    private YoutubeWebView tuWebView;

    private ProgressDialog progressDialog;

   // private FloatingActionButton fab;

    public ImageView btn_back;

    public ImageView btn_forward;

    public ImageView btn_home;

    public ImageView btn_download;

    public ProgressBar progressBar;

    private Toolbar toolbar;

    private String vid;

    public static final String BASE_URL="https://m.youtube.com";

    public int position = 0;

    public int downloadingCount = 0;

    private static final int ACTION_SEARCH_ID = 34535;

    private static final int ACTION_DOWNLOAD_ID = 34536;

    public List<FmtStreamMap> fmtStreamMaps = null;

    public YoutubeMarkerVersion2 youtubeMarkerVersion2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btn_back:

                if(this.tuWebView.canGoBack()){

                    this.tuWebView.goBack();

                }

                break;

            case R.id.btn_forward:

                if(this.tuWebView.canGoForward()){
                    this.tuWebView.goForward();
                }

                break;
            case R.id.btn_download:

                LogUtils.i("btn_download:onClick");

                showProgressDialog();

                new YoutubeMarkerVersion2(getApplicationContext(), vid, new MyParseListener()).execute();

        }
    }


    /**
     *显示错误消息
     */
    private void showErrorDialog(){

        //异常   需要修改  Unable to add window -- token null is not for an application
        new AlertDialog.Builder(this).setMessage(getString(R.string.download_parse_err_message)).setTitle(getString(R.string.download_parse_err_message)).show();

    }


    /**
     * 显示 下载的视频格式
     */
    private void showSelectTypeDialog(final List<FmtStreamMap> fmtStreamMaps){

        dismissProgressDialog();

        /*String args[] = new String[fmtStreamMaps.size()];

        for(int i = 0;i<args.length;i++){
            args[i] = ((FmtStreamMap)fmtStreamMaps.get(i)).resolution.format+" ("+((FmtStreamMap)fmtStreamMaps.get(i)).resolution.resolution+")";
        }

        new AlertDialog.Builder(this).setTitle("选择类型").setSingleChoiceItems(args, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LogUtils.i("showSelectTypeDialog:"+which);

                position = which;

            }
        }).

        setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new YoutubeMarker(getApplicationContext()).startDownload(fmtStreamMaps.get(position));
            }
        }).setNegativeButton(getString(R.string.btn_cancel), null).show();*/

        DialogPlus dialogPlus = DialogPlus.newDialog(this).setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return fmtStreamMaps.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder viewHolder;

                View view = convertView;

                if (view == null) {

                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_common, parent, false);

                    viewHolder = new ViewHolder();

                    viewHolder.textView = (TextView) view.findViewById(R.id.text_view);

                    viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);

                    view.setTag(viewHolder);

                } else {

                    viewHolder = (ViewHolder) view.getTag();

                }

                viewHolder.textView.setText(((FmtStreamMap) fmtStreamMaps.get(position)).resolution.format + " (" + ((FmtStreamMap) fmtStreamMaps.get(position)).resolution.resolution + ")");

                return view;
            }
        }).setExpanded(true).setGravity(Gravity.CENTER).setCancelable(true).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

            }
        }).create();



        dialogPlus.show();

    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }


    /**
     * 显示等待提示框
     */
    private void showProgressDialog(){

        progressDialog.setTitle(getString(R.string.download_progressDialog_title));

        progressDialog.setMessage(getString(R.string.download_progressDialog_message));

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

    }

    /**
     * 关闭等待提示框
     *
     * java.lang.IllegalArgumentException: View=com.android.internal.policy.impl.PhoneWindow$DecorView{3f7904ac V.E..... R......D 0,0-456,162} not attached to window manager
     *
     */
    private void dismissProgressDialog(){

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }




    class TubeWebClient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {

            LogUtils.i("onPageFinished>>>>>>>>>>>>>>>>>>>>>>>>>>");

            super.onPageFinished(view, url);
        }



        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            showErrorDialog();

            super.onReceivedError(view, request, error);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //LogUtils.i("shouldOverrideUrlLoading:"+url);

            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {

            LogUtils.i("doUpdateVisitedHistory:" + isReload);

            LogUtils.i("doUpdateVisitedHistory:" + url);

            if(MainActivity.this.tuWebView!=null && !TextUtils.isEmpty(url)){

                updateButtonUI();

            }

            super.doUpdateVisitedHistory(view, url, isReload);

        }
    }


    /**
     * class TubeWebChromeClient

     */
    class TubeWebChromeClient extends WebChromeClient{

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if(!progressBar.isShown()){
                progressBar.setVisibility(View.VISIBLE);
            }

            progressBar.setProgress(newProgress);

            if(newProgress>=90){
                if(progressBar.isShown()){
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            LogUtils.i(newProgress + "");

            super.onProgressChanged(view, newProgress);
        }


    }


    /**
     * 初始化视图
     */
    private void initView(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        this.btn_back = (ImageView) findViewById(R.id.btn_back);

        this.btn_forward = (ImageView) findViewById(R.id.btn_forward);

        this.btn_download = (ImageView) findViewById(R.id.btn_download);

        this.btn_home = (ImageView) findViewById(R.id.btn_home);

        this.btn_home.setOnClickListener(this);

        this.btn_download.setOnClickListener(this);

        this.btn_back.setOnClickListener(this);

        this.btn_forward.setOnClickListener(this);

        tuWebView = (YoutubeWebView) findViewById(R.id.tubeView);

        progressDialog = new ProgressDialog(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initWebView();

    }


    /**
     * 更新UI的可用性
     */
    private void updateButtonUI(){

        String url = this.tuWebView.getUrl();

        LogUtils.i("updateButtonUI:"+url);

        if(TextUtils.isEmpty(url)){
            return;
        }

        String videoId = YoutubeUtils.extractVideoId(url);

        if(!TextUtils.isEmpty(videoId)){

            this.vid = videoId;

            if(!this.btn_download.isShown()){

                LogUtils.i("youtubeMarkerVersion2.getStatus():");

                //尝试 预解析
                /*if(youtubeMarkerVersion2==null){

                    youtubeMarkerVersion2 = new YoutubeMarkerVersion2(getApplicationContext(), vid, new MyParseListener());

                    youtubeMarkerVersion2.execute();

                }else{

                    LogUtils.i("youtubeMarkerVersion2.getStatus():"+youtubeMarkerVersion2.getStatus());

                    if(youtubeMarkerVersion2.getStatus()== AsyncTask.Status.FINISHED){

                        youtubeMarkerVersion2 = new YoutubeMarkerVersion2(getApplicationContext(), vid, new MyParseListener());

                        youtubeMarkerVersion2.execute();

                    }else{

                        LogUtils.i("(youtubeMarkerVersion2.getStatus):isCanceled:"+youtubeMarkerVersion2.isCancelled());

                        boolean isCanceled = youtubeMarkerVersion2.cancel(true);

                        LogUtils.i("youtubeMarkerVersion2.getStatus():"+isCanceled);

                    }


                    youtubeMarkerVersion2=null;
                }*/

                this.btn_download.setVisibility(View.VISIBLE);

                Animation shakeAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tran_y);

                this.btn_download.startAnimation(shakeAnim);

            }
        }else{
            if(this.btn_download.isShown()){
                this.btn_download.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 解析类型 监听
     */
    class MyParseListener implements YoutubeMarkerVersion2.ParseListener{
        @Override
        public void onParseComplete(int statusCode, List<FmtStreamMap> result) {

            dismissProgressDialog();

            if(statusCode==YoutubeMarker.PARSE_SUCCESS){
                //成功 展示Dialog
                showSelectTypeDialog(result);

               // MainActivity.this.fmtStreamMaps = result;

            }else{
                showErrorDialog();

            }
        }
    }


    /**
     * 初始化WebView设置
     */
    private void initWebView(){

        WebSettings settings = this.tuWebView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        settings.setAppCacheEnabled(false);

        settings.setUserAgentString(new StringBuilder(String.valueOf(this.tuWebView.getSettings().getUserAgentString())).append(" Rong/2.0").toString());

        settings.setSupportZoom(false);

        settings.setPluginState(WebSettings.PluginState.ON);

        settings.setLoadWithOverviewMode(true);

        settings.setBlockNetworkImage(true);

        tuWebView.setWebViewClient(new TubeWebClient());

        tuWebView.setWebChromeClient(new TubeWebChromeClient());

        tuWebView.loadUrl(BASE_URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        new ActionItemBadgeAdder().act(this).menu(menu).title(R.string.btn_web_forward_val).itemDetails(0, ACTION_SEARCH_ID, 1).
                showAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS).add(FontAwesome.Icon.faw_search, Integer.MIN_VALUE);

        new ActionItemBadgeAdder().act(this).menu(menu).title(R.string.btn_web_forward_val).itemDetails(0, ACTION_DOWNLOAD_ID, 1).
                showAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS).add(FontAwesome.Icon.faw_cloud_download, Integer.MIN_VALUE);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==ACTION_SEARCH_ID){

            Toast.makeText(this,"打开搜索界面", Toast.LENGTH_SHORT).show();

            return true;
        }

        if(id==ACTION_DOWNLOAD_ID){

            Toast.makeText(this,"打开下载界面", Toast.LENGTH_SHORT).show();

            ActionItemBadge.update(item, downloadingCount++);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
