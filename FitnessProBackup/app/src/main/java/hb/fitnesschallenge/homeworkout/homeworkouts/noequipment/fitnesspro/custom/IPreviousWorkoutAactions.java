package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.custom;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models.Exercise;
import java.util.List;

public interface IPreviousWorkoutAactions extends IActions {
    void onPreview(List<Exercise> list);

    void onRestore();
}
