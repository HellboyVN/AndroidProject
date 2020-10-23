package hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal;

import java.util.ArrayList;

public class GroupInfo {
    private ArrayList<ChildInfo> list = new ArrayList();
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChildInfo> getProductList() {
        return this.list;
    }

    public void setProductList(ArrayList<ChildInfo> productList) {
        this.list = productList;
    }
}
