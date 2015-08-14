package eu.gyurasz.mariaradio.program;

import java.util.Date;

public class Program {
    private Date dateTime;
    private String title, description;
    private boolean current;

    public Program(String title, String description, Date dateTime) {
        this.title = new String(title);
        this.description = new String(description);
        this.dateTime = new Date(dateTime.getTime());
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
