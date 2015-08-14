package eu.gyurasz.mariaradio.program;

import android.os.AsyncTask;

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

public class ProgramLoader extends AsyncTask<ProgramLoaded, Void, List<Program>> {
    List<Program> mPrograms;
    private ProgramLoaded[] mCallbacks;

    public ProgramLoader(List<Program> programs){
        mPrograms = programs;
    }

    @Override
    protected List<Program> doInBackground(ProgramLoaded... callbacks) {
        mCallbacks = callbacks;

        try {
            Date dateTime = new Date();
            Date now = new Date();
            Document doc = Jsoup.connect(String.format(Consts.PROGRAMS_URL, new SimpleDateFormat("yyyyMMdd").format(dateTime)) ).get();
            final String br = "|||";
            boolean gotCurrent = false;


            Elements progDates = doc.getElementsByClass("mlistaido");
            Elements progNamesTitles = doc.getElementsByClass("mlistacim");

            for(int i = 0; i < progDates.size() && i < progNamesTitles.size(); i++){
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


            for (Iterator<Program> iter = mPrograms.listIterator(); iter.hasNext(); ) {
                Program program = iter.next();
                if(now.compareTo(program.getDateTime()) > 0 && !program.isCurrent()){
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mPrograms;
    }

    @Override
    protected void onPostExecute(List<Program> programs) {
        for(ProgramLoaded programLoaded: mCallbacks){
            programLoaded.Loaded(programs);
        }
    }
}
