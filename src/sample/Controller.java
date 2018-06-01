package sample;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.lang.reflect.Array;
import java.util.*;

public class Controller {
    private int rowHeight;
    private int rowWidth;

    int windowWidth=500;
    int windowHeight=500;

    private List<NetNode> siatka;
    private List<Ball> ballsList;

    private ArrayList<ArrayList<Ball>> poziomo = new ArrayList<>();
    private ArrayList<ArrayList<Ball>> skosprawy = new ArrayList<>();
    private ArrayList<ArrayList<Ball>> skoslewy = new ArrayList<>();
    private ArrayList<ArrayList<Ball>> dol = new ArrayList<>();
    private ArrayList<ArrayList<Ball>> kwadrat = new ArrayList<>();

    @FXML
    public Pane stackPane;
    int pickedX, pickedY;

    long clickCounter = 0;

    boolean foundPath=false;

    public void onMouseClicked(MouseEvent mouseEvent)
    {
        /*click((int) mouseEvent.getX(), (int) mouseEvent.getY());
        //System.out.println((int) mouseEvent.getX() + " " + (int) mouseEvent.getY());

        if(clickCounter == 1)
            moveBall((int)mouseEvent.getX(), (int)mouseEvent.getY());
        */

        createBalls();

        if(isFieldFull())
        {
            countPoints();
        }
    }

    public void square() {

        kwadrat.clear();
        int[] currentColor = new int[4];
        int index = 0;

        for (int i = 0; i < siatka.size() - 9; i += 9) {
            ArrayList<Ball> balls = new ArrayList<>();

            for (int j = i; j < i + 8; j++) {
                if (!siatka.get(j).isTaken)
                    continue;

                int j2 = j + 1;
                int j3 = j + 9;
                int j4 = j2 + 9;
                int ballX;
                int ballY;

                for (Ball ball : ballsList) {
                    ballX = (int)ball.getLayoutX();
                    ballY = (int)ball.getLayoutY();
                    if ( ( siatka.get(j).x == ballX && siatka.get(j).y == ballY )
                            || ( siatka.get(j2).x == ballX && siatka.get(j2).y == ballY )
                            || ( siatka.get(j3).x == ballX && siatka.get(j3).y == ballY )
                            || ( siatka.get(j4).x == ballX && siatka.get(j4).y == ballY ) )
                    {
                        currentColor[index] = ball.color;
                        balls.add(ball);
                        index++;
                    }
                }
                index = 0;

                if(currentColor[0] == currentColor[1] && currentColor[2] == currentColor[3] && currentColor[1] == currentColor[2])
                {
                    kwadrat.add(copyArrayList(balls));
                    balls.clear();
                }
                else
                {
                    balls.clear();
                }
            }
        }

        /*for(List<Ball> L : kwadrat)
        {
            for (Ball b : L)
            {
                System.out.print(b.color+" ");
            }
            if(!L.isEmpty()) {
                System.out.println("");
            }
        }*/
    }

    public void countPoints()
    {

        //poziomo
        int[] colors = new int[5];
        int currentColor=-1;
        int inRow=0;

        //poziomo.clear();
        System.out.println(" 0 BLUE 1 WHITE 2 RED 3 Green");

        for(int i = 0; i < siatka.size();i+=9)
        {
            ArrayList<Ball> balls = new ArrayList<>();

            for(int j = i; j < i + 9; j++)
            {
                if(!siatka.get(j).isTaken) // zle bo jak bede mial 5 to wykasuje
                {
                    if(inRow >= 4)
                    {
                        poziomo.add(copyArrayList(balls));
                    }
                    balls.clear();
                    inRow=0;
                    continue;
                }

                for(Ball ball: ballsList)
                {
                    if (siatka.get(j).x == ball.getLayoutX() && (siatka.get(j).y == ball.getLayoutY())) {

                        if(currentColor!= ball.color)
                        {
                            if(inRow>=4)
                            {
                                poziomo.add(copyArrayList(balls));
                            }
                            balls.clear();
                            currentColor=ball.color;
                            inRow=1;
                            //break;
                        }
                        else
                        {
                            balls.add(ball);
                            inRow++;
                            //if(balls.size() > 2)
                                //System.out.println("dupka");
                            //break;
                        }
                    }
                }

//                    for (int x=0;x<ballsList.size();x++) {
//                        if (siatka.get(i).x == ball.getLayoutX() && (siatka.get(i).x == ball.getLayoutY())) {
//
//
//                                colors[ball.color]++; //inkrementujemy ilosc kolorów
//                        }
//                    }
            }

//                for(int j = 0; j<colors.length;j++)
//                {
//                    if( colors[j]>=5)
//                    {
//                        for(int x =i;x<i+9;x++)
//                        {
//                            for (Ball ball : ballsList) {
//                                if (siatka.get(i).x == ball.getLayoutX() && (siatka.get(i).x == ball.getLayoutY())) {
//
//                                }
//                            }
//                        }
//                    }
//                }


            //System.out.println("Rzad "+i/9+" inRow "+inRow);
            for(List<Ball> L : poziomo)
            {

                //System.out.println("L size: " + L.size());
                //System.out.print("Color: ");
                for (Ball b : L)
                {
                    System.out.print(b.color+" ");
                }
                if(!L.isEmpty()) {
                    System.out.println("");
                }
            }

        }




        //poziomo koniec
    }




    private boolean isReachablePath(int startX, int startY, int endX, int endY)
    {
        int[] array = {-1, 1, -9, 9};

        int index=0;
        List checked = new ArrayList();
        NetNode point,point2;
        for(NetNode point3 : siatka)
            if(point3.x==startX && point3.y==startY)
                index = siatka.indexOf(point3);
        checked.add(index);
        for(int i : array) {
        //System.out.println(i);
            if (index + i > 0 && index + i < 81) {
                point = siatka.get(index);
                point2 = siatka.get(index + i);
                if ((i==-1 || i==1) && point2.y != point.y)
                    continue;
                recursivePathSearching(index + i, i, checked, endX, endY);
            }
        }
        System.out.println(checked.size());

        checked.clear();
        return false;
    }

    private boolean recursivePathSearching(int index,int direction, List checked, int endX, int endY)  // direction okresla w jakim kierunku znajduje sie poprzednia pozycja.
    {

        NetNode point = siatka.get(index);
        if(point==null)
            System.out.println("dupazbita");
        if(point.x == endX && point.y == endY)
            foundPath = true;

        if(point.isTaken)
            return false;


        for(int i=0;i<checked.size();i++) // czy juz tu bylismy
        {
            NetNode pt = siatka.get((int)checked.get(i));
            if(pt.x==point.x && pt.y == point.y)
                return false;
        }
        int[] array = {-1, 1, -9, 9};

        checked.add(index);
        List checked2 = new ArrayList(checked);

        NetNode point2;
        for( int i : array)
        {
            if(i==-direction)
                continue;
            if(index+i>0&&index+i<81) {
                point2 = siatka.get(index + i);
                if ((i==-1 || i==1) && (point2.y != point.y))
                    continue;
                recursivePathSearching(index + i, i, checked2, endX, endY);

            }
        }
        return false;
    }

    private void click(int x, int y)
    {
        if(clickCounter == 0)
        {
            if (!isEmptySpace(x, y, 15)) {
                pickedX = x;
                pickedY = y;
                clickCounter = 1;
            }
            else
            {
                clickCounter = 0;
                pickedX = -1;
                pickedY = -1;
            }
        }
        else
        {
            x = closestShot(x, y).x;
            y = closestShot(x, y).y;

            for(Ball b: ballsList)
            {
                if(b.getLayoutX() == x && b.getLayoutY() == y)
                {
                    pickedX = x;
                    pickedY = y;
                }
            }
        }
    }

    private boolean moveBall(int x, int y)
    {
        foundPath=false;
        x = closestShot(x, y).x;
        y = closestShot(x, y).y;
        //System.out.println("x: "+x+" y: "+y+" picX: "+pickedX+" picY: "+pickedY);
        if(isEmptySpace(x,y, 15) && pickedX != -1 && pickedY != -1)
        {
            NetNode point = closestShot(pickedX, pickedY);
            for(Ball b: ballsList)
            {
                if(b.getLayoutX() == point.x && b.getLayoutY() == point.y)
                {
                    isReachablePath(point.x,point.y,x,y);
                    if(foundPath) {

                        int index = siatka.indexOf(point);
                        point.isTaken=false;
                        siatka.set(index,point);
                        b.setLayoutX(x);
                        b.setLayoutY(y);

                        NetNode point2 = closestShot(x, y);
                        int index2=siatka.indexOf(point2);
                        System.out.println(siatka.get(index2).isTaken);
                        point2.isTaken=true;
                        siatka.set(index2,point2);
                        System.out.println(siatka.get(index2).isTaken);
                        clickCounter = 0;

                        System.out.println("Cool, you changed position.");
                    }
                    else
                        System.out.println("sorry, you cant get there.");
                    return true;
                }
            }
        }
        return false;
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
        while (j < 35 && !isFieldFull()) {
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

    private ArrayList copyArrayList(final ArrayList list) {

        ArrayList listCopy = new ArrayList(list);
        return listCopy;
    }
}
