package hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal;

import java.util.List;

public class BodyPartList {
    List<BodyPart> bodyPartList;

    public BodyPartList(List<BodyPart> bodyPartList) {
        this.bodyPartList = bodyPartList;
    }

    public List<BodyPart> getBodyPartList() {
        return this.bodyPartList;
    }

    public void setBodyPartList(List<BodyPart> bodyPartList) {
        this.bodyPartList = bodyPartList;
    }
}
