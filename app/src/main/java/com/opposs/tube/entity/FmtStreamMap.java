package com.opposs.tube.entity;

public class FmtStreamMap {
    public boolean encrypted;
    public String extension;
    public String fallbackHost;
    public String html5playerJS;
    public String itag;
    public String mediatype;
    public String quality;
    public String realUrl;
    public Resolution resolution;
    public String f602s;
    public String sig;
    public String title;
    public String type;
    public String url;
    public CharSequence videoid;

    public String toString() {
        return "FmtStreamMap [fallbackHost=" + this.fallbackHost + ", s=" + this.f602s + ", itag=" + this.itag + ", type=" + this.type + ", quality=" + this.quality + ", url=" + this.url + ", sig=" + this.sig + ", title=" + this.title + ", mediatype=" + this.mediatype + ", encrypted=" + this.encrypted + ", extension=" + this.extension + ", resolution=" + this.resolution + ", html5playerJS=" + this.html5playerJS + ", videoid=" + this.videoid + "]";
    }

    public String getStreamString() {
        if (this.resolution == null) {
            return null;
        }
        return String.format("%s (%s)", new Object[]{this.extension, this.resolution.resolution});
    }
}
