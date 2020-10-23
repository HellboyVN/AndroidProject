package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabfour.liseners;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabfour.model.ChallengeDay;

public interface IDaySelect {
    void onChangePlanClick();

    void onDayClick(ChallengeDay challengeDay);

    void onResetProgressClick();
}
