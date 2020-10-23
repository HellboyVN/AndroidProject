package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.models.ChallengeDay;

public interface IDaySelect {
    void onChangePlanClick();

    void onDayClick(ChallengeDay challengeDay);

    void onResetProgressClick();
}
