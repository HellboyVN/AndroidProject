package com.tlvanelearning.ielts.player;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tlvanelearning.ielts.R;
import com.tlvanelearning.ielts.database.KEYConstants;
import com.tlvanelearning.ielts.player.PlayerListener.CallbackActivityListener;
import com.tlvanelearning.ielts.player.PlayerListener.NotificationServiceListener;
import com.tlvanelearning.ielts.player.PlayerListener.PlayerViewServiceListener;
import com.tlvanelearning.ielts.util.FileUtil;
import com.tlvanelearning.ielts.util.NetworkUtil;
import com.tlvanelearning.ielts.util.PrefUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PlayerService extends Service implements KEYConstants {
    private static final String TAG = PlayerService.class.getSimpleName();
    public static PlayerService mInstance;
    private CallbackActivityListener callbackActivityListener;
    private BroadcastReceiver downloadBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Query query = new Query();
                long receivedID = intent.getLongExtra("extra_download_id", -1);
                query.setFilterById(new long[]{receivedID});
                Cursor cur = downloadManager.query(query);
                int index = cur.getColumnIndex("status");
                if (cur.moveToFirst()) {
                    switch (cur.getInt(index)) {
                        case 8:
                            try {
                                String filePath = cur.getString(cur.getColumnIndex("local_uri"));
                                File from = new File(filePath.replace("file://", ""));
                                if (from.exists()) {
                                    from.renameTo(new File(PlayerService.this.getExternalFilesDir(null).getPath() + "/" + filePath.substring(filePath.lastIndexOf("/") + 1)));
                                }
                                if (PlayerService.this.playerServiceListener != null) {
                                    PlayerService.this.playerServiceListener.onDownloadSuccess();
                                }
                                downloadManager.remove(new long[]{receivedID});
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                    }
                }
                cur.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (PlayerService.this.playerServiceListener != null) {
                    PlayerService.this.playerServiceListener.onProgressBar(false);
                }
            }
        }
    };
    private boolean isPlaying;
    private final IBinder mBinder = new PlayerServiceBinder();
    private int mPosition = -1;
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtil.isOnline(context)) {
                PlayerService.this.registerNetworkReceiver();
            } else if (PlayerService.this.waitNetworkChange) {
                NetworkUtil.toastPlay(context);
                PlayerService.this.waitNetworkChange = false;
                PlayerService.this.play(PlayerService.this.mPosition);
                PlayerService.this.unRegisterNetWorkReceiver();
            }
        }
    };
    private NotificationServiceListener notificationListener;
    private NotificationPlayerService notificationPlayerService;
    OnBufferingUpdateListener onBufferingUpdateListener = new OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
            if (PlayerService.this.playerServiceListener != null) {
                PlayerService.this.playerServiceListener.onBufferingUpdate(percent);
            }
        }
    };
    OnCompletionListener onCompletionListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            try {
                if (PlayerService.this.playerServiceListener != null) {
                    PlayerService.this.playerServiceListener.onCompletedAudio();
                }
                if (PlayerService.this.notificationListener != null) {
                    PlayerService.this.notificationListener.onCompletedAudio();
                }
                if (PrefUtil.getRepeatValue(PlayerService.getInstance(), "REPEATE")) {
                    PlayerService.this.seekTo(0);
                    PlayerService.this.play(PlayerService.this.mPosition);
                    return;
                }
                PlayerService.this.nextAudio();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    OnErrorListener onErrorListener = new OnErrorListener() {
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            return false;
        }
    };
    OnPreparedListener onPreparedListener = new OnPreparedListener() {
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            PlayerService.this.isPlaying = true;
            PlayerService.this.updateTimeAudio();
            if (PlayerService.this.playerServiceListener != null) {
                PlayerService.this.playerServiceListener.onPrepared();
                PlayerService.this.playerServiceListener.onPlaying();
                PlayerService.this.playerServiceListener.onPreparedAudio(mediaPlayer.getDuration());
                PlayerService.this.playerServiceListener.onProgressBar(false);
            }
            if (PlayerService.this.notificationListener != null) {
                PlayerService.this.notificationListener.onPlaying();
            }
        }
    };
    private PlayerViewServiceListener playerServiceListener;
    private ArrayList<JSONObject> playlist;
    private boolean registerNetWorkChange;
    private int tempPosition;
    private boolean waitNetworkChange;

    private class DownloadAsyncTask extends AsyncTask<Boolean, Integer, String> {
        private DownloadAsyncTask() {
        }

        protected String doInBackground(Boolean... bool) {
            try {
                if (!(PlayerService.this.playlist == null || PlayerService.this.playlist.size() == 0)) {
                    JSONObject currentTrack = (JSONObject) PlayerService.this.playlist.get(PlayerService.this.mPosition);
                    if (!(currentTrack == null || !currentTrack.has("audio") || FileUtil.fileExists(PlayerService.this, currentTrack.getString("audio")))) {
                        Uri destinationUri;
                        boolean visible = bool[0].booleanValue();
                        if (PlayerService.this.playerServiceListener != null) {
                            PlayerService.this.playerServiceListener.onProgressBar(visible);
                        }
                        DownloadManager downloadManager = (DownloadManager) PlayerService.this.getSystemService(DOWNLOAD_SERVICE);
                        File direct = new File(PlayerService.this.getExternalFilesDir(null).getPath());
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }
                        Uri downloadUri = Uri.parse(currentTrack.getString("audio"));
                        File cache;
                        if (Environment.getExternalStorageState() == null) {
                            cache = new File(Environment.getDataDirectory() + "/ielts-cache");
                            if (!cache.exists()) {
                                cache.mkdir();
                            }
                            destinationUri = Uri.fromFile(new File(cache.getAbsolutePath() + "/" + FileUtil.getFilename(currentTrack.getString("audio"))));
                        } else {
                            cache = new File(Environment.getExternalStorageDirectory() + "/ielts-cache");
                            if (!cache.exists()) {
                                cache.mkdir();
                            }
                            destinationUri = Uri.fromFile(new File(cache.getAbsolutePath() + "/" + FileUtil.getFilename(currentTrack.getString("audio"))));
                        }
                        Request request = new Request(downloadUri);
                        if (VERSION.SDK_INT >= 11) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(visible ? 1 : 2);
                        }
                        request.setAllowedNetworkTypes(3).setAllowedOverRoaming(false).setTitle(currentTrack.getString("title")).setDescription(PlayerService.this.getString(R.string.app_name)).setDestinationUri(destinationUri);
                        downloadManager.enqueue(request);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (PlayerService.this.playerServiceListener != null) {
                    PlayerService.this.playerServiceListener.onProgressBar(false);
                }
            }
            return null;
        }
    }

    private class Player extends AsyncTask<String, Integer, Boolean> {
        private MediaPlayer mediaPlayer;

        public Player(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        protected Boolean doInBackground(String... strings) {
            boolean retVal = true;
            try {
                String URL = strings[0];
                if (FileUtil.fileExists(PlayerService.this.getContext(), URL)) {
                    this.mediaPlayer.setDataSource(PlayerService.this.getContext(), Uri.parse(FileUtil.filePath(PlayerService.this.getContext(), URL)));
                    this.mediaPlayer.prepareAsync();
                    if (PlayerService.this.playerServiceListener != null) {
                        PlayerService.this.playerServiceListener.onDownloadSuccess();
                    }
                } else if (NetworkUtil.isOnline(PlayerService.this.getContext())) {
                    this.mediaPlayer.setDataSource(URL);
                    this.mediaPlayer.prepareAsync();
                    if (PlayerService.this.playerServiceListener != null) {
                        PlayerService.this.playerServiceListener.onProgressBar(true);
                    }
                } else {
                    this.mediaPlayer = null;
                    if (PlayerService.this.playerServiceListener != null) {
                        PlayerService.this.playerServiceListener.onContinueAudio();
                        PlayerService.this.playerServiceListener.onPaused();
                    }
                    PlayerService.this.waitNetworkChange = true;
                    retVal = false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return Boolean.valueOf(retVal);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (this.mediaPlayer != null) {
                this.mediaPlayer.setOnPreparedListener(PlayerService.this.onPreparedListener);
                this.mediaPlayer.setOnBufferingUpdateListener(PlayerService.this.onBufferingUpdateListener);
                this.mediaPlayer.setOnCompletionListener(PlayerService.this.onCompletionListener);
                this.mediaPlayer.setOnErrorListener(PlayerService.this.onErrorListener);
            }
        }

        protected void onPostExecute(Boolean retVal) {
            super.onPostExecute(retVal);
            if (!retVal.booleanValue()) {
                PlayerService.this.registerNetworkReceiver();
                NetworkUtil.toastMessage(PlayerService.this.getContext());
            }
        }
    }

    public class PlayerServiceBinder extends Binder {
        public PlayerService getService() {
            PlayerService playerService = PlayerService.this;
            PlayerService.mInstance = playerService;
            return playerService;
        }
    }

    public static PlayerService getInstance() {
        return mInstance;
    }

    public void registerNotificationListener(NotificationServiceListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    public void registerCallbackActivityListener(CallbackActivityListener callbackActivityListener) {
        this.callbackActivityListener = callbackActivityListener;
    }

    public void registerServicePlayerListener(PlayerViewServiceListener playerServiceListeners) {
        this.playerServiceListener = playerServiceListeners;
        if (this.isPlaying) {
            this.playerServiceListener.onPlaying();
        }
    }

    public void unRegisterServicePlayerListener() {
        this.playerServiceListener = null;
    }

    private void registerDownloadReceiver() {
        registerReceiver(this.downloadBroadcastReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
    }

    private void registerNetworkReceiver() {
        if (!this.registerNetWorkChange) {
            this.registerNetWorkChange = true;
            registerReceiver(this.networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    private void unRegisterDownloadReceiver() {
        unregisterReceiver(this.downloadBroadcastReceiver);
    }

    private void unRegisterNetWorkReceiver() {
        if (this.registerNetWorkChange) {
            this.registerNetWorkChange = false;
            unregisterReceiver(this.networkChangeReceiver);
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        try {
            this.notificationPlayerService = new NotificationPlayerService(this);
            registerDownloadReceiver();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            unRegisterDownloadReceiver();
            unRegisterNetWorkReceiver();
            if (this.notificationPlayerService != null) {
                this.notificationPlayerService.destroyNotificationIfExists();
                this.notificationPlayerService = null;
            }
            if (this.notificationListener != null) {
                this.notificationListener = null;
            }
            if (this.playerServiceListener != null) {
                this.playerServiceListener = null;
            }
            if (this.callbackActivityListener != null) {
                this.callbackActivityListener = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createNotificationPlayer() {
        if (this.isPlaying) {
            try {
                if (this.playlist != null && this.playlist.size() != 0) {
                    JSONObject jsonObject = (JSONObject) this.playlist.get(this.mPosition);
                    if (jsonObject != null && jsonObject.has("title")) {
                        this.notificationPlayerService.createNotificationPlayer(jsonObject.getString("title"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateNotification() {
        if (this.notificationPlayerService != null) {
            this.notificationPlayerService.updateNotification();
        }
    }

    public void pauseAudio() {
        try {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.pause();
                this.isPlaying = false;
            }
            if (this.playerServiceListener != null) {
                this.playerServiceListener.onPaused();
            }
            if (this.notificationListener != null) {
                this.notificationListener.onPaused();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        if (this.mediaPlayer != null) {
            try {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.isPlaying = false;
    }

    public void stopService() {
        stop();
        stopSelf();
    }

    public void nextAudio() {
        if (this.playlist != null || this.playlist.size() != 0) {
            try {
                boolean shuffle = PrefUtil.getBooleanValue(this, "PREFS_SHUFFLE");
                int nextPosition = this.mPosition;
                if (shuffle) {
                    Random random = new Random();
                    while (nextPosition == this.mPosition) {
                        nextPosition = random.nextInt(this.playlist.size());
                    }
                    updateData(nextPosition);
                    return;
                }
                if (nextPosition >= this.playlist.size() - 1) {
                    nextPosition = 0;
                } else {
                    nextPosition++;
                }
                updateData(nextPosition);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public void previousAudio() {
        if (this.playlist == null || this.playlist.size() != 0) {
            try {
                int prevPosition = this.mPosition;
                if (prevPosition == 0) {
                    prevPosition = this.playlist.size() - 1;
                } else {
                    prevPosition--;
                }
                updateData(prevPosition);
            } catch (Exception e) {
            }
        }
    }

    public void continueAudio() {
        play(this.mPosition);
    }

    public void play(int position) {
        this.tempPosition = this.mPosition;
        this.mPosition = position;
        try {
            if (this.playlist != null && this.playlist.size() != 0) {
                JSONObject currentTrack = (JSONObject) this.playlist.get(position);
                if (currentTrack != null && currentTrack.has("audio")) {
                    String URL = currentTrack.getString("audio");
                    if (this.mediaPlayer == null) {
                        this.mediaPlayer = new MediaPlayer();
                        new Player(this.mediaPlayer).execute(new String[]{URL});
                    } else if (this.isPlaying) {
                        stop();
                        play(position);
                    } else if (this.tempPosition != position) {
                        stop();
                        play(position);
                    } else {
                        this.mediaPlayer.start();
                        this.isPlaying = true;
                        updateTimeAudio();
                        if (this.playerServiceListener != null) {
                            this.playerServiceListener.onPlaying();
                            this.playerServiceListener.onContinueAudio();
                        }
                        if (this.notificationListener != null && this.isPlaying) {
                            this.notificationListener.onPlaying();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void seekTo(int time) {
        if (this.mediaPlayer != null) {
            try {
                this.mediaPlayer.seekTo(time);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void download(boolean visible) {
        new DownloadAsyncTask().execute(new Boolean[]{Boolean.valueOf(visible)});
    }

    public void kill() {
        this.isPlaying = false;
        try {
            if (this.playerServiceListener != null) {
                this.playerServiceListener.onKill();
            }
            if (this.callbackActivityListener != null) {
                this.callbackActivityListener.onKill();
            } else {
                stopService();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateData(int position) {
        try {
            if (this.playerServiceListener != null) {
                this.playerServiceListener.updateData(position);
            } else {
                play(this.mPosition);
            }
            if (this.notificationListener != null && this.playlist != null && this.playlist.size() != 0) {
                this.notificationListener.updateTitle(((JSONObject) this.playlist.get(position)).getString("title"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateTimeAudio() {
        new Thread() {
            public void run() {
                Exception e;
                while (PlayerService.this.isPlaying) {
                    try {
                        if (PlayerService.this.playerServiceListener != null) {
                            PlayerService.this.playerServiceListener.onTimeChanged((long) PlayerService.this.mediaPlayer.getCurrentPosition());
                            PlayerService.this.playerServiceListener.onDuration(PlayerService.this.mediaPlayer.getDuration());
                        }
                        if (PlayerService.this.notificationListener != null && PlayerService.this.isPlaying) {
                            PlayerService.this.notificationListener.onTimeChanged((long) PlayerService.this.mediaPlayer.getCurrentPosition());
                        }
                        Thread.sleep(200);
                    } catch (IllegalStateException e2) {
                        e = e2;
                        e.printStackTrace();
                    } catch (InterruptedException e3) {
                        e = e3;
                        e.printStackTrace();
                    } catch (NullPointerException e4) {
                        e = e4;
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public int getTrackPosition() {
        return this.mPosition;
    }

    public void setPlaylist(ArrayList<JSONObject> playlist) {
        this.playlist = playlist;
    }

    public ArrayList<JSONObject> getPlaylist() {
        return this.playlist;
    }

    private Context getContext() {
        return this;
    }
}
