package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabTwo implements Serializable {
    private List<TabTwo> data = new ArrayList();

    public List<TabTwo> getData() {
        return this.data;
    }

    public void setData(List<TabTwo> data) {
        this.data = data;
    }
}
