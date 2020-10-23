package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.liseners;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.model.ChallengeDay;

public interface IDaySelect {
    void onChangePlanClick();

    void onDayClick(ChallengeDay challengeDay);

    void onResetProgressClick();
}
