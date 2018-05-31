package sample;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Controller {
    private int rowHeight;
    private int rowWidth;

    int windowWidth=500;
    int windowHeight=500;

    private List<NetNode> siatka;
    private List<Ball> ballsList;
    @FXML
    public Pane stackPane;


    public void onMouseClicked(MouseEvent mouseEvent)
    {
       if(!checkForGroups());
          createBalls();
       //closestShot(mouseEvent.getX(),mouseEvent.getY());
        //checkForGroups();
       /* if(!checkForGroups())
        */   // createBallOnClick((int)mouseEvent.getX(),(int)mouseEvent.getY());
       // checkForGroups();
       // checkForGroups();
    }

    private void createArea()
    {
        rowHeight = windowHeight/11;
        rowWidth = windowWidth/9;
        for( int i = 2*rowHeight; i < windowHeight-rowHeight ; i+=rowHeight)
        {
            for( int j = rowWidth; j < windowWidth ; j+=rowWidth)
            {
                NetNode node = new NetNode(j,i,false);
                siatka.add(node);
            }
        }
    }

    private boolean isEmptySpace(int x, int y, int radius)
    {
        for(Ball b : ballsList)
        {
            double odl = Math.sqrt( Math.pow(x-(int)b.getLayoutX(),2) + Math.pow(y-(int)b.getLayoutY(),2) );
            if(odl<2*radius)
                return false;
        }
        return true;
    }

    private NetNode losujPoints(int radius)
    {
        Random rand = new Random();
        NetNode point;
        int index = rand.nextInt(81) ;
        point = siatka.get(index);
        if (isEmptySpace(point.x, point.y, radius))
        {
            return point;
        }
       return null;
    }

    private int drawColor()
    {
        Random rand = new Random();
        int i = rand.nextInt(4) ;
        return i;
    }

    private void createBalls()
    {
        int j = 0;
        while (j < 3 && !isFieldFull()) {
                int radius = 15;
                NetNode point = losujPoints(radius);
                if (point != null) {
                    Ball b = new Ball(point.x, point.y, radius, drawColor());
                    b.getLight();

                    stackPane.getChildren().add(b);
                    ballsList.add(b);
                    int index = siatka.indexOf(point);
                    point.isTaken=true;
                    siatka.set(index, point);
                    j++;
                }
        }
    }

    private void createBallOnClick(int x, int y)
    {
            if(!isFieldFull()) {
                int radius = 15;
                NetNode point = closestShot(x,y);
                if (point != null) {
                    if (isEmptySpace(point.x, point.y, radius)) {

                        Ball b = new Ball(point.x, point.y, radius, drawColor());
                        b.getLight();
                        stackPane.getChildren().add(b);
                        ballsList.add(b);

                        int index = siatka.indexOf(point);
                        point.isTaken = true;
                        siatka.set(index, point);
                    }
                }
            }
    }

    private NetNode closestShot(double x, double y)
    {
        NetNode point= new NetNode();
        double min = 1000;

        for( NetNode p: siatka)
        {
            double odl = Math.sqrt( Math.pow((int)x-p.x,2) + Math.pow((int)y-p.y,2) );
            if(odl<min)
            {
                min=odl;
                point = p;
            }
        }
        return point;
    }

    private boolean isFieldFull()
    {
        if (this.ballsList.size() == 81)
            return true;

        return false;
    }

    private int rekursiveCheckForGroupMember(int index, int direction, int countOfBallsInRow, int color)
    {
        NetNode p = siatka.get(index);
        Ball b = null;

        if ( index<81 && p.isTaken )
        {
            Iterator<Ball> itr = ballsList.iterator();
            while(itr.hasNext())
            {
                b = itr.next();
                if (p.x == (int)b.getLayoutX() && p.y == (int)b.getLayoutY())
                {
                    break;
                }

            }
            if(b.color == color ) {
                countOfBallsInRow++;

                if (direction == 0) // patrzymy tyko w prawo.
                {
                    if (index + 1 < 81 && siatka.get(index).y == siatka.get(index + 1).y)
                        return rekursiveCheckForGroupMember(index + 1, direction, countOfBallsInRow, color);

                } else if (direction == 1) // patrzymy tyko w skosPrawy.
                {
                    if (index + 10 < 81 && siatka.get(index).x < siatka.get(index + 10).x)
                        return rekursiveCheckForGroupMember(index + 10, direction, countOfBallsInRow, color);
                } else if (direction == 2) // patrzymy tyko w w dół.
                {
                    if (index + 9 < 81 && siatka.get(index).x == siatka.get(index + 9).x)
                        return rekursiveCheckForGroupMember(index + 9, direction, countOfBallsInRow, color);
                } else if (direction == 3) // patrzymy tyko w skosLewy.
                {
                    if (index + 8 < 81 && siatka.get(index).x > siatka.get(index + 8).x)
                        return rekursiveCheckForGroupMember(index + 8, direction, countOfBallsInRow, color);
                }
            }
        }
        else return countOfBallsInRow;

        return countOfBallsInRow;
    }

    private boolean checkForGroups()
    {
        boolean hasRemovedSomething = false;
        for(NetNode p : siatka)
        {
            int color = 0;
            int countOfBallsInPoziom = 0;
            int countOfBallsInSkosPrawy = 0;
            int countOfBallsInSkosLewy = 0;
            int countOfBallsInPion = 0;
            int index = siatka.indexOf(p);

            Iterator<Ball> itr = ballsList.iterator();
            while(itr.hasNext())
            {
                Ball b = itr.next();
                if (p.x == (int)b.getLayoutX() && p.y == (int)b.getLayoutY())
                {
                    color = b.color;
                    break;
                }

            }

            countOfBallsInPoziom = rekursiveCheckForGroupMember( index , 0,  countOfBallsInPoziom, color);
            countOfBallsInSkosPrawy = rekursiveCheckForGroupMember( index , 1,  countOfBallsInSkosPrawy, color);
            countOfBallsInPion = rekursiveCheckForGroupMember( index , 2,  countOfBallsInPion, color);
            countOfBallsInSkosLewy = rekursiveCheckForGroupMember( index , 3,  countOfBallsInSkosLewy, color);


            if(countOfBallsInPoziom>=3)
            {
                for( int i = index; i < index+countOfBallsInPoziom; i++) {
                    int countOfBallsInSkosPrawy2 = 0;
                    int countOfBallsInPion2 = 0;
                    int countOfBallsInSkosLewy2 = 0;

                    countOfBallsInSkosPrawy2 = rekursiveCheckForGroupMember(i, 1, countOfBallsInSkosPrawy2, color);
                    countOfBallsInPion2 = rekursiveCheckForGroupMember(i, 2, countOfBallsInPion2, color);
                    countOfBallsInSkosLewy2 = rekursiveCheckForGroupMember(i, 3, countOfBallsInSkosLewy2, color);

                    deleteBalls(i, countOfBallsInSkosPrawy2, 1);
                    deleteBalls(i, countOfBallsInPion2, 2);
                    deleteBalls(i, countOfBallsInSkosLewy2, 3);
                }
                deleteBalls(index,countOfBallsInPoziom,0);
                hasRemovedSomething = true;
            }
            //===============================  **********
            if(countOfBallsInSkosPrawy>=3)
            {
                for (int i = index; i < index + countOfBallsInSkosPrawy*10; i+=10)
                {
                    int countOfBallsInPoziom2 = 0;
                    int countOfBallsInPion2 = 0;
                    int countOfBallsInSkosLewy2 = 0;

                    countOfBallsInPoziom2 = rekursiveCheckForGroupMember(i, 1, countOfBallsInPoziom2, color);
                    countOfBallsInPion2 = rekursiveCheckForGroupMember(i, 2, countOfBallsInPion2, color);
                    countOfBallsInSkosLewy2 = rekursiveCheckForGroupMember(i, 3, countOfBallsInSkosLewy2, color);

                    deleteBalls(i, countOfBallsInPoziom2, 1);
                    deleteBalls(i, countOfBallsInPion2, 2);
                    deleteBalls(i, countOfBallsInSkosLewy2, 3);
                }
                deleteBalls(index,countOfBallsInSkosPrawy,1);
                hasRemovedSomething = true;
            }
            //===============================   **********
            if(countOfBallsInPion>=3)
            {
                for (int i = index; i < index + countOfBallsInPion*9; i+=9)
                {
                    int countOfBallsInPoziom2 = 0;
                    int countOfBallsInSkosPrawy2 = 0;
                    int countOfBallsInSkosLewy2 = 0;

                    countOfBallsInPoziom2 = rekursiveCheckForGroupMember(i, 1, countOfBallsInPoziom2, color);
                    countOfBallsInSkosPrawy2 = rekursiveCheckForGroupMember(i, 2, countOfBallsInSkosPrawy2, color);
                    countOfBallsInSkosLewy2 = rekursiveCheckForGroupMember(i, 3, countOfBallsInSkosLewy2, color);

                    deleteBalls(i, countOfBallsInPoziom2, 1);
                    deleteBalls(i, countOfBallsInSkosPrawy2, 2);
                    deleteBalls(i, countOfBallsInSkosLewy2, 3);
                }
                deleteBalls(index,countOfBallsInPion,2);
                hasRemovedSomething = true;
            }
            //===============================
            if(countOfBallsInSkosLewy>=3)
            {
                for (int i = index; i < index + countOfBallsInSkosLewy*8; i+=8)
                {
                    int countOfBallsInPoziom2 = 0;
                    int countOfBallsInSkosPrawy2 = 0;
                    int countOfBallsInPion2 = 0;

                    countOfBallsInPoziom2 = rekursiveCheckForGroupMember(i, 1, countOfBallsInPoziom2, color);
                    countOfBallsInSkosPrawy2 = rekursiveCheckForGroupMember(i, 2, countOfBallsInSkosPrawy2, color);
                    countOfBallsInPion2 = rekursiveCheckForGroupMember(i, 3, countOfBallsInPion2, color);

                    deleteBalls(i, countOfBallsInPoziom2, 1);
                    deleteBalls(i, countOfBallsInSkosPrawy2, 2);
                    deleteBalls(i, countOfBallsInPion2, 3);
                }

                deleteBalls(index, countOfBallsInSkosLewy, 3);
                hasRemovedSomething = true;
            }
        }
        return hasRemovedSomething;
    }

    private void deleteBalls(int index,int count, int direction)
    {
        if(direction == 0)  // poziom.
        {
            for (int i = index; i < index + count; i++)
            {
                NetNode p = siatka.get(i);
                p.isTaken = false;
                siatka.set(i, p);

                Iterator<Ball> itr = ballsList.iterator();
                while(itr.hasNext())
                {
                    Ball b = itr.next();
                    if (p.x == (int)b.getLayoutX() && p.y == (int)b.getLayoutY())
                    {

                        itr.remove();

                        stackPane.getChildren().remove(b);

                        break;
                    }

                }
            }
        }
        else if(direction == 1)  // skosPrawy.
        {
            for (int i = index; i < index + count*10; i+=10)
            {
                NetNode p = siatka.get(i);
                if(p==null) System.out.println("asd");
                p.isTaken = false;
                siatka.set(i, p);
                Iterator<Ball> itr = ballsList.iterator();
                while(itr.hasNext())
                {
                    Ball b = itr.next();
                    if (p.x == (int)b.getLayoutX() && p.y == (int)b.getLayoutY())
                    {
                        itr.remove();
                        stackPane.getChildren().remove(b);
                        System.out.print("");
                        break;
                    }
                }
            }
        }
        else if(direction == 2)  // pion.
        {
            for (int i = index; i < index + count*9; i+=9)
            {
                System.out.print("");

                NetNode p = siatka.get(i);
                if(p==null) System.out.println("asd");
                p.isTaken = false;
                siatka.set(i, p);
                Iterator<Ball> itr = ballsList.iterator();
                while(itr.hasNext())
                {
                    Ball b = itr.next();
                    if (p.x == (int)b.getLayoutX() && p.y == (int)b.getLayoutY())
                    {
                        itr.remove();
                        System.out.print("");
                        stackPane.getChildren().remove(b);
                        break;
                    }
                }
            }
        }
        else if(direction == 3)  // skosLewy
        {
            for (int i = index; i < index + count*8; i+=8)
            {
                NetNode p = siatka.get(i);
                if(p==null) System.out.println("asd");
                p.isTaken = false;
                siatka.set(i, p);
                Iterator<Ball> itr = ballsList.iterator();
                while(itr.hasNext())
                {
                    Ball b = itr.next();
                    if (p.x == (int)b.getLayoutX() && p.y == (int)b.getLayoutY())
                    {
                        System.out.println(i);
                        itr.remove();
                        stackPane.getChildren().remove(b);

                        break;
                    }
                    else {

                    }
                }
            }
        }
        System.out.println("");
    }

    public void initialize()
    {
        this.ballsList = new ArrayList<>();
        this.siatka = new ArrayList<>();
        createArea();
        createBalls();


    }
}
