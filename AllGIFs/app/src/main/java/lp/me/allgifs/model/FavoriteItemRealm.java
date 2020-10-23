package lp.me.allgifs.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by nxtruong on 7/7/2017.
 */
@RealmClass
public class FavoriteItemRealm extends RealmObject implements Serializable {
    //1
    private String title;
    //2
    private String gifPreview;
    //3
    @PrimaryKey
    private String gifLink;
    //4
    private String mpfourLink;



    public FavoriteItemRealm() {

    }

    public FavoriteItemRealm(String gifLink, String mpfourLink, String title, String gifPreview) {
        this.gifLink = gifLink;
        this.mpfourLink = mpfourLink;
        this.title = title;
        this.gifPreview = gifPreview;
    }

    public String getGifLink() {
        return gifLink;
    }

    public void setGifLink(String gifLink) {
        this.gifLink = gifLink;
    }

    public String getMpfourLink() {
        return mpfourLink;
    }

    public void setMpfourLink(String mpfourLink) {
        this.mpfourLink = mpfourLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGifPreview() {
        return gifPreview;
    }

    public void setGifPreview(String gifPreview) {
        this.gifPreview = gifPreview;
    }

    public FavoriteItemRealm clone() {
        return new FavoriteItemRealm(gifLink, mpfourLink, title, gifPreview);
    }
}
