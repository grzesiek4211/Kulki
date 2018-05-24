package sample;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    private int rowHeight;
    private int rowWidth;

    int windowWidth=900;
    int windowHeight=900;

    public List<Ball> ballsList;
    @FXML
    public Pane stackPane;


   public void onMouseClicked(MouseEvent mouseEvent) {
        //this.stackPane.getChildren().remove(Ball.).getLayoutX()
        //createArea();
    }

    public void createArea()
    {

        rowHeight = windowHeight/12;
        rowWidth = windowWidth/11;

        //System.out.println("row width "+rowHeight+ "  row he  "+rowHeight);

    }

    public void createBalls()
    {
        for(int i =rowHeight; i <windowHeight;i+=rowHeight)
        {
            for(int j = rowWidth;j < windowWidth;j+=rowWidth)
            {
                Ball b = new Ball(j,i,25, Color.WHITE);
                ballsList.add(b);
                //System.out.println("dupa");
            }
        }
    }


    public void initialize()
    {


        this.ballsList = new LinkedList();
        createArea();

        createBalls();
        stackPane.getChildren().addAll(ballsList);
       // System.out.println(stackPane.getHeight()+"    "+ stackPane.getWidth());
    }
}
