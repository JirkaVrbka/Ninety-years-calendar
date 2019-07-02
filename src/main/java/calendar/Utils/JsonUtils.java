package calendar.Utils;

import calendar.MoodEnum;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class help to load and unload Json data from file
 */
public class JsonUtils {

    private final static Logger LOGGER = Logger.getLogger(JsonUtils.class.getName());

    public static Map<Integer, Map<Integer, Map<Integer, String>>> generateInitData(){
        LOGGER.log(Level.INFO , "Generating initial data filled with NONE");

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Map<Integer, Map<Integer,Map<Integer,String>>> years = new LinkedHashMap<>();
        Map<Integer, Map<Integer,String>> months = new LinkedHashMap<>();
        Map<Integer, String> days = new LinkedHashMap<>();

        for(int i = 1; i < 29; i++){
            days.put(i, MoodEnum.NONE.toString());
        }

        for(int i = 1; i < 13; i++){
            months.put(i, new LinkedHashMap<>(days));
        }

        for(int year = currentYear; year < currentYear + 10; year++){
            years.put(year, months);
            for(int month = 1; month < 13; month++){
                for(int day = 29; day < YearMonth.of(year, month).lengthOfMonth(); day++){
                    years
                            .get(year)
                            .get(month)
                            .put(day, MoodEnum.NONE.toString());
                }
            }
        }

        return years;
    }

    public static void exportJsonToFile(JSONObject jsonObject, File jsonFile){
        LOGGER.log(Level.INFO , "Exporting JSON into file [" + jsonFile.getAbsolutePath() + "]");

        if (!jsonFile.exists()) {
            try {
                if (!jsonFile.createNewFile()) {
                    LOGGER.log(Level.SEVERE, "Unable to create file at [" + jsonFile.getAbsolutePath() + "]");
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString());
                return;
            }
        }

        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(jsonObject.toString());
            file.flush();
        }catch (IOException e){
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    public static JSONObject importJsonFromFile(File jsonFile) {
        LOGGER.log(Level.INFO , "Importing JSON from file [" + jsonFile.getAbsolutePath() + "]");

        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(jsonFile.getAbsolutePath())));
            return new JSONObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
