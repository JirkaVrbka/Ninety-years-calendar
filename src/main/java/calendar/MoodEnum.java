package calendar;

import javafx.scene.paint.Color;

public enum MoodEnum {
    SAD(Color.RED),
    AVERAGE(Color.YELLOW),
    HAPPY(Color.GREEN),
    MISSED(Color.GRAY.brighter()),
    NONE(Color.WHITE);

    private Color color;

    MoodEnum(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public static Color getColor(String mood){
        switch (mood){
            case "SAD": return SAD.getColor();
            case "AVERAGE": return AVERAGE.getColor();
            case "HAPPY": return HAPPY.getColor();
            case "NONE": return NONE.getColor();
            default: return Color.BLACK;
        }
    }
}
