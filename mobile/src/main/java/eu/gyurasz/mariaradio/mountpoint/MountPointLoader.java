package eu.gyurasz.mariaradio.mountpoint;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import eu.gyurasz.mariaradio.Consts;
import eu.gyurasz.mariaradio.ILoaded;

public class MountPointLoader extends AsyncTask<ILoaded<MountPoint>, Void, List<MountPoint>> {
    private List<MountPoint> mMountPoints;
    private ILoaded<MountPoint>[] mCallbacks;

    public MountPointLoader(List<MountPoint> mountPoints){
        mMountPoints = mountPoints;
    }

    @Override
    protected List<MountPoint> doInBackground(ILoaded<MountPoint>... callbacks) {
        mCallbacks = callbacks;

        try {
            Document doc = Jsoup.connect(Consts.MOUNTPOINTS_URL).get();

            Elements divs = doc.getElementsByClass("roundcont");

            mMountPoints.clear();

            for (Element div: divs) {
                Elements dataElements = div.getElementsByClass("streamdata");

                if(dataElements != null && dataElements.size() == 10) {
                    mMountPoints.add(
                        new MountPoint(
                            dataElements.get(MountPoint.INDEX_TITLE).text(),
                            dataElements.get(MountPoint.INDEX_DESCRIPTION).text(),
                            dataElements.get(MountPoint.INDEX_CONTENT_TYPE).text(),
                            new Date(dataElements.get(MountPoint.INDEX_UPTIME).text()),
                            Integer.parseInt(dataElements.get(MountPoint.INDEX_BITRATE).text()),
                            Integer.parseInt(dataElements.get(MountPoint.INDEX_CURRENTLISTENERS).text()),
                            Integer.parseInt(dataElements.get(MountPoint.INDEX_PEAKLISTENERS).text()),
                            dataElements.get(MountPoint.INDEX_GENRE).text(),
                            dataElements.get(MountPoint.INDEX_URL).text(),
                            dataElements.get(MountPoint.INDEX_CURRENT_SONG).text()
                        )
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mMountPoints;
    }

    @Override
    protected void onPostExecute(List<MountPoint> mountPoints) {
        for(ILoaded<MountPoint> mountPointILoaded: mCallbacks){
            mountPointILoaded.Loaded(mountPoints);
        }
    }
}
