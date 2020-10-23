package hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.adapter.card;

import android.content.Context;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight.UserWeight;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class WeightChartData {
    private static long DAY_TIME = 86400000;
    private long START_TIME_OFFSET = (7 * DAY_TIME);
    private Context mContext;
    private List<WeightChartMonthLine> mMonthLines = new ArrayList();
    private long mStartTime;
    private List<WeightChartRecord> mWeightChartRecords = new ArrayList();
    private int maxDayOffset = 0;
    private int maxWeight = Integer.MIN_VALUE;
    private int minDayOffset = 0;
    private int minWeight = Integer.MAX_VALUE;

    public class WeightChartMonthLine {
        private String name;
        private int value;

        public WeightChartMonthLine(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class WeightChartRecord {
        private int daysFromStart;
        private float weight;

        public WeightChartRecord(float weight, int daysFromStart) {
            this.weight = weight;
            this.daysFromStart = daysFromStart;
        }

        public float getWeight() {
            return this.weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public int getDaysFromStart() {
            return this.daysFromStart;
        }

        public void setDaysFromStart(int daysFromStart) {
            this.daysFromStart = daysFromStart;
        }
    }

    public WeightChartData(Context context) {
        this.mContext = context;
    }

    public void parse(List<UserWeight> weightRecords, int metric) {
        Collections.sort(weightRecords);
        long endTime;
        if (weightRecords.size() != 0) {
            this.mStartTime = ((UserWeight) weightRecords.get(0)).getTime() - this.START_TIME_OFFSET;
            this.minDayOffset = 0;
            endTime = ((UserWeight) weightRecords.get(weightRecords.size() - 1)).getTime() + this.START_TIME_OFFSET;
            if (((UserWeight) weightRecords.get(weightRecords.size() - 1)).getTime() < Calendar.getInstance().getTimeInMillis()) {
                endTime = Calendar.getInstance().getTimeInMillis() + this.START_TIME_OFFSET;
            }
            for (UserWeight wr : weightRecords) {
                if (wr.getWeight(metric) < ((float) this.minWeight)) {
                    this.minWeight = (int) wr.getWeight(metric);
                }
                if (wr.getWeight(metric) > ((float) this.maxWeight)) {
                    this.maxWeight = (int) wr.getWeight(metric);
                }
                this.mWeightChartRecords.add(new WeightChartRecord(wr.getWeight(metric), (int) ((wr.getTime() - this.mStartTime) / DAY_TIME)));
            }
            this.maxDayOffset = (int) ((endTime - this.mStartTime) / DAY_TIME);
            this.mMonthLines = parseMonthLimitLines(this.mStartTime, endTime);
            return;
        }
        this.mStartTime = Calendar.getInstance().getTimeInMillis() - this.START_TIME_OFFSET;
        endTime = Calendar.getInstance().getTimeInMillis() + this.START_TIME_OFFSET;
        this.maxDayOffset = (int) ((endTime - this.mStartTime) / DAY_TIME);
        this.mMonthLines = parseMonthLimitLines(this.mStartTime, endTime);
        this.minWeight = 0;
        this.maxWeight = 0;
    }

    private List<WeightChartMonthLine> parseMonthLimitLines(long start, long end) {
        List<WeightChartMonthLine> mMonthLimits = new ArrayList();
        List<Integer> months = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);
        while (calendar.getTimeInMillis() <= end) {
            int day = calendar.get(5);
            int month = calendar.get(2);
            int year = calendar.get(1);
            if (!months.contains(Integer.valueOf(month + year))) {
                months.add(Integer.valueOf(month + year));
                mMonthLimits.add(new WeightChartMonthLine((int) (((calendar.getTimeInMillis() - (((long) (day - 1)) * DAY_TIME)) - start) / DAY_TIME), getMonthName(month)));
            }
            calendar.add(5, 5);
        }
        return mMonthLimits;
    }

    private String getMonthName(int month) {
        return this.mContext.getResources().getStringArray(R.array.month_names_short)[month];
    }

    public List<WeightChartRecord> getWeightRecords() {
        return this.mWeightChartRecords;
    }

    public void setWeightRecords(List<WeightChartRecord> mWeightRecords) {
        this.mWeightChartRecords = mWeightRecords;
    }

    public List<WeightChartMonthLine> getMonthLines() {
        return this.mMonthLines;
    }

    public void setMonthLines(List<WeightChartMonthLine> mMonthLines) {
        this.mMonthLines = mMonthLines;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public int getMinDayOffset() {
        return this.minDayOffset;
    }

    public void setMinDayOffset(int minDayOffset) {
        this.minDayOffset = minDayOffset;
    }

    public int getMinWeight() {
        return this.minWeight + -10 < 0 ? 0 : this.minWeight - 10;
    }

    public void setMinWeight(int minWeight) {
        this.minWeight = minWeight;
    }

    public int getMaxWeight() {
        return this.maxWeight + 10;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getMaxDayOffset() {
        return this.maxDayOffset;
    }

    public void setMaxDayOffset(int maxDayOffset) {
        this.maxDayOffset = maxDayOffset;
    }
}
