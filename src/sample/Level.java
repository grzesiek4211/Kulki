package sample;

import java.io.Serializable;

public class Level implements Comparable, Serializable {

    private String nick;
    private int points;

    public Level(String nick, int points) {
        this.nick = nick;
        this.points = points;
    }

    @Override
    public String toString() {
        return points + " pkt.\t\t" + nick;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(Object o) {
        int compareage=((Level)o).getPoints();
        /* For Ascending order*/
        return compareage-this.points;
    }
}
