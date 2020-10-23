package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners;

public interface IProgressWheel {
    void onTimerFinish();

    void onTimerPause();

    void onTimerPlay();

    void onTimerRepeat(int i);
}
