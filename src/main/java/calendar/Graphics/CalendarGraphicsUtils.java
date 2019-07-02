package calendar.Graphics;

import calendar.Controller;
import calendar.MoodEnum;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class helps to draw calendar into canvas
 */
public class CalendarGraphicsUtils {
    public static final int topSpace = 10;
    public static final int leftSpace = 10;
    public static final int rectangleSide = 5;
    public static final int lineWidth = 1;


    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    public static void colorCanvas(Canvas canvas, MoodEnum mood){


        canvas.getGraphicsContext2D().setFill(mood.getColor());
        canvas.getGraphicsContext2D().fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    public static void drawMonth(Canvas canvas, JSONObject json){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GRAY);
        gc.fillRect(leftSpace,topSpace,7*rectangleSide + (8*lineWidth),(5*rectangleSide) + (6*lineWidth));
        drawMonthLines(canvas);
        drawMonthFillMoods(canvas,json);
    }


    private static void drawMonthFillMoods(Canvas canvas,  JSONObject json) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < json.length(); i++) {
            gc.setFill(MoodEnum.getColor(json.getString(String.valueOf(i+1))));
            gc.fillRect(leftSpace + lineWidth + lineWidth*(i%7) + rectangleSide*(i%7), topSpace + lineWidth + lineWidth*(i/7) + rectangleSide*(i/7), rectangleSide, rectangleSide);
        }
    }

    private static void drawMonthLines(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int rightLimit = (7*rectangleSide) + (8*lineWidth);
        int bottomLimit =  (5*rectangleSide) + (6*lineWidth);
/*
        //draw lines
        gc.setFill(Color.BLACK);

        for(int i = 0; i < 6; i++){
            int y = leftSpace + lineWidth*i + rectangleSide*i;

             gc.fillRect(leftSpace , y, rightLimit , lineWidth);

        }

        //draw columns
       for(int i = 0; i < 8; i++){
            int x = leftSpace + lineWidth*i + rectangleSide*i;

            gc.fillRect(x, topSpace,  lineWidth, bottomLimit );

        }
        *//*
        try{
            CalendarStructure cc = new CalendarStructure((int)gc.getCanvas().getHeight(), (int)gc.getCanvas().getWidth());

            for (CalendarStructure.Rectangle rec : cc.getLines(7)) {
                gc.fillRect(rec.xCoordinate, rec.yCoordinate, rec.width, rec.height);
            }
        }catch (Exception ex){
            LOGGER.log(Level.SEVERE, ex.toString());
        }*/
    }

    public static void drawBackground(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GRAY);

        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
    }


}
