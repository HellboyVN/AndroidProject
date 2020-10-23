package com.workout.workout.modal;

import java.util.List;

public class PlanCategory {
    private String plan_category_id;
    private String plan_category_name;
    private List<Plan> plansList;
    public PlanCategory(){

    }
    public PlanCategory(String plan_category_id, String plan_category_name, List<Plan> plansList) {
        this.plan_category_id = plan_category_id;
        this.plan_category_name = plan_category_name;
        this.plansList = plansList;
    }

    public String getPlan_category_id() {
        return this.plan_category_id;
    }

    public void setPlan_category_id(String plan_category_id) {
        this.plan_category_id = plan_category_id;
    }

    public String getPlan_category_name() {
        return this.plan_category_name;
    }

    public void setPlan_category_name(String plan_category_name) {
        this.plan_category_name = plan_category_name;
    }

    public List<Plan> getPlansList() {
        return this.plansList;
    }

    public void setPlansList(List<Plan> plansList) {
        this.plansList = plansList;
    }
}
