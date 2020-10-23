package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils;

public class RestartAppModel {
    private static RestartAppModel mInstance;
    private OnAppRestartListener mListener;
    private boolean mState;

    public interface OnAppRestartListener {
        void restartApp();
    }

    private RestartAppModel() {
    }

    public static RestartAppModel getInstance() {
        if (mInstance == null) {
            mInstance = new RestartAppModel();
        }
        return mInstance;
    }

    public void setListener(OnAppRestartListener listener) {
        this.mListener = listener;
    }

    public void settingsChanged(boolean state) {
        if (this.mListener != null) {
            this.mState = state;
            notifyStateChange();
        }
    }

    public boolean getState() {
        return this.mState;
    }

    private void notifyStateChange() {
        this.mListener.restartApp();
    }
}
