package eu.gyurasz.mariaradio.program;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import eu.gyurasz.mariaradio.Consts;
import eu.gyurasz.mariaradio.ILoaded;

public class ProgramLoader extends AsyncTask<ILoaded, Exception, List<Program>> {
    private List<Program> mPrograms;
    private ILoaded[] mCallbacks;

    public ProgramLoader(List<Program> programs){
        mPrograms = programs;
    }

    @Override
    protected List<Program> doInBackground(ILoaded... callbacks) {
        mCallbacks = callbacks;

        try {
            Date dateTime = new Date();
            Date now = new Date();

            Connection conn = Jsoup.connect(String.format(Consts.PROGRAMS_URL, new SimpleDateFormat("yyyyMMdd").format(dateTime)));
            conn.timeout(Consts.TIMEOUT);
            //conn.userAgent()

            Document doc = conn.get();
            final String br = "|||";
            boolean gotCurrent = false;

            mPrograms.clear();

            Elements progDates = doc.getElementsByClass("mlistaido");
            Elements progNamesTitles = doc.getElementsByClass("mlistacim");

            for(int i = 0; i < progDates.size() && i < progNamesTitles.size() && !isCancelled(); i++){
                String time = progDates.get(i).text();
                String txt = progNamesTitles.get(i).html();

                txt = txt.replace("<br>",br);
                txt = Jsoup.parse(txt).text();

                int hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
                int mins = Integer.parseInt(time.substring(time.indexOf(":")+1));

                dateTime.setHours(hours);
                dateTime.setMinutes(mins);

                Program program = new Program(
                    txt.substring(0,txt.indexOf(br)),
                    txt.substring(txt.indexOf(br)+br.length()),
                    dateTime
                );

                mPrograms.add(program);

                if(!gotCurrent && now.compareTo(program.getDateTime()) < 0){
                    if(mPrograms.size() == 1){
                        mPrograms.get(0).setCurrent(true);
                    }
                    else{
                        mPrograms.get(mPrograms.size()-2).setCurrent(true);
                    }

                    gotCurrent = true;
                }
            }


            for (Iterator<Program> iter = mPrograms.listIterator(); iter.hasNext() && !isCancelled(); ) {
                Program program = iter.next();
                if(now.compareTo(program.getDateTime()) > 0 && !program.isCurrent()){
                    iter.remove();
                }
            }

            if(isCancelled()){
                mPrograms.clear();
            }

        } catch (Exception e) {
            publishProgress(e);
        }

        return mPrograms;
    }

    @Override
    protected void onProgressUpdate(Exception... ex) {
        for(Exception e: ex) {
            for (ILoaded mountPointILoaded : mCallbacks) {
                mountPointILoaded.OnException(e);
            }
        }
    }

    @Override
    protected void onPostExecute(List<Program> programs) {
        for(ILoaded programLoaded: mCallbacks){
            programLoaded.<Program>Loaded(programs);
        }
    }
}
