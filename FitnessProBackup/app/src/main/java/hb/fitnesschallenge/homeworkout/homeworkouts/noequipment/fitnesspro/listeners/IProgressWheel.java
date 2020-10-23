package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.listeners;

public interface IProgressWheel {
    void onTimerFinish();

    void onTimerPause();

    void onTimerPlay();

    void onTimerRepeat(int i);
}
