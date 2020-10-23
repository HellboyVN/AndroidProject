package hellboy.bka.tlvan.blowwifiprank.Process;

/**
 * Created by tlvan on 9/14/2017.
 */

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Process;
import android.util.Log;

public class AudioMeter extends Thread {
    /////////////////////////////////////////////////////////////////
    // PUBLIC CONSTANTS

    // Convenience constants
    public static final int AMP_SILENCE = 0;
    public static final int AMP_NORMAL_BREATHING = 10;
    public static final int AMP_MOSQUITO = 20;
    public static final int AMP_WHISPER = 30;
    public static final int AMP_STREAM = 40;
    public static final int AMP_QUIET_OFFICE = 50;
    public static final int AMP_NORMAL_CONVERSATION = 60;
    public static final int AMP_HAIR_DRYER = 70;
    public static final int AMP_GARBAGE_DISPOSAL = 80;

    /////////////////////////////////////////////////////////////////
    // PRIVATE CONSTANTS

    private static final float MAX_REPORTABLE_AMP = 32767f;
    private static final float MAX_REPORTABLE_DB = 90.3087f;

    /////////////////////////////////////////////////////////////////
    // PRIVATE MEMBERS

    private AudioRecord mAudioRecord;
    private int mSampleRate;
    private short mAudioFormat;
    private short mChannelConfig;

    private short[] mBuffer;
    private int mBufferSize = AudioRecord.ERROR_BAD_VALUE;

    private int mLocks = 0;

    /////////////////////////////////////////////////////////////////
    // CONSTRUCTOR

    public AudioMeter() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
        createAudioRecord();
    }

    /////////////////////////////////////////////////////////////////
    // PUBLIC METHODS

    public AudioMeter getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public float getAmplitude() {
        return (float) (MAX_REPORTABLE_DB + (20 * Math.log10(getRawAmplitude() / MAX_REPORTABLE_AMP)));
    }

    public synchronized void startRecording() {
        if (mAudioRecord == null || mAudioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            throw new IllegalStateException("startRecording() called on an uninitialized AudioRecord.");
        }

        if (mLocks == 0) {
            createAudioRecord();
            mAudioRecord.startRecording();
        }

        mLocks++;
    }

    public synchronized void stopRecording() {
        mLocks--;

        //if (mLocks == 0)
        {
            if (mAudioRecord != null) {
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }
    }

    /////////////////////////////////////////////////////////////////
    // PRIVATE METHODS

    private void createAudioRecord() {
        if (mSampleRate > 0 && mAudioFormat > 0 && mChannelConfig > 0) {
            mAudioRecord = new AudioRecord(AudioSource.MIC, mSampleRate, mChannelConfig, mAudioFormat, mBufferSize);

            return;
        }

        // Find best/compatible AudioRecord
        for (int sampleRate : new int[]{8000, 11025, 16000, 22050, 32000, 44100, 47250, 48000}) {
            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT}) {
                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_MONO,AudioFormat.CHANNEL_IN_STEREO,
                        AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.CHANNEL_CONFIGURATION_STEREO}) {

                    // Try to initialize
                    try {
                        mBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

                        if (mBufferSize < 0) {
                            continue;
                        }

                        mBuffer = new short[mBufferSize];
                        mAudioRecord = new AudioRecord(AudioSource.MIC, sampleRate, channelConfig, audioFormat,
                                mBufferSize);

                        if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                            mSampleRate = sampleRate;
                            mAudioFormat = audioFormat;
                            mChannelConfig = channelConfig;
                            Log.e("mSampleRate ", String.valueOf(mSampleRate));
                            Log.e("mAudioFormat ", String.valueOf(mAudioFormat));
                            Log.e("mChannelConfig ", String.valueOf(mChannelConfig));
                            return;
                        }

                        mAudioRecord.release();
                        mAudioRecord = null;
                    } catch (Exception e) {
                        // Do nothing
                    }
                }
            }
        }
    }

    private int getRawAmplitude() {
        if (mAudioRecord == null) {
            createAudioRecord();
        }
        mBufferSize = mAudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, mAudioFormat);
        mBuffer = new short[mBufferSize];
        mAudioRecord.read(mBuffer, 0, mBufferSize);
        for (short s : mBuffer) {
            System.out.println("Blow Value=" + Math.abs(s));
            if (Math.abs(s) > 350 && Math.abs(s) < 5000)   //DETECT VOLUME (IF I BLOW IN THE MIC)
            {
                Log.e("Math.abs(s)", String.valueOf(Math.abs(s)));
                return Math.abs(s);

            }

        }
        return 0;
    }

    /////////////////////////////////////////////////////////////////
    // PRIVATE CLASSES

    private static class InstanceHolder {
        private static final AudioMeter INSTANCE = new AudioMeter();
    }
}
