package hb.me.giphy.etc;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader.Factory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class MyGlideModule implements GlideModule {
    public void applyOptions(Context context, GlideBuilder builder) {
    }

    public void registerComponents(Context context, Glide glide) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);
        glide.register(GlideUrl.class, InputStream.class, new Factory(client));
    }
}
