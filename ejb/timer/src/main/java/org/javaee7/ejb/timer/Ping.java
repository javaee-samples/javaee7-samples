package org.javaee7.ejb.timer;

public class Ping {

    private String timeInfo;
    private long time = System.currentTimeMillis();

    public Ping(String s) {
        this.timeInfo = s;
    }

    public long getTime() {
        return time;
    }

    public String getTimeInfo() {
        return timeInfo;
    }

    @Override
    public String toString() {
        return "Ping {" +
            "timeInfo='" + timeInfo + '\'' +
            ", time=" + time +
            '}';
    }
}
