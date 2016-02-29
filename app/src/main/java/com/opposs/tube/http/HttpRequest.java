package com.opposs.tube.http;

import android.content.Context;
import com.opposs.tube.utils.MyClosable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xcl on 16/2/24.
 */
public class HttpRequest implements Runnable{

    public static final int SUCCESS = 1;

    public static final int ERROR = 2;

    private Context context;

    private String rawParams = null;

    private String url = null;

    private HttpRequestLister httpRequestLister;


    /**
     * GET请求构造方法
     * @param context
     * @param url
     * @param httpRequestLister
     */
    public HttpRequest(Context context,String url,HttpRequestLister httpRequestLister){

        this.context = context;

        this.url = url;

        this.httpRequestLister = httpRequestLister;

    }

    public HttpRequest(String url){

        this.url = url;
    }


    /**
     * POST 请求方法构造器
     * @param context
     * @param url
     * @param rawParams
     * @param httpRequestLister
     */
    public HttpRequest(Context context,String url,String rawParams,HttpRequestLister httpRequestLister){

        this.context = context;

        this.url = url;

        this.rawParams = rawParams;

        this.httpRequestLister = httpRequestLister;


    }


    /**
     * GET请求方法
     * @param url
     * @return
     */
    private void getRequest(String url){

        BufferedReader in = null;

        String result = "";

        try {

            URL localUrl = new URL(url);

            URLConnection conn = localUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");

            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");

            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));

            String line = "";

            while ((line = in.readLine()) != null) {
                result += line;
            }

            this.httpRequestLister.doRequestResult(SUCCESS,result);

        } catch (Exception e) {

            this.httpRequestLister.doRequestResult(ERROR,result);

            e.printStackTrace();

        } finally {

            MyClosable.close(in);

           /* try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }*/
        }
    }


    /**
     * POST请求方法
     * @param url
     * @param rawParams
     * @return
     */
    private void postRequest(String url,String rawParams){

        PrintWriter out = null;

        BufferedReader in = null;

        String result = "";

        try {

            URL localurl = new URL(url);

            URLConnection conn = localurl.openConnection();

            conn.setRequestProperty("accept", "*/*");

            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");

            conn.setDoOutput(true);

            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());

            out.print(rawParams);

            out.flush();

            in = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                result += line;
            }

            this.httpRequestLister.doRequestResult(SUCCESS,result);

        } catch (Exception e) {

            this.httpRequestLister.doRequestResult(ERROR,result);

            e.printStackTrace();
        } finally {

           // MyClosable.close(out);

           // MyClosable.close(in);

            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {

                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

    }
}
