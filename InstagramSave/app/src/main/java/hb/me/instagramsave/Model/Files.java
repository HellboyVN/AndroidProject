package hb.me.instagramsave.Model;

import android.net.Uri;

public class Files {
    private String filename;
    private String name;
    private Uri uri;

    public Files() {
        this.uri = uri;
        this.name = name;
        this.filename = filename;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
