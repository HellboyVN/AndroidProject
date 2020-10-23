package hb.me.homeworkout.gym.buttlegs.buttlegspro.custom;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import java.util.List;

public interface IPreviousWorkoutAactions extends IActions {
    void onPreview(List<Exercise> list);

    void onRestore();
}
