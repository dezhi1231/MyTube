package com.opposs.tube.listener;

import android.view.View;

/**
 * Created by xcl on 16/2/26.
 */
public interface OnItemClickListener<T> {

    void onItemClick(View v, int position, T t);

}
