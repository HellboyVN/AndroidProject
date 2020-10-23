package hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners;

public interface EventsListener {
    void iabSetupFailed();

    void notifyAdapterUpdate();

    void setWaitScreen(boolean z);

    void updateRemoveAdsUI();
}
