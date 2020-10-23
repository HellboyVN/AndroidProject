package com.tlvanelearning.ielts.common;

public class SectionItem implements Item {
    private final String title;

    public SectionItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isSection() {
        return true;
    }
}
