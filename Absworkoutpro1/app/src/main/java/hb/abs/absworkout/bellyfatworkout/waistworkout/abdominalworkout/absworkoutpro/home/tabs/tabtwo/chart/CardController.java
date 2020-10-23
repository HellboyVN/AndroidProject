package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.widget.RelativeLayout;

public class CardController {
    protected boolean firstStage;
    private final Runnable showAction = new Runnable() {
        public void run() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CardController.this.show(CardController.this.unlockAction);
                }
            }, 500);
        }
    };
    private final Runnable unlockAction = new Runnable() {
        public void run() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CardController.this.unlock();
                }
            }, 500);
        }
    };

    protected CardController(CardView card) {
        RelativeLayout toolbar = (RelativeLayout) card.findViewById(R.id.chart_toolbar);
    }

    public void init() {
        show(this.unlockAction);
    }

    protected void show(Runnable action) {
        lock();
        this.firstStage = false;
    }

    protected void update() {
        lock();
        this.firstStage = !this.firstStage;
    }

    protected void dismiss(Runnable action) {
        lock();
    }

    private void lock() {
    }

    private void unlock() {
    }
}
