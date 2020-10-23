package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners;

import java.util.HashSet;
import java.util.Set;

public class ListenerManager {
    private static ListenerManager _instance;
    private Set<ExerciseTimeStatListener> exerciseTimeStatListenerList = new HashSet();

    public interface ExerciseTimeStatListener {
        void onPurchase();
    }

    private ListenerManager() {
    }

    public static ListenerManager getInstance() {
        if (_instance == null) {
            _instance = new ListenerManager();
        }
        return _instance;
    }

    public void addExerciseTimeStatListener(ExerciseTimeStatListener listener) {
        this.exerciseTimeStatListenerList.add(listener);
    }

    public void removeExerciseTimeStatListener(ExerciseTimeStatListener listener) {
        this.exerciseTimeStatListenerList.remove(listener);
    }

    public void notifyExerciseTimeStatPurchase() {
        for (ExerciseTimeStatListener listener : this.exerciseTimeStatListenerList) {
            listener.onPurchase();
        }
    }
}
