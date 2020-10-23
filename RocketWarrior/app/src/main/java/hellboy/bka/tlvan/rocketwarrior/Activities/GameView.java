package hellboy.bka.tlvan.rocketwarrior.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.LinkedList;

import hellboy.bka.tlvan.rocketwarrior.Components.Background;
import hellboy.bka.tlvan.rocketwarrior.Components.BlueRocket;
import hellboy.bka.tlvan.rocketwarrior.Components.Bridge;
import hellboy.bka.tlvan.rocketwarrior.Components.Captain;
import hellboy.bka.tlvan.rocketwarrior.Components.RedRocket;
import hellboy.bka.tlvan.rocketwarrior.Components.Score;
import hellboy.bka.tlvan.rocketwarrior.Components.SoundGame;
import hellboy.bka.tlvan.rocketwarrior.R;
import hellboy.bka.tlvan.rocketwarrior.StaticVariable.GameSetup;
import hellboy.bka.tlvan.rocketwarrior.Ui.EndGame;


/**
 * Created by sev_user on 8/8/2016.
 */
class GameView extends SurfaceView implements Runnable {
    // all static variable
    private static final String TAG = "123";
    private int MIN_RED1_XSPEED = GameSetup.MIN_RED1_XSPEED;
    private int MAX_RED1_XSPEED = GameSetup.MAX_RED1_XSPEED;
    private int MAX_CAP_XSPEED = GameSetup.MAX_CAP_XSPEED;
    private static float MAX_ROCKET_Y, MIN_ROCKET_Y;
    private static float MAX_ROCKET_Y_RATIO = GameSetup.MAX_ROCKET_Y_RATIO;
    private static float MIN_ROCKET_Y_RATIO = GameSetup.MIN_ROCKET_Y_RATIO;
    private int MIN_BACKGROUND_XSPEED = GameSetup.MIN_BACKGROUND_XSPEED;
    private int MAX_BACKGROUND_XSPEED = GameSetup.MAX_BACKGROUND_XSPEED;
    private static final float MIN_ROCKET_DISTANCE = GameSetup.MIN_ROCKET_DISTANCE;
    private static final float MAX_ROCKET_DISTANCE = GameSetup.MAX_ROCKET_DISTANCE;
    private int MIN_BLUE_XSPEED = GameSetup.MIN_BLUE_XSPEED;
    private int MAX_BLUE_XSPEED = GameSetup.MAX_BLUE_XSPEED;

    Thread gameThread = null;
    SurfaceHolder surfaceHolder;
    boolean playing = false;
    private Paint paint;
    Canvas canvas;
    Captain captain;

    Context context;
    int screenX, screenY;
    // random distance to create a new rocket
    float randomDistance;
    // variables to make captain walking
    boolean isWalk = true;
    // captain speed
    float captainSpeed = 0;

    Background background;
    Score score;
    Bridge bridge;
    Bitmap circleBitmap;

    // Blue rocket;
    BlueRocket blueRocket;

    // sounds


    private LinkedList<RedRocket> redRockets;

    public GameView(Context context, int screenX, int screenY, int captainType, int backgroundType) {
        super(context);
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        MIN_RED1_XSPEED = GameSetup.dp2Px(3,context);
        MAX_RED1_XSPEED = GameSetup.dp2Px(6,context);
        MAX_CAP_XSPEED = GameSetup.dp2Px(3,context);
        MIN_BACKGROUND_XSPEED = GameSetup.dp2Px(0,context);
        MAX_BACKGROUND_XSPEED = GameSetup.dp2Px(4,context);
        MIN_BLUE_XSPEED = GameSetup.dp2Px(3,context);
        MAX_BLUE_XSPEED = GameSetup.dp2Px(6,context);

        // initial drawing object
        surfaceHolder = getHolder();
        paint = new Paint();

        // static variable
        MIN_ROCKET_Y = screenY * MIN_ROCKET_Y_RATIO;
        MAX_ROCKET_Y = screenY * MAX_ROCKET_Y_RATIO;

        //initial Captain
        captain = new Captain(context,  GameSetup.dp2Px(3,context), screenY * 3 / 5, screenX, screenY, captainType);

        // create red rockets
        redRockets = new LinkedList<>();
        if (redRockets.isEmpty()) {
            // add a initial rocket
            float Ystart = MAX_ROCKET_Y - GameSetup.dp2Px(100,context);
            redRockets.add(new RedRocket(context, MIN_RED1_XSPEED, Ystart, 1, screenX, screenY));
            randomDistance = screenX * MIN_ROCKET_DISTANCE + (int) (Math.random() * ((screenX * MAX_ROCKET_DISTANCE - screenX * MIN_ROCKET_DISTANCE) + 1));
        }

        // initial background
        background = new Background(context, MIN_BACKGROUND_XSPEED, 0, screenX, screenY, backgroundType);
        // initial score
        score = new Score(context, background);
        // initial bridge
        bridge = new Bridge(context,  GameSetup.dp2Px(50,context), screenY * 3 / 5 + GameSetup.dp2Px(30,context));
        Log.e("levan", String.valueOf(GameSetup.dp2Px(33,getContext())));
        Log.e("levan1", String.valueOf(GameSetup.dp2Px(50,getContext())));
        // score circle
        circleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.scorecircle);
        circleBitmap = Bitmap.createScaledBitmap(circleBitmap, GameSetup.dp2Px(50,context),  GameSetup.dp2Px(50,context), true);
    }



    @Override
    public void run() {
        //drawGui();
        while (playing) {
            if (captain.isAlive()) {
//            if (true) {
                update();
                draw();
                control();
            } else {
                // game over
                Intent intent = new Intent(context, SoundGame.class);
                intent.putExtra("idSound", 2);
                context.startService(intent);

                playing = false;
                destroy();
                Message message = new Message();
                message.what = score.getScore();
                endgameHandler.sendMessage(message);
            }
        }
    }

    private Handler endgameHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int value = msg.what;
            Intent intent = new Intent(context, EndGame.class);
            intent.putExtra("score", value);
            context.startActivity(intent);

        }

    };


    int blueFlag = 0;
    int fastJumpFlag = 0;

    private void update() {
        // update red rockets
        for (Iterator<RedRocket> iterator = redRockets.iterator(); iterator.hasNext(); ) {
            // remove a rocket
            RedRocket aRedRocket = iterator.next();
            if (aRedRocket.isDisappear() == true) {
                iterator.remove();
            }
            aRedRocket.update();
        }
        RedRocket lastRocket = redRockets.getLast();
        if (screenX - lastRocket.getX() >= randomDistance) {
            // add a new rocket
            float Ystart = MIN_ROCKET_Y + (int) (Math.random() * ((MAX_ROCKET_Y - MIN_ROCKET_Y) + 1));
            redRockets.add(new RedRocket(context, MIN_RED1_XSPEED, Ystart, 1, screenX, screenY));
            randomDistance = screenX * MIN_ROCKET_DISTANCE + (int) (Math.random() * ((screenX * MAX_ROCKET_DISTANCE - screenX * MIN_ROCKET_DISTANCE) + 1));
        }

//      update blue rocket
        if (score.getScore() % 10 == 0) {
            blueFlag++;
            if (blueFlag == 1) {
                float Ystart = MIN_ROCKET_Y + (int) (Math.random() * ((MAX_ROCKET_Y - GameSetup.dp2Px(100,context) - MIN_ROCKET_Y) + 1));
                blueRocket = new BlueRocket(context, MIN_BLUE_XSPEED, Ystart, screenX, screenY);
            }
        } else {
            blueRocket.update();
            blueFlag = 0;
        }

//        updateCaptain(0);

        if (captain.isJumpAboveRocket(blueRocket)) {
            fastJumpFlag = GameSetup.dp2Px(15,context);
        } else {
            if (fastJumpFlag > 0) fastJumpFlag--;
            else fastJumpFlag = 0;
        }

        // update fast jumping
        if (fastJumpFlag > 0) {
//             fast
            updateCaptain(10);
        } else {
//             normal
            updateCaptain(0);
        }

        // update score
        score.update();

        // update playing status
        if (captain.isAlive()) playing = true;
        else playing = true;

    }

    public void updateCaptain(float scaleSpeed) {

        // update captain
        bridge.update(GameSetup.dp2Px(1,context));
        if (isWalk) {
            if (redRockets.getFirst().getX() > GameSetup.dp2Px(250,context)) {
                // captain walking...
                captain.update(GameSetup.dp2Px(1,context));

            } else {
                // first jump
                isWalk = false;
                captain.Jump(GameSetup.dp2Px(3,context));
            }
        } else {
            for (RedRocket aRedRocket : redRockets) {
                if (captain.isJumpAboveRocket(aRedRocket)) {

                    captain.Jump(captainSpeed + scaleSpeed);
                    aRedRocket.setDrop(true);
//
                    Intent intent = new Intent(context, SoundGame.class);
                    intent.putExtra("idSound", 1);
                    context.startService(intent);
                }
                if (captain.isAttacked(aRedRocket)) {
                    captain.setAlive(false);
                }
            }
            if (captain.isJumpAboveRocket(blueRocket)) {
                captain.Jump(captainSpeed + scaleSpeed);
                blueRocket.setDrop(true);
//
                Intent intent = new Intent(context, SoundGame.class);
                intent.putExtra("idSound", 1);
                context.startService(intent);
            }
            if (captain.isAttacked(blueRocket)) {
                captain.setAlive(false);
            }
            captain.update(captainSpeed + scaleSpeed);
        }

        // when captain at threshold
        float Xtemp = captain.getX() + captainSpeed + scaleSpeed;
        if (Xtemp > screenX * GameSetup.SCREEN_THRESHOLD) {
            // set new speed to background and all rockets
            for (RedRocket aRedRocket : redRockets) {
                aRedRocket.setXspeed(MAX_RED1_XSPEED + scaleSpeed);
            }
            background.update(MAX_BACKGROUND_XSPEED + scaleSpeed);
            blueRocket.setXspeed(MAX_BLUE_XSPEED);
        } else {
            // set normal speed to background and all rockest
            for (RedRocket aRedRocket : redRockets) {
                aRedRocket.setXspeed(MIN_RED1_XSPEED );
            }
            background.update(MIN_BACKGROUND_XSPEED );
            blueRocket.setXspeed(MIN_BLUE_XSPEED);
        }
    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            // draw background
            canvas.drawBitmap(background.getBitmap(), background.getX(), 0, paint);
            // draw captain
            canvas.drawBitmap(captain.getBitmap(), captain.getX(), captain.getY(), paint);
            // draw bridge
            canvas.drawBitmap(bridge.getBitmap(), bridge.getX(), bridge.getY(), paint);
            //draw gui
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_left), (int) (screenX/6-screenX/8), (int)screenY*5/6, paint);
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_right), (int) screenX*5/6, (int)screenY*5/6, paint);
            // draw rockets
            for (RedRocket aRedRocket : redRockets) {
                canvas.drawBitmap(aRedRocket.getBitmap(), aRedRocket.getX(), aRedRocket.getY(), paint);
            }
            canvas.drawBitmap(blueRocket.getBitmap(), blueRocket.getX(), blueRocket.getY(), paint);

            // score circle
//            canvas.drawBitmap(circleBitmap, 270, 200 ,paint);
            // score
            paint.setTextSize(GameSetup.dp2Px(23,context));
            Typeface typeface = Typeface.DEFAULT_BOLD;
            paint.setColor(Color.BLACK);
            paint.setTypeface(typeface);
            paint.setTextSize(GameSetup.dp2Px(50,context));
            canvas.drawText(score.getScore()+"m", GameSetup.dp2Px(135,context), GameSetup.dp2Px(100,context), paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void drawGui(){
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
                canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.arrow_left), 100, screenY *3/5 +60, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void control() {
        try {
//            gameThread.sleep(17);
            gameThread.sleep(9);
        } catch (InterruptedException e) {

        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
//            gameThread.stop();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        if (captain.isAlive()) playing = true;
        else playing = true;
        gameThread = new Thread(this);
        //drawGui();
        gameThread.start();

    }

    public void destroy() {
        // destroy captain
        captain.destroy();

        // destroy rockets
        for (RedRocket aRedRocket : redRockets) {
            aRedRocket.destroy();
        }

        // destroy background
        background.destroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isRightTouched = event.getX() > screenX / 2;
        switch (event.getAction() & event.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (isRightTouched) {
                    captainSpeed = MAX_CAP_XSPEED;
                } else {
                    captainSpeed = -MAX_CAP_XSPEED;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isRightTouched) {
                    captainSpeed = 0;
                } else {
                    captainSpeed = -0.001f;
                }
                break;
        }
        return true;
    }
}
