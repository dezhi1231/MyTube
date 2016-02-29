package com.opposs.tube.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.opposs.tube.entity.VedioInfo;
import com.opposs.tube.listener.OnItemClickListener;
import com.opposs.tube.mytube.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xcl on 16/2/26.
 */
public class DownloadListViewAdapter extends BaseAdapter{

    private List<VedioInfo> mVedioInfos;

    private OnItemClickListener mListener;

    public DownloadListViewAdapter(){

        mVedioInfos = new ArrayList<VedioInfo>();

    }

    public void setData(List<VedioInfo> vedioInfos){

        this.mVedioInfos.clear();

        this.mVedioInfos.addAll(vedioInfos);

    }

    @Override
    public int getCount() {
        return mVedioInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mVedioInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vedio,parent,false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final VedioInfo vedioInfo = mVedioInfos.get(position);

        holder.tvName.setText(vedioInfo.getName());

        holder.tvDownloadPerSize.setText(vedioInfo.getDownloadPerSize());

        holder.tvStatus.setText(vedioInfo.getStatusText());

        holder.progressBar.setProgress(vedioInfo.getProgress());

        holder.btnDownload.setText(vedioInfo.getButtonText());

        //Pica





        return null;
    }


    /**
     * 视图 List Item
     */
    public final static class ViewHolder{

        @Bind(R.id.ivIcon)
        public ImageView ivIcon;

        @Bind(R.id.tvName)
        public TextView tvName;

        @Bind(R.id.btnDownload)
        public Button btnDownload;

        @Bind(R.id.tvDownloadPerSize)
        public TextView tvDownloadPerSize;

        @Bind(R.id.tvStatus)
        public TextView tvStatus;

        @Bind(R.id.progressBar)
        public ProgressBar progressBar;

        public ViewHolder(View itemView){
            ButterKnife.bind(this,itemView);
        }

    }



}
