package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
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

    int clickCounter = 0;
    int points = 0;

    boolean foundPath=false;
    boolean cantBeReached = false;
    int checkedNodesCounter =0;
    int color = 5;
    String nick = "computer";
    ArrayList<Level> ranking5 = new ArrayList<Level>();
    ArrayList<Level> ranking7 = new ArrayList<Level>();
    ArrayList<Level> ranking9 = new ArrayList<Level>();
    Ranking wynik = new Ranking();

    int flagEnd = 0;

    public void onMouseClicked(MouseEvent mouseEvent)
    {
        if(flagEnd == 0) {
            if (!isFieldFull()) {

                clickAndMoveBall((int) mouseEvent.getX(), (int) mouseEvent.getY());

                if (clickCounter == 0) {
                    countPointsVertictal();
                    countPointsHorizontal();
                    countSquare();
                    countPointsDiagonalLeft();
                    countPointsDiagonalRight();


                    if (!deletingBalls()) {
                        createBalls(3);
                    }
                    countPointsVertictal();
                    countPointsHorizontal();
                    countSquare();
                    countPointsDiagonalLeft();
                    countPointsDiagonalRight();
                    deletingBalls();
                }
            } else {
                flagEnd = 1;
                Alert end = new Alert(Alert.AlertType.INFORMATION);
                end.setTitle("End game!");
                end.setHeaderText("End game!");
                end.setContentText(nick + " your result is: " + points);
                end.showAndWait();

                ranking5 = wynik.getRanking5();
                ranking7 = wynik.getRanking7();
                ranking9 = wynik.getRanking9();

                Level level = new Level(nick, points);
                if (color == 5)
                    ranking5.add(level);
                if (color == 7)
                    ranking7.add(level);
                if (color == 9)
                    ranking9.add(level);

                Collections.sort(ranking5);
                Collections.sort(ranking7);
                Collections.sort(ranking9);

                wynik.setRanking5(ranking5);
                wynik.setRanking7(ranking7);
                wynik.setRanking7(ranking9);

                try {
                    Serialize st = new Serialize(wynik);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            Alert end = new Alert(Alert.AlertType.INFORMATION);
            end.setTitle("End of game!");
            end.setHeaderText("End of game!");
            end.setContentText("Press New game!");
            end.showAndWait();
        }
    }

    private boolean deletingBalls()
    {
        boolean flag = false;
        if( removeBalls(poziomo) ) flag = true;
        if( removeBalls(dol) ) flag = true;
        if( removeBalls(skoslewy) ) flag = true;
        if( removeBalls(skosprawy) ) flag = true;
        if( removeBalls(kwadrat) ) flag = true;

        return flag;
    }

    private boolean removeBalls(ArrayList<ArrayList<Ball>> ballsToRemove)
    {
        boolean flag = false;
        for(ArrayList<Ball> L : ballsToRemove)
        {
            switch(L.size())
            {
                case 6:
                    points += 18;
                    break;
                case 7:
                    points += 28;
                    break;
                default: points += L.size();
            }
            for (Ball b : L)
            {
                for(NetNode node : siatka)
                {
                    if(b.getLayoutX()==node.x && b.getLayoutY()==node.y)
                    {
                        int index = siatka.indexOf(node);
                        node.isTaken=false;
                        siatka.set(index,node);
                        stackPane.getChildren().remove(b);
                        if (ballsList.remove(b)) flag = true;
                    }
                }
            }
        }

        ballsToRemove.clear();

        return flag;
    }

    public void countSquare() {

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
                Ball b1 = null;
                Ball b2 = null;
                Ball b3 = null;
                Ball b4 = null;

                for (Ball ball : ballsList) {
                    ballX = (int) ball.getLayoutX();
                    ballY = (int) ball.getLayoutY();

                    if (siatka.get(j).x == ballX && siatka.get(j).y == ballY)
                        b1 = ball;
                }
                for (Ball ball : ballsList) {
                    ballX = (int) ball.getLayoutX();
                    ballY = (int) ball.getLayoutY();

                    if (siatka.get(j2).x == ballX && siatka.get(j2).y == ballY)
                        b2 = ball;
                }
                for (Ball ball : ballsList) {
                    ballX = (int) ball.getLayoutX();
                    ballY = (int) ball.getLayoutY();

                    if (siatka.get(j3).x == ballX && siatka.get(j3).y == ballY)
                        b3 = ball;
                }
                for (Ball ball : ballsList) {
                    ballX = (int) ball.getLayoutX();
                    ballY = (int) ball.getLayoutY();

                    if (siatka.get(j4).x == ballX && siatka.get(j4).y == ballY)
                        b4 = ball;
                }
                if(b1!=null && b2 != null && b3 != null && b4 != null) {
                    if((b1.color==b2.color) && (b3.color==b4.color) && b1.color==b3.color) {
                        balls.add(b1);
                        balls.add(b2);
                        balls.add(b3);
                        balls.add(b4);
                    }
                }

                kwadrat.add(copyArrayList(balls));
                balls.clear();
            }
        }
    }

    public void countPointsHorizontal() {

        int[] colors = new int[5];
        int currentColor = -1;
        int inRow = 0;
        int howManyInRow = 5;

        poziomo.clear();
        for (int i = 0; i < siatka.size(); i += 9) {
            ArrayList<Ball> balls = new ArrayList<>();

            for (int j = i; j < i + 9; j++) {

                if (!siatka.get(j).isTaken)
                {
                    if (inRow >= howManyInRow) {
                        ArrayList<Ball> arrayList = copyArrayList(balls);
                        poziomo.add(arrayList);
                    }
                    balls.clear();
                    inRow = 0;
                    currentColor=-1;
                    continue;
                }

                for (Ball ball : ballsList) {
                    if (siatka.get(j).x == ball.getLayoutX() && (siatka.get(j).y == ball.getLayoutY())) {

                        if (currentColor != ball.color) {
                            if (inRow >= howManyInRow) {
                                poziomo.add(copyArrayList(balls));
                            }
                            balls.clear();
                            balls.add(ball);
                            currentColor = ball.color;
                            inRow = 1;
                        } else {
                            balls.add(ball);
                            inRow++;
                            if (j % 9 == 8) {
                                if (inRow >= howManyInRow) {
                                    poziomo.add(copyArrayList(balls));
                                    balls.clear();
                                }
                            }

                        }
                    }
                }
            }

            inRow = 0;
            currentColor = -1;
        }
    }

    public void countPointsVertictal() {

        int howManyInRow = 5;
        int[] colors = new int[5];
        int currentColor = -1;
        int inRow = 0;

        dol.clear();
        for (int i = 0; i < 9; i++) {
            ArrayList<Ball> balls = new ArrayList<>();

            for (int j = i; j <= i + 72; j += 9) {

                if (!siatka.get(j).isTaken)
                {
                    if (inRow >= howManyInRow) {
                        ArrayList<Ball> arrayList = copyArrayList(balls);
                        dol.add(arrayList);
                    }
                    balls.clear();
                    inRow = 0;
                    currentColor=-1;
                    continue;
                }

                for (Ball ball : ballsList) {
                    if (siatka.get(j).x == ball.getLayoutX() && (siatka.get(j).y == ball.getLayoutY())) {

                        if (currentColor != ball.color) {
                            if (inRow >= howManyInRow) {
                                dol.add(copyArrayList(balls));
                            }
                            balls.clear();
                            balls.add(ball);
                            currentColor = ball.color;
                            inRow = 1;
                        } else {
                            balls.add(ball);
                            inRow++;
                            if (j > 71) {
                                if (inRow >= howManyInRow) {
                                    dol.add(copyArrayList(balls));
                                    balls.clear();
                                }
                            }

                        }
                    }
                }
            }

            inRow = 0;
            currentColor = -1;
        }
    }

    public void countPointsDiagonalRight() {

        skosprawy.clear();
        int howManyInRow = 5;
        int inRow = 0;
        int currentColor = -1;
        boolean flag = true;
        int i = 36;
        while(flag)
        {
            ArrayList<Ball> balls = new ArrayList<>();
            for (int j = i; j < 81; j+=10) {

                if (!siatka.get(j).isTaken)
                {
                    if (inRow >= howManyInRow) {
                        skosprawy.add(copyArrayList(balls));
                    }
                    balls.clear();
                    inRow = 0;
                    currentColor=-1;
                    continue;
                }

                for (Ball ball : ballsList) {
                    if (siatka.get(j).x == ball.getLayoutX() && (siatka.get(j).y == ball.getLayoutY()) ) {

                        if (currentColor != ball.color) {
                            if (inRow >= howManyInRow) {
                                skosprawy.add(copyArrayList(balls));
                            }
                            balls.clear();
                            balls.add(ball);
                            currentColor = ball.color;
                            inRow = 1;
                        } else {
                            balls.add(ball);
                            inRow++;
                            if (j % 9 == 8 || j > 71) {
                                if (inRow >= howManyInRow)
                                {
                                    skosprawy.add(copyArrayList(balls));
                                }
                                    balls.clear();
                                    inRow = 0;
                                    currentColor = -1;
                            }
                        }
                    }
                }
            }

            inRow = 0;
            currentColor = -1;

            if(i >= 9)
                i -= 9;
            else
                i++;
            if(i == 5) flag = false;

        }
    }

    public void countPointsDiagonalLeft() {

        skoslewy.clear();
        int howManyInRow = 5;
        int inRow = 0;
        int currentColor = -1;
        boolean flag = true;
        int i = 44;
        while(flag)
        {
            ArrayList<Ball> balls = new ArrayList<>();
            for (int j = i; j < 81; j+=8) {
                if( j-8 > 0 && siatka.get(j).x > siatka.get(j-8).x && j < i/8*8) {
                    if (inRow >= howManyInRow) {
                        skoslewy.add(copyArrayList(balls));
                    }
                    balls.clear();
                    inRow = 0;
                    currentColor = -1;
                    break;
                }

                if (!siatka.get(j).isTaken) // zle bo jak bede mial 5 to wykasuje
                {
                    if (inRow >= howManyInRow) {
                        skoslewy.add(copyArrayList(balls));
                    }
                    balls.clear();
                    inRow = 0;
                    currentColor=-1;
                    continue;
                }

                for (Ball ball : ballsList) {
                    if (siatka.get(j).x == ball.getLayoutX() && (siatka.get(j).y == ball.getLayoutY()) ) {

                        if (currentColor != ball.color) {
                            if (inRow >= howManyInRow) {
                                skoslewy.add(copyArrayList(balls));
                            }
                            balls.clear();
                            balls.add(ball);
                            currentColor = ball.color;
                            inRow = 1;
                        } else {
                            balls.add(ball);
                            inRow++;
                            if (j % 9 == 0 || j > 71) {
                                if (inRow >= howManyInRow)
                                {
                                    skoslewy.add(copyArrayList(balls));
                                }
                                balls.clear();
                                inRow = 0;
                                currentColor = -1;
                            }
                        }
                    }
                }
            }

            inRow = 0;
            currentColor = -1;

            if(i > 9)
                i -= 9;
            else
                i--;
            if(i == 3) flag = false;

        }
    }

    private boolean isReachablePath(int startX, int startY, int endX, int endY)
    {
        int[] array=new int[4];
        checkedNodesCounter=0;
        int wektorX=endX-startX;
        int wektorY=endY-startY;

        if(wektorX>0)
        {
            if(wektorY>0)
            {
                //array = {1, 9, -1, -9};
                array[0]=1;
                array[1] = 9;
                array[2] = -1;
                array[3] = -9;
            }
            else{
                array[0]=1;
                array[1] = -9;
                array[2] = -1;
                array[3] = 9;
            }

        }
        else
        {
            if(wektorY>0)
            {
                array[0] = -1;
                array[1] = 9;
                array[2] = 1;
                array[3] = -9;
            }
            else
            {
                array[0] = -1;
                array[1] = -9;
                array[2] = 1;
                array[3] = 9;
            }
        }


        int index=0;
        boolean flag = false;
        List checked = new ArrayList();
        NetNode point,point2;
        for(NetNode point3 : siatka)
            if(point3.x==startX && point3.y==startY)
                index = siatka.indexOf(point3);
        checked.add(index);
        for(int i : array) {
            if (index + i >= 0 && index + i < 81) {
                point = siatka.get(index);
                point2 = siatka.get(index + i);
                if ((i==-1 || i==1) && point2.y != point.y)
                    continue;
                if(recursivePathSearching(index + i, i, checked, endX, endY))
                    flag = true;
            }
        }

        checked.clear();
        return flag;
    }

    private boolean recursivePathSearching(int index,int direction, List checked, int endX, int endY)  // direction okresla w jakim kierunku znajduje sie poprzednia pozycja.
    {
        if(checkedNodesCounter>45)
            return false;
        if(cantBeReached)
            return false;
        NetNode point = siatka.get(index);
        if(foundPath || (point.x == endX && point.y == endY)) {
            foundPath = true;
            return true;
        }

        if(point.isTaken)
            return false;


        for(int i=0;i<checked.size();i++) // czy juz tu bylismy
        {
            NetNode pt = siatka.get((int)checked.get(i));
            if(pt.x==point.x && pt.y == point.y)
                return false;
        }
        int[] array=new int[4];
        int wektorX=endX-point.x;
        int wektorY=endY-point.y;

        if(wektorX>0)
        {
            if(wektorY>0)
            {
                array[0]=1;
                array[1] = 9;
                array[2] = -1;
                array[3] = -9;

            }
            else{
                array[0]=1;
                array[1] = -9;
                array[2] = -1;
                array[3] = 9;
            }

        }
        else
        {
            if(wektorY>0)
            {
                array[0] = -1;
                array[1] = 9;
                array[2] = 1;
                array[3] = -9;
            }
            else
            {
                array[0] = -1;
                array[1] = -9;
                array[2] = 1;
                array[3] = 9;
            }
        }

        checked.add(index);
        List checked2 = new ArrayList(checked);
        checkedNodesCounter++;
        NetNode point2;
        for( int i : array)
        {
            if(i==-direction)
                continue;
            if(index+i>=0&&index+i<81) {
                point2 = siatka.get(index + i);
                if ((i==-1 || i==1) && (point2.y != point.y))
                    continue;
                if(recursivePathSearching(index + i, i, checked2, endX, endY))
                    return true;
            }
        }
        return false;
    }

    private boolean clickAndMoveBall(int x, int y)
    {
        click(x, y);
        if (clickCounter == 1) {
            return moveBall(x, y);
        }
        return false;
    }

    private void click(int x, int y)
    {
        if(clickCounter == 0 || clickCounter == 2)
        {
            if (!isEmptySpace(x, y, 15)) {
                pickedX = x;
                pickedY = y;
                clickCounter = 1;
            }
            else
            {
                clickCounter = 2;
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

        if(isEmptySpace(x,y, 15) && pickedX != -1 && pickedY != -1)
        {
            NetNode point = closestShot(pickedX, pickedY);

            for(Ball b: ballsList)
            {
                if(b.getLayoutX() == point.x && b.getLayoutY() == point.y)
                {
                    boolean flag = isReachablePath(x,y,point.x,point.y);
                    if(!flag)
                        isReachablePath(point.x,point.y,x,y);
                    if(foundPath || flag) {

                        int index = siatka.indexOf(point);
                        point.isTaken=false;
                        siatka.set(index,point);
                        b.setLayoutX(x);
                        b.setLayoutY(y);

                        NetNode point2 = closestShot(x, y);
                        int index2=siatka.indexOf(point2);
                        point2.isTaken=true;
                        siatka.set(index2,point2);
                        clickCounter = 0;
                        pickedX=-1;
                        pickedY=-1;
                        System.out.println("Cool, you changed position.");
                        return true;
                    }
                    else {
                        System.out.println("Sorry, you cant get there.");
                        pickedX = -1;
                        pickedY = -1;
                        return false;
                    }

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
                Circle tlo = new Circle(node.x,node.y,3, Color.GRAY);
                stackPane.getChildren().add(tlo);

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
        int i = rand.nextInt(color) ;
        return i;
    }

    private void createBalls(int howMany)
    {
        int j = 0;
        while (j < howMany && !isFieldFull()) {
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

    private void createBallOnClick(int x, int y) // funkcja do testów
    {
            if(!isFieldFull()) {
                int radius = 15;
                NetNode point = closestShot(x,y);
                if (point != null) {
                    if (isEmptySpace(point.x, point.y, radius)) {

                        Ball b = new Ball(point.x, point.y, radius, 0);//drawColor());
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

    public void initialize() {

        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Settings");
        textInputDialog.setHeaderText("");
        textInputDialog.setContentText("Enter your name");
        Optional<String> result = textInputDialog.showAndWait();

        if (result.isPresent()) {
            nick = result.get();
        }

        try {
            wynik = Deserialize.odczyt();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        view();
    }

    private void view()
    {
        MenuBar menuBar = new MenuBar();
        Menu menuNewGame = new Menu("New game");
        MenuItem menuNewGame2 = new MenuItem("New game");
        menuNewGame.getItems().addAll(menuNewGame2);
        Menu menuLevel = new Menu("Difficulty level");
        MenuItem ball5 = new MenuItem("5 balls");
        MenuItem ball7 = new MenuItem("7 balls");
        MenuItem ball9 = new MenuItem("9 balls");
        menuLevel.getItems().addAll(ball5, ball7, ball9);
        Menu menuStatistics = new Menu("Ranking");
        MenuItem sBall5 = new MenuItem("5 balls");
        MenuItem sBall7 = new MenuItem("7 balls");
        MenuItem sBall9 = new MenuItem("9 balls");
        menuStatistics.getItems().addAll(sBall5, sBall7, sBall9);
        menuBar.getMenus().addAll(menuNewGame, menuLevel, menuStatistics);

        Alert ranking = new Alert(Alert.AlertType.INFORMATION);
        ranking.setTitle("Ranking");
        sBall5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int count = 1;
                String result = "";
                for(Level r: wynik.getRanking5())
                {
                    if(count > 10) break;
                    result += count + ". " + r + "\n";
                    count++;
                }
                ranking.setHeaderText("5 colors");
                ranking.setContentText(result);
                ranking.showAndWait();
            }
        });
        sBall7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int count = 1;
                String result = "";
                for(Level r: wynik.getRanking7())
                {
                    if(count > 10) break;
                    result += count + ". " + r + "\n";
                    count++;
                }
                ranking.setHeaderText("7 colors");
                ranking.setContentText(result);
                ranking.showAndWait();
            }
        });
        sBall9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int count = 1;
                String result = "";
                for(Level r: wynik.getRanking9())
                {
                    if(count > 10) break;
                    result += count + ". " + r + "\n";
                    count++;
                }
                ranking.setHeaderText("9 colors");
                ranking.setContentText(result);
                ranking.showAndWait();
            }
        });
        menuNewGame2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage primaryStage = (Stage) stackPane.getScene().getWindow();
                primaryStage.close();
                Platform.runLater( () -> {
                    try {
                        new Main().start( new Stage() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        ball5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stackPane.getChildren().clear();
                view();

                poziomo = new ArrayList<>();
                skosprawy = new ArrayList<>();
                skoslewy = new ArrayList<>();
                dol = new ArrayList<>();
                kwadrat = new ArrayList<>();
                clickCounter = 0;
                points = 0;
                foundPath=false;
                cantBeReached = false;
                checkedNodesCounter = 0;
                flagEnd = 0;
                pickedX = 0;
                pickedY = 0;


                color = 5;
            }
        });

        ball7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stackPane.getChildren().clear();
                view();

                poziomo = new ArrayList<>();
                skosprawy = new ArrayList<>();
                skoslewy = new ArrayList<>();
                dol = new ArrayList<>();
                kwadrat = new ArrayList<>();
                clickCounter = 0;
                points = 0;
                foundPath=false;
                cantBeReached = false;
                checkedNodesCounter = 0;
                flagEnd = 0;
                pickedX = 0;
                pickedY = 0;


                color = 7;
            }
        });

        ball9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stackPane.getChildren().clear();
                view();

                poziomo = new ArrayList<>();
                skosprawy = new ArrayList<>();
                skoslewy = new ArrayList<>();
                dol = new ArrayList<>();
                kwadrat = new ArrayList<>();
                clickCounter = 0;
                points = 0;
                foundPath=false;
                cantBeReached = false;
                checkedNodesCounter = 0;
                flagEnd = 0;
                pickedX = 0;
                pickedY = 0;


                color = 9;
            }
        });

        stackPane.getChildren().add(menuBar);

        this.ballsList = new ArrayList<>();
        this.siatka = new ArrayList<>();
        createArea();
        createBalls(5);
    }

    private ArrayList copyArrayList( ArrayList list) {

        ArrayList listCopy = new ArrayList(list);
        return listCopy;
    }
}
