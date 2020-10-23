package com.tlvanelearning.ielts.player;

public class PlayerListener {

    public interface CallbackActivityListener {
        void onKill();
    }

    public interface NotificationServiceListener {
        void onCompletedAudio();

        void onPaused();

        void onPlaying();

        void onTimeChanged(long j);

        void updateTitle(String str);
    }

    public interface PlayerViewServiceListener {
        void onBufferingUpdate(int i);

        void onCompletedAudio();

        void onContinueAudio();

        void onDownloadSuccess();

        void onDuration(int i);

        void onKill();

        void onPaused();

        void onPlaying();

        void onPrepared();

        void onPreparedAudio(int i);

        void onProgressBar(boolean z);

        void onTimeChanged(long j);

        void updateData(int i);
    }
}
