package sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by student on 2018-04-16.
 */
public class Serialize {

    Ranking ps;

    Serialize(Ranking ps)throws IOException {
        ObjectOutputStream pl = null;

        try{
            pl=new ObjectOutputStream(new FileOutputStream("ranking.dat"));
            pl.writeObject(ps);
            pl.flush();
        }
        finally{
            if(pl!=null)
                pl.close();
        }
    }
}





