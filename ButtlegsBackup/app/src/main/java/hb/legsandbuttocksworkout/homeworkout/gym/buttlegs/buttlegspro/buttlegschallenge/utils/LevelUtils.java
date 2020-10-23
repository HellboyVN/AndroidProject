package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.RealmManager;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.model.ExerciseRealm;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Exercise;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Levels;

public class LevelUtils {
    public static Levels getLevelsData(Context context) {
        try {
            InputStream ims;
            AssetManager assetManager = context.getAssets();
            switch (SharedPrefsService.getInstance().getGender(context)) {
                case 1:
                    ims = assetManager.open("json/data_male.json");
                    break;
                default:
                    ims = assetManager.open("json/data_female.json");
                    break;
            }
            return (Levels) new Gson().fromJson(new InputStreamReader(ims), Levels.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Levels getChallenfData(Context context) {
        try {
            InputStream ims;
            AssetManager assetManager = context.getAssets();
            switch (SharedPrefsService.getInstance().getGender(context)) {
                case 1:
                    ims = assetManager.open("json/data_male_challenge.json");
                    break;
                default:
                    ims = assetManager.open("json/data_female_challenge.json");
                    break;
            }
            return (Levels) new Gson().fromJson(new InputStreamReader(ims), Levels.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Exercise> getSavedExerciseData(Context context) {
        List<Exercise> exerciseData = new ArrayList();
        List<ExerciseRealm> exerciseRealms = RealmManager.getInstance().getExerciseData(context);
        Levels levels = getLevelsData(context);
        for (ExerciseRealm ex : exerciseRealms) {
            Exercise exercise = new Exercise();
            exercise.setDuration(Integer.valueOf(ex.getDuration()));
            exercise.setNameKey(ex.getName());
            exercise.setImgKey(getExerciseImageKey(levels, ex));
            if (checkIsExist(exerciseData, exercise)) {
                addDuration(exerciseData, exercise);
            } else {
                exerciseData.add(exercise);
            }
        }
        return exerciseData;
    }

    private static String getExerciseImageKey(Levels levels, ExerciseRealm ex) {
        String imageKey = "";
        for (Exercise exercise : levels.getAllExercises().getExercises()) {
            if (exercise.getNameKey().equals(ex.getName())) {
                return exercise.getImgKey();
            }
        }
        return imageKey;
    }

    private static boolean checkIsExist(List<Exercise> list, Exercise exercise) {
        for (Exercise ex : list) {
            if (ex.getNameKey().equals(exercise.getNameKey())) {
                return true;
            }
        }
        return false;
    }

    private static void addDuration(List<Exercise> exerciseList, Exercise exercise) {
        for (Exercise ex : exerciseList) {
            if (ex.getNameKey().equals(exercise.getNameKey())) {
                ex.setDuration(Integer.valueOf(ex.getDuration().intValue() + exercise.getDuration().intValue()));
            }
        }
    }
}
