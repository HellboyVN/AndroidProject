package hb.giphy.allgif.giffree.model;

/**
 * Created by nxtruong on 7/13/2017.
 */

public class CategoryItem {
    private String link;
    private String tag;

    public CategoryItem(String link, String tag) {
        this.link = link;
        this.tag = tag;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
