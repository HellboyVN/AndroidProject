package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.listeners;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.listener.MIAPListener;
import java.util.ArrayList;
import java.util.List;

public class EventCenter {
    private static EventCenter _instance;
    List<MIAPListener> miapListeners = new ArrayList();

    private EventCenter() {
    }

    public static EventCenter getInstance() {
        if (_instance == null) {
            _instance = new EventCenter();
        }
        return _instance;
    }

    public void addIAPListener(MIAPListener listener) {
        if (!this.miapListeners.contains(listener)) {
            this.miapListeners.add(listener);
        }
    }

    public void removeIAPListener(MIAPListener listener) {
        this.miapListeners.remove(listener);
    }

    public void notifyUpdateRemoveAdsUI() {
        for (MIAPListener miapListener : this.miapListeners) {
            if (miapListener != null) {
                miapListener.updateRemoveAdsUI();
            }
        }
    }
}
