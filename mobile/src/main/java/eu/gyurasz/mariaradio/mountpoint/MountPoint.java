package eu.gyurasz.mariaradio.mountpoint;

import java.util.Date;

public class MountPoint {
    private String title, description, contentType, genre, url, currentSong, streamUrl;
    private Date upTime;
    private int bitrate, currentListeners, peakListeners;

    public final static int INDEX_TITLE = 0;
    public final static int INDEX_DESCRIPTION = 1;
    public final static int INDEX_CONTENT_TYPE = 2;
    public final static int INDEX_UPTIME = 3;
    public final static int INDEX_BITRATE = 4;
    public final static int INDEX_CURRENTLISTENERS = 5;
    public final static int INDEX_PEAKLISTENERS = 6;
    public final static int INDEX_GENRE = 7;
    public final static int INDEX_URL = 8;
    public final static int INDEX_CURRENT_SONG = 9;

    public MountPoint(String title, String description, String contentType, Date upTime, int bitrate, int currentListeners, int peakListeners, String genre, String url, String currentSong, String streamUrl)
    {
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.upTime = upTime;
        this.bitrate = bitrate;
        this.currentListeners = currentListeners;
        this.peakListeners = peakListeners;
        this.genre = genre;
        this.url = url;
        this.currentSong = currentSong;
        this.streamUrl = streamUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }

    public Date getUpTime() {
        return upTime;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getCurrentListeners() {
        return currentListeners;
    }

    public int getPeakListeners() {
        return peakListeners;
    }

    public String getGenre() {
        return genre;
    }

    public String getUrl() {
        return url;
    }

    public String getCurrentSong() {
        return currentSong;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    @Override
    public String toString() {
        return String.format("%1$s, %2$d kbit/s", title, bitrate);
    }
}
