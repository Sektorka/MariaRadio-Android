package eu.gyurasz.mariaradio;

public class Consts {
    private Consts(){}

    public static final String BASE_URL = "http://www.mariaradio.hu";
    public static final String PROGRAMS_URL = BASE_URL + "/musorok/musornaptar/%1$s";
    public static final String MOUNTPOINTS_URL = BASE_URL + ":8000/status.xsl";
    public static final String M3U_PRE_URL = BASE_URL + ":8000";
    public static final String USER_AGENT = "mariaradio_v1.0";

    public static final int TIMEOUT = 10000; //10 secs
    public static final int MIN_BITRATE = 56;
}
