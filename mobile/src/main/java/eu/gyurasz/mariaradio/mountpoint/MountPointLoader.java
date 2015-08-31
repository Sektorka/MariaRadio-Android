package eu.gyurasz.mariaradio.mountpoint;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import eu.gyurasz.mariaradio.Consts;
import eu.gyurasz.mariaradio.ILoaded;

public class MountPointLoader extends AsyncTask<ILoaded, Exception, List<MountPoint>> {
    private List<MountPoint> mMountPoints;
    private ILoaded[] mCallbacks;

    public MountPointLoader(List<MountPoint> mountPoints){
        mMountPoints = mountPoints;
    }

    @Override
    protected List<MountPoint> doInBackground(ILoaded... callbacks) {
        mCallbacks = callbacks;

        try {
            Connection conn = Jsoup.connect(Consts.MOUNTPOINTS_URL);
            conn.timeout(Consts.TIMEOUT);
            //conn.userAgent()

            Document doc = conn.get();

            Elements divs = doc.getElementsByClass("roundcont");

            mMountPoints.clear();

            for (Element div: divs) {
                if(isCancelled()){
                    break;
                }

                Elements dataElements = div.getElementsByClass("streamdata");
                Elements aElements = div.getElementsByTag("a");

                if(dataElements != null && dataElements.size() == 10) {
                    int bitrate = Integer.parseInt(dataElements.get(MountPoint.INDEX_BITRATE).text());

                    if(bitrate < Consts.MIN_BITRATE){
                        continue;
                    }

                    String m3u = aElements.get(0).attributes().get("href");

                    m3u = (m3u != null ? m3u.substring(0, m3u.lastIndexOf(".")) : "");
                    m3u = Consts.M3U_PRE_URL + m3u;

                    mMountPoints.add(
                        new MountPoint(
                            dataElements.get(MountPoint.INDEX_TITLE).text(),
                            dataElements.get(MountPoint.INDEX_DESCRIPTION).text(),
                            dataElements.get(MountPoint.INDEX_CONTENT_TYPE).text(),
                            new Date(dataElements.get(MountPoint.INDEX_UPTIME).text()),
                            bitrate,
                            Integer.parseInt(dataElements.get(MountPoint.INDEX_CURRENTLISTENERS).text()),
                            Integer.parseInt(dataElements.get(MountPoint.INDEX_PEAKLISTENERS).text()),
                            dataElements.get(MountPoint.INDEX_GENRE).text(),
                            dataElements.get(MountPoint.INDEX_URL).text(),
                            dataElements.get(MountPoint.INDEX_CURRENT_SONG).text(),
                            m3u
                        )
                    );
                }
            }
        }
        catch (Exception e){
            publishProgress(e);
        }

        if(isCancelled()){
            mMountPoints.clear();
        }

        return mMountPoints;
    }

    @Override
    protected void onProgressUpdate(Exception... ex) {
        for(Exception e: ex){
            for(ILoaded mountPointILoaded: mCallbacks){
                mountPointILoaded.OnException(e);
            }
        }
    }

    @Override
    protected void onPostExecute(List<MountPoint> mountPoints) {
        for(ILoaded mountPointILoaded: mCallbacks){
            mountPointILoaded.<MountPoint>Loaded(mountPoints);
        }
    }
}
