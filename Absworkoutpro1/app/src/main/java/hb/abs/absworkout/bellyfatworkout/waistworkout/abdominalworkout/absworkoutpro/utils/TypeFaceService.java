package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils;

import android.content.Context;
import android.graphics.Typeface;
import java.util.HashMap;
import java.util.Map;

public class TypeFaceService {
    private static TypeFaceService INSTANCE = null;
    private static final String TYPEFACE_EXTENSION = ".ttf";
    private static final String TYPEFACE_FOLDER = "fonts";
    private Map<String, Typeface> typeFaces = new HashMap(30);

    public static TypeFaceService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TypeFaceService();
        }
        return INSTANCE;
    }

    private TypeFaceService() {
    }

    public Typeface getTypeFace(Context context, String fileName) {
        Typeface typeface = (Typeface) this.typeFaces.get(fileName);
        if (typeface != null) {
            return typeface;
        }
        typeface = Typeface.createFromAsset(context.getAssets(), new StringBuilder(TYPEFACE_FOLDER).append('/').append(fileName).append(TYPEFACE_EXTENSION).toString());
        this.typeFaces.put(fileName, typeface);
        return typeface;
    }

    public Typeface getRobotoThin(Context context) {
        return getTypeFace(context, "Roboto-Thin");
    }

    public Typeface getRobotoLight(Context context) {
        return getTypeFace(context, "Roboto-Light");
    }

    public Typeface getRobotoRegular(Context context) {
        return getTypeFace(context, "Roboto-Regular");
    }

    public Typeface getDensRegular(Context context) {
        return getTypeFace(context, "Dense-Regular");
    }

    public Typeface getRobotoMedium(Context context) {
        return getTypeFace(context, "Roboto-Medium");
    }

    public Typeface getRobotoBold(Context context) {
        return getTypeFace(context, "Roboto-Bold");
    }
}
