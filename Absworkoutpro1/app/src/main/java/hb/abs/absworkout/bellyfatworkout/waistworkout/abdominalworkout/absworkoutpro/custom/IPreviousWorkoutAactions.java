package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.custom;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.Exercise;
import java.util.List;

public interface IPreviousWorkoutAactions extends IActions {
    void onPreview(List<Exercise> list);

    void onRestore();
}
