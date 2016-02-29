package com.opposs.tube.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

import com.opposs.tube.entity.VedioInfo;
import com.opposs.tube.listener.OnItemClickListener;
import com.opposs.tube.mytube.R;

import java.util.List;

import butterknife.Bind;

/**
 * Created by xcl on 16/2/26.
 */
public class DownloadListViewFragment extends FragmentActivity implements OnItemClickListener<VedioInfo>{

    @Bind(R.id.listView)
    ListView listView;

    private List<VedioInfo> mVedioInfos;

    @Override
    public void onCreate(Bundle savedInstanceState) {









        super.onCreate(savedInstanceState);
    }




    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onItemClick(View v, int position, VedioInfo vedioInfo) {




    }
}
