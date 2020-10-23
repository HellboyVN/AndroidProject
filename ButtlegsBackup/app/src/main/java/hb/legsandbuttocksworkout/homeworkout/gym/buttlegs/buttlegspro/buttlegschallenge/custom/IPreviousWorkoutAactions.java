package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.custom;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Exercise;
import java.util.List;

public interface IPreviousWorkoutAactions extends IActions {
    void onPreview(List<Exercise> list);

    void onRestore();
}
