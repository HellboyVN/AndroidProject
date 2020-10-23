package com.tlvanelearning.ielts.common;

public class AnswerItem {
    private boolean isChecked;
    private int qnumber;
    private final String title;
    private String value;

    public AnswerItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public AnswerItem(int qnumber, String title, String value) {
        this.qnumber = qnumber;
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return this.title;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public int getQnumber() {
        return this.qnumber;
    }

    public boolean compareValue(boolean quiz) {
        return quiz ? this.title.equals(this.value) : this.value.equals("1");
    }
}
