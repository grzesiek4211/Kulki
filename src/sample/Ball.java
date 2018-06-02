package sample;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.Random;


public class Ball extends Circle{

    int color;
    public Ball() {
      /*  Random rand= new Random();
        double x = rand.nextInt(800)+1;
        double y = rand.nextInt(800)+1;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setRadius(10);
        this.setFill(Color.WHITE);*/
    }

    public Ball(double x, double y, double r, int color2) {
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setRadius(r);
        this.color = color2;
        switch (color2)
        {
            case 0:
                this.setFill(Color.BLUE);
                break;
            case 1:
                this.setFill(Color.WHITE);
                break;
            case 2:
                this.setFill(Color.RED);
                break;
            case 3:
                this.setFill(Color.GREEN);
                break;
            case 4:
                this.setFill(Color.PURPLE);
                break;
            case 5:
                this.setFill(Color.GRAY);
                break;
            case 6:
                this.setFill(Color.YELLOW);
                break;
            case 7:
                this.setFill(Color.GOLDENROD);
                break;
            case 8:
                this.setFill(Color.BROWN);
                break;
        }

        /*this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //System.out.println(getLayoutX() + "  " + getLayoutY());

                setLayoutX(me.getX());
                setLayoutY(me.getX());
            }
        });*/
    }


    public void getLight()
    {
        Light.Distant light = new Light.Distant() ;
        light.setAzimuth(90.0) ;
        light.setElevation(10.0) ;
        Lighting lighting = new Lighting() ;
        this.setEffect(lighting);
    }
//
//    public void moveBall(double x, double y)
//    {
//
//     /*   this.setLayoutX(this.getLayoutX() + x);
//        this.setLayoutY(this.getLayoutY() + y);*/
//
//    }
}