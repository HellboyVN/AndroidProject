package hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.listeners;

public interface IProgressWheel {
    void onTimerFinish();

    void onTimerPause();

    void onTimerPlay();

    void onTimerRepeat(int i);
}
