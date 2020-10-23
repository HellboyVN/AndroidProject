package hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal;

import java.util.List;

public class BodyPart {
    String image_url;
    String part_image_name;
    String part_name;
    List<Workout> workoutList;

    public BodyPart(List<Workout> workoutList) {
        this.workoutList = workoutList;
    }

    public List<Workout> getWorkoutList() {
        return this.workoutList;
    }

    public void setWorkoutList(List<Workout> workoutList) {
        this.workoutList = workoutList;
    }

    public String getPart_name() {
        return this.part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getPart_image_name() {
        return this.part_image_name;
    }

    public void setPart_image_name(String part_image_name) {
        this.part_image_name = part_image_name;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
