package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class Ranking implements Serializable {
    private ArrayList<Level> ranking5;
    private ArrayList<Level> ranking7;
    private ArrayList<Level> ranking9;


    public Ranking(ArrayList<Level> ranking5, ArrayList<Level> ranking7, ArrayList<Level> ranking9) {
        this.ranking5 = ranking5;
        this.ranking7 = ranking7;
        this.ranking9 = ranking9;
    }

    public Ranking() {
    }

    public ArrayList<Level> getRanking5() {
        return ranking5;
    }

    public ArrayList<Level> getRanking7() {
        return ranking7;
    }

    public ArrayList<Level> getRanking9() {
        return ranking9;
    }

    public void setRanking5(ArrayList<Level> ranking5) {
        this.ranking5 = ranking5;
    }

    public void setRanking7(ArrayList<Level> ranking7) {
        this.ranking7 = ranking7;
    }

    public void setRanking9(ArrayList<Level> ranking9) {
        this.ranking9 = ranking9;
    }
}
