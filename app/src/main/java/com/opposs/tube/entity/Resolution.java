package com.opposs.tube.entity;


import com.opposs.tube.utils.YoutubeUtils;


/**
 * 分辨率实体类
 */
public class Resolution {

    public String format;
    public String id;
    public YoutubeUtils.ResolutionNote notes;
    public String resolution;
    public String type;

    public Resolution(String _id, String _resolution, String _format, String _type, YoutubeUtils.ResolutionNote _notes) {
        this.id = _id;
        this.resolution = _resolution;
        this.format = _format;
        this.type = _type;
        this.notes = _notes;
    }
}
