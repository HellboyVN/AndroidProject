package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.Gson;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.model.ExerciseRealm;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Levels;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.MData;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LevelUtils {
    public static MData getLevelsData(Context context, int tab) {
        try {
            InputStream ims;
            AssetManager assetManager = context.getAssets();
            switch (tab) {
                case 20:
                    ims = assetManager.open("json/data_female.json");
                    break;
                case 22:
                    ims = assetManager.open("json/data_female_challenge.json");
                    break;
                default:
                    ims = assetManager.open("json/data_female_treadmill.json");
                    break;
            }
            return (MData) new Gson().fromJson(new InputStreamReader(ims), MData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
