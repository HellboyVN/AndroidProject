package hellboy.bka.tlvan.rocketwarrior.Components;

import android.content.Context;

/**
 * Created by sev_user on 8/10/2016.
 */
public class Score {
    Context context;
    Background background;
    int score;


    float counter;

    public Score(Context context, Background background) {
        this.context = context;
        this.background = background;
        this.score = 0;
        counter = 0;
    }

    public void update() {
        if (counter <= 100) {
            counter += background.getXSpeed();
        } else {
            score++;
            counter = 0;
        }

    }

    public int getScore() {
        return score;
    }
}
