package com.tlvanelearning.ielts.common;

public class EntryItem implements Item {
    public final String subtitle;
    public final String title;

    public EntryItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public boolean isSection() {
        return false;
    }
}
