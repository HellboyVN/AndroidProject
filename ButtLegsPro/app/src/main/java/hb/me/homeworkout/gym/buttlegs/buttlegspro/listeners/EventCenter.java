package hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.lisener.IMetricChange;
import java.util.ArrayList;
import java.util.List;

public class EventCenter {
    private static EventCenter _instance;
    List<IMetricChange> mMetricChangeListeners = new ArrayList();
    List<EventsListener> miapListeners = new ArrayList();

    private EventCenter() {
    }

    public static EventCenter getInstance() {
        if (_instance == null) {
            _instance = new EventCenter();
        }
        return _instance;
    }

    public void addIAPListener(EventsListener listener) {
        if (!this.miapListeners.contains(listener)) {
            this.miapListeners.add(listener);
        }
    }

    public void removeIAPListener(EventsListener listener) {
        this.miapListeners.remove(listener);
    }

    public void notifyUpdateRemoveAdsUI() {
        for (EventsListener miapListener : this.miapListeners) {
            if (miapListener != null) {
                miapListener.updateRemoveAdsUI();
            }
        }
    }

    public void notifyAdapterUpdate() {
        for (EventsListener miapListener : this.miapListeners) {
            if (miapListener != null) {
                miapListener.notifyAdapterUpdate();
            }
        }
    }

    public void addMetricChangeListener(IMetricChange listener) {
        if (!this.mMetricChangeListeners.contains(listener)) {
            this.mMetricChangeListeners.add(listener);
        }
    }

    public void removeMetricChangeListener(IMetricChange listener) {
        this.mMetricChangeListeners.remove(listener);
    }

    public void notifyWeightMetricChanged(int metric) {
        for (IMetricChange listener : this.mMetricChangeListeners) {
            if (listener != null) {
                listener.onWeightMetricChange(metric);
            }
        }
    }
}
