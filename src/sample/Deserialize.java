package sample;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by student on 2018-04-16.
 */
public class Deserialize {

    public static Ranking odczyt() throws IOException, ClassNotFoundException {
        ObjectInputStream pl2 = null;
        Ranking ps = null;
        try {
            pl2 = new ObjectInputStream(new FileInputStream("ranking.dat"));
            ps = (Ranking) pl2.readObject();

            ArrayList<Level> ranking5 = ps.getRanking5();
            ArrayList<Level> ranking7 = ps.getRanking7();
            ArrayList<Level> ranking9 = ps.getRanking9();



        } catch (EOFException ex) {
            System.out.println("Koniec pliku");
        } finally {
            if (pl2 != null)
                pl2.close();
        }

        return ps;
    }

}
