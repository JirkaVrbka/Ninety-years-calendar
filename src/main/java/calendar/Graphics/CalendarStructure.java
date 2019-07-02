package calendar.Graphics;

import calendar.MoodEnum;
import calendar.Utils.JsonUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.time.YearMonth;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class CalendarStructure {

    private final static Logger LOGGER = Logger.getLogger(JsonUtils.class.getName());

    private  static int gapsBetweenYears = 3;
    private  static int gapsBetweenMonths = 2;
    private  static int gapsBetweenDays = 1;

    private  static int yearsInLine = 3;
    private  static int monthsInLine = 12;
    private  static int daysInLine = 7;

    private  static int daySide = 3;

    private static class Calendar {
        private static int gapsBetweenYears = CalendarStructure.gapsBetweenYears;
        private static int yearsInLine = CalendarStructure.yearsInLine;
        private Map<Integer, Year> years = new HashMap<>();

        private Calendar(int startYear, int length){
            for(int i = 0; i < length; i++){
                int x = (Year.getWidth() + gapsBetweenYears) * (i % yearsInLine);
                int y = (Year.getHeight() + gapsBetweenYears) * (i / yearsInLine);
                years.put(startYear + i, new Year(startYear + i).move(x, y));
            }
        }

        /**
         * Move every coordinate by x and y
         * @param x move in X axis
         * @param y move in Y axis
         * @return this instance with moved coordinates
         */
        private Calendar move(int x, int y){
            years.forEach((k, v) -> v.move(x, y));
            return this;
        }

        private Year getYear(int year){
            return years.get(year);
        }

        private Map<Integer, Year> getYears(){
            return years;
        }
    }

    private static class Year {
        private static int gapsBetweenMonths = CalendarStructure.gapsBetweenMonths;
        private static int monthsInLine = CalendarStructure.monthsInLine;
        private Map<Integer, Month> months = new HashMap<>();

        private Year(int year){
            for(int i = 0; i < 12; i++){
                int x = (Month.getWidth() + gapsBetweenMonths) * (i % monthsInLine);
                int y = (Month.getHeight() + gapsBetweenMonths) * (i / monthsInLine);
                months.put(i+1, new Month(year, i).move(x, y));
            }
        }

        /**
         * Move every coordinate by x and y
         * @param x move in X axis
         * @param y move in Y axis
         * @return this instance with moved coordinates
         */
        private Year move(int x, int y){
            months.forEach((k, v) -> v.move(x, y));
            return this;
        }

        private static int getWidth(){
            return monthsInLine*(gapsBetweenMonths + Month.getWidth());
        }

        private static int getHeight(){
            return (12 / monthsInLine)*(gapsBetweenMonths + Month.getHeight());
        }

        private Month getMonth(int month){
            return months.get(month);
        }

        private Map<Integer, Month> getMonths(){
            return months;
        }

    }

    private static class Month {
        private static int gapsBetweenDays = CalendarStructure.gapsBetweenDays;
        private static int daysInLine = CalendarStructure.daysInLine;
        private Map<Integer, Day> days = new HashMap<>();
        private static int daysInMonth;

        private Month(int year, int month){
            daysInMonth = YearMonth.of(year, month+1).lengthOfMonth();

            for(int i = 0; i < daysInMonth; i++){
                int x = (daySide + gapsBetweenDays) * (i % daysInLine);
                int y = (daySide + gapsBetweenDays) * (i / daysInLine);
                days.put(i+1, new Day(x, y));
            }
        }

        /**
         * Move every coordinate by x and y
         * @param x move in X axis
         * @param y move in Y axis
         * @return this instance with moved coordinates
         */
        private Month move(int x, int y){
            days.forEach((key, val) -> val.move(x, y));
            return this;
        }

        private static int getWidth(){
            return (daySide + gapsBetweenDays) * daysInLine;
        }

        private static int getHeight(){
            int maxDaysInMonth = 31;
            return (daySide + gapsBetweenDays) * ((maxDaysInMonth / daysInLine) + (maxDaysInMonth % daysInLine == 0 ? 0 : 1));
        }

        private Day getDay(int day){
            return days.get(day);
        }

        private Map<Integer, Day> getDays(){
            return days;
        }

    }

    private static class Day {
        private static int side = daySide;
        private int x;
        private int y;
        private MoodEnum mood = MoodEnum.NONE;


        private Day(int x, int y){
            this.x = x;
            this.y = y;
        }

        /**
         * Move every coordinate by x and y
         * @param x move in X axis
         * @param y move in Y axis
         */
        private void move(int x, int y){
            this.x += x;
            this.y += y;
        }

        private static int getWidth(){
            return side;
        }

        private static int getHeight(){
            return side;
        }

        private int getX() {
            return x;
        }

        private int getY() {
            return y;
        }

        public MoodEnum getMood() {
            return mood;
        }

        private void setMood(MoodEnum mood) {
            this.mood = mood;
        }
    }

    private static Calendar calendar;

    private CalendarStructure(){}

    public static void createCalendar(int from, int length){
        LOGGER.log(Level.INFO , String.format("Creating new calendar from %d by lenght %d", from, length));
        calendar = new Calendar(from, length);
    }

    public static void createCalendar(JSONObject jsonObject){
        LOGGER.log(Level.INFO , "Creating new calendar from JsonObject");
        Set<Integer> keySet = jsonObject.keySet().stream().map(Integer::valueOf).collect(Collectors.toSet());
        createCalendar(Collections.min(keySet), keySet.size());

        setMoods(jsonObject);
    }

    public static void setMoods(JSONObject jsonObject){
        LOGGER.log(Level.INFO , "Coloring calendar from JsonObject");
        calendar.getYears().
                forEach((keyYear, year) ->
                        year.getMonths().forEach((keyMonth, month) ->
                                month.getDays().forEach((keyDay, day) -> {
                                    day.setMood(getMoodFromJson(jsonObject, keyYear, keyMonth, keyDay));
                                })
                        )
                );
    }

    private static MoodEnum getMoodFromJson(JSONObject jsonObject, Integer year, Integer month, Integer day){
        try {
            String moodString = jsonObject
                    .getJSONObject(year.toString())
                    .getJSONObject(month.toString())
                    .getString(day.toString());
            return MoodEnum.valueOf(moodString);
        } catch (JSONException e){
            LOGGER.log(Level.INFO, e.toString());
            return MoodEnum.NONE;
        }

    }

    public static void drawCalendar(Canvas canvas) {
        LOGGER.log(Level.INFO , "Drawing calendar");
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GRAY);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());

        calendar.getYears().
                forEach((keyYear, year) ->
                        year.getMonths().forEach((keyMonth, month) ->
                                month.getDays().forEach((keyDay, day) -> {
                                    gc.setFill(day.getMood().getColor());
                                    gc.fillRect(day.getX(), day.getY(), CalendarStructure.Day.getHeight(), CalendarStructure.Day.getHeight());
                                })
                        )
                );
    }

    public static void moveCalendar(int x, int y) {
        LOGGER.log(Level.INFO , String.format("Moving calendar coordinates by (%d,%d)", x, y));
        calendar.move(x, y);
    }

    public static JSONObject toJsonObject(){
        LOGGER.log(Level.INFO , "Creating JsonObject from calendar");

        LinkedHashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, String>>> map = new LinkedHashMap<>();

        calendar.getYears().
                forEach((keyYear, year) ->
                        {
                            map.put(keyYear, new LinkedHashMap<>());
                            year.getMonths().forEach((keyMonth, month) -> {
                                        map.get(keyYear).put(keyMonth, new LinkedHashMap<>());
                                        month.getDays().forEach((keyDay, day) -> {
                                            map.get(keyYear).get(keyMonth).put(keyDay, day.getMood().toString());
                                        });
                                    }
                            );
                        }
                );

        return new JSONObject(map);
    }

    public static void setMood(MoodEnum mood, int year, int month, int day){
        calendar.getYear(year).getMonth(month).getDay(day).setMood(mood);
    }

    public static void setMood(MoodEnum mood, Date date){
        calendar.getYear(date.getYear()).getMonth(date.getMonth()).getDay(date.getDay()).setMood(mood);
    }

    public static void setMoodForToday(MoodEnum mood){
        Date date = java.util.Calendar.getInstance().getTime();
        calendar.getYear(date.getYear()).getMonth(date.getMonth()).getDay(date.getDay()).setMood(mood);
    }

    public static void saveToFileAsJson(File file){
        JsonUtils.exportJsonToFile(toJsonObject(), file);
    }
}
