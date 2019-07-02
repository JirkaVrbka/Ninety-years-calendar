package calendar;

import calendar.Graphics.CalendarGraphicsUtils;
import calendar.Graphics.CalendarStructure;
import calendar.Utils.JsonUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.File;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    public static final String pathToJson = "src/main/resources/data.json";
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    @FXML
    public Canvas canvas;
    @FXML
    public Text date;
    @FXML
    public Text textShownCalendar;

    private File jsonFile = new File(pathToJson);
    private int currentlyShownMonth;
    private int currentlyShownYear;


    public Controller() {
        if(!jsonFile.exists()) {
            JSONObject data = new JSONObject(JsonUtils.generateInitData());
            JsonUtils.exportJsonToFile(data, jsonFile);
        }
    }

    @FXML
    public void initialize(){
        JSONObject data = JsonUtils.importJsonFromFile(jsonFile);

        if(data == null){
            CalendarStructure.createCalendar(1994, 90);
        }else{
            CalendarStructure.createCalendar(data);
            CalendarStructure.saveToFileAsJson(jsonFile);
        }

        CalendarStructure.moveCalendar(10,10);
        HELPERgenerateMoods();
        CalendarStructure.drawCalendar(canvas);
    }

    private void HELPERgenerateMoods(){
        Random rand = new Random();

        for(int year = 1994; year < 2020; year++){
            for(int month = 1; month < 13; month++){
                for(int day = 1; day < 29; day++){
                    if(year >= 2019 && month >= 6 && day >= 20 ){
                        return;
                    }
                    MoodEnum moodEnum;
                    switch (rand.nextInt(8)){
                        case 1: moodEnum = MoodEnum.SAD;
                            break;
                        case 2: moodEnum = MoodEnum.AVERAGE;
                            break;
                        case 3: moodEnum = MoodEnum.HAPPY;
                            break;

                        default: moodEnum = MoodEnum.MISSED;
                            break;

                    }
                    CalendarStructure.setMood(moodEnum, year, month, day);
                }
            }
        }
    }

    private void setDateIntoTextField(int day, int month, int year){
        date.setText(String.format("%d/%d/%d", day, month, year));
    }

    private void setCurrentlyShownMonthYearTextField(int month, int year){
        textShownCalendar.setText(String.format("%d/%d", month, year));
    }

    private void drawMonthIntoCanvas(int month, int year){

        JSONObject data = JsonUtils.importJsonFromFile(jsonFile);

        String stringYear = String.valueOf(year);
        String stringMonth = String.valueOf(month);

        JSONObject currentJson;

        if(data == null || data.isNull(stringYear)){
            LOGGER.log(Level.INFO, String.format("No data for %d/%d. Generating empty data", month, year));
            LinkedHashMap<String, String> emptyJson = new LinkedHashMap<>();

            for(int i = 1; i < YearMonth.of(year, month).lengthOfMonth(); i++){
                emptyJson.put(String.valueOf(i), MoodEnum.NONE.toString());
            }
            currentJson = new JSONObject(emptyJson);
        } else {
            currentJson = data.getJSONObject(stringYear).getJSONObject(stringMonth);
        }

        CalendarGraphicsUtils.drawMonth(canvas, currentJson);
    }

    public void pickSad(ActionEvent actionEvent) {
        saveMood(MoodEnum.SAD);
    }

    public void pickAverage(ActionEvent actionEvent) {
        saveMood(MoodEnum.AVERAGE);
    }

    public void pickHappy(ActionEvent actionEvent) {
        saveMood(MoodEnum.HAPPY);
    }

    private void saveMood(MoodEnum mood){
        java.util.Calendar cal = java.util.Calendar.getInstance();

        int year = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH)+1;
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

        LOGGER.log(Level.INFO, String.format("Changing mood to [%s] at [%s/%s/%s]", mood.toString(), day, month, year).toString());

        CalendarStructure.setMood(mood, year, month, day);
        CalendarStructure.drawCalendar(canvas);
        CalendarStructure.saveToFileAsJson(jsonFile);
    }


    public void showPreviousMonth(ActionEvent actionEvent) {
        currentlyShownMonth--;

        if(currentlyShownMonth == 0){
            currentlyShownMonth = 12;
            currentlyShownYear--;
        }

        setCurrentlyShownMonthYearTextField(currentlyShownMonth, currentlyShownYear);
        drawMonthIntoCanvas(currentlyShownMonth, currentlyShownYear);
    }

    public void showNextMonth(ActionEvent actionEvent) {
        currentlyShownMonth++;

        if(currentlyShownMonth == 13){
            currentlyShownMonth = 1;
            currentlyShownYear++;
        }

        setCurrentlyShownMonthYearTextField(currentlyShownMonth, currentlyShownYear);
        drawMonthIntoCanvas(currentlyShownMonth, currentlyShownYear);
    }
}
