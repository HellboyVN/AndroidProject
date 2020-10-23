package tlvan.humax.vlclib;

/**
 * Created by tlvan on 1/5/2018.
 */

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

public class VideoVLCActivity extends Activity  {
    private static final String TAG = VideoVLCActivity.class.getSimpleName();

    // size of the video
    private int mVideoHeight;
    private int mVideoWidth;
    private int mVideoVisibleHeight;
    private int mVideoVisibleWidth;
    private int mSarNum;
    private int mSarDen;

    private SurfaceView mSurfaceView;
    private FrameLayout mSurfaceFrame;
    private SurfaceHolder mSurfaceHolder;
    private Surface mSurface = null;

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;
    private String mMediaUrl;
    private Media mMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "VideoVLC -- onCreate -- START ------------");
        setContentView(R.layout.activity_video_vlc);

        mSurfaceView = (SurfaceView) findViewById(R.id.player_surface);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceFrame = (FrameLayout) findViewById(R.id.player_surface_frame);
        mMediaUrl = getIntent().getExtras().getString("videoUrl");
        final ArrayList<String> args = new ArrayList<>();
        //have not used yet
        args.add("--aout=android_audiotrack");
        args.add("--android-display-chroma");
        args.add("RV16");
        args.add("--vout=android_display,none");
        try {
            mLibVLC = new LibVLC(this,null);
            mMedia = new Media(mLibVLC,Uri.parse(mMediaUrl));
            mMediaPlayer = new MediaPlayer(mMedia);
            this.mMediaPlayer.getVLCVout().attachViews(new IVLCVout.OnNewVideoLayoutListener() {
                @Override
                public void onNewVideoLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

                }
            });
            this.mMediaPlayer.getVLCVout().setVideoSurface(mSurfaceHolder.getSurface(), mSurfaceHolder);
            mMediaPlayer.play();


        } catch (Exception e){
            Log.e(TAG, e.toString());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMediaPlayer.release();
        mLibVLC.release();
    }
    @Override
    protected void onStart() {
        super.onStart();

        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        vlcVout.setVideoView(mSurfaceView);
        vlcVout.attachViews();
        Media media = new Media(mLibVLC, Uri.parse(mMediaUrl));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();

    }

    @Override
    protected void onStop() {
        super.onStop();

        mMediaPlayer.stop();
        mMediaPlayer.getVLCVout().detachViews();
      //  mMediaPlayer.getVLCVout().removeCallback((IVLCVout.Callback) this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vieo_vlc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void eventHardwareAccelerationError() {
        Log.e(TAG, "eventHardwareAccelerationError()!");
        return;
    }


    public void setSurfaceLayout(final int width, final int height, int visible_width, int visible_height, final int sar_num, int sar_den){
        Log.d(TAG, "setSurfaceSize -- START");
        if (width * height == 0)
            return;

        // store video size
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisibleHeight = visible_height;
        mVideoVisibleWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;

        Log.d(TAG, "setSurfaceSize -- mMediaUrl: " + mMediaUrl + " mVideoHeight: " + mVideoHeight + " mVideoWidth: " + mVideoWidth + " mVideoVisibleHeight: " + mVideoVisibleHeight + " mVideoVisibleWidth: " + mVideoVisibleWidth + " mSarNum: " + mSarNum + " mSarDen: " + mSarDen);
    }


}
