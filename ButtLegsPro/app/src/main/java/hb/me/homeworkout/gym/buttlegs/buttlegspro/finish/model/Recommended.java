package hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.model;

import java.io.Serializable;

public class Recommended implements Serializable {
    private String appDecs;
    private String appName;
    private String appPackage;
    private String imgKey;

    public String getAppPackage() {
        return this.appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getImgKey() {
        return this.imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getAppDecs() {
        return this.appDecs;
    }

    public void setAppDecs(String appDecs) {
        this.appDecs = appDecs;
    }
}
