package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.liseners;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.model.ChallengeDay;

public interface IDaySelect {
    void onChangePlanClick();

    void onDayClick(ChallengeDay challengeDay);

    void onResetProgressClick();
}
