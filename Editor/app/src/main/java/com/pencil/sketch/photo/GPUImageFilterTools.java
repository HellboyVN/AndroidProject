package com.pencil.sketch.photo;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import com.google.android.gms.cast.TextTrackStyle;
import java.util.LinkedList;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;

public class GPUImageFilterTools {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$pencil$sketch$photo$GPUImageFilterTools$FilterType;
    static Bitmap overlayBmp = null;

    static class AnonymousClass1 implements OnClickListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ FilterList val$filters;
        private final /* synthetic */ OnGpuImageFilterChosenListener val$listener;

        AnonymousClass1(OnGpuImageFilterChosenListener onGpuImageFilterChosenListener, Context context, FilterList filterList) {
            this.val$listener = onGpuImageFilterChosenListener;
            this.val$context = context;
            this.val$filters = filterList;
        }

        public void onClick(DialogInterface dialog, int item) {
            this.val$listener.onGpuImageFilterChosenListener(GPUImageFilterTools.createFilterForType(this.val$context, (FilterType) this.val$filters.filters.get(item)));
        }
    }

    private static class FilterList {
        public List<FilterType> filters;
        public List<String> names;

        private FilterList() {
            this.names = new LinkedList();
            this.filters = new LinkedList();
        }

        public void addFilter(String name, FilterType filter) {
            this.names.add(name);
            this.filters.add(filter);
        }
    }

    private enum FilterType {
        POSTERIZE,
        RED,
        GREEN,
        BLUE
    }

    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter gPUImageFilter);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$pencil$sketch$photo$GPUImageFilterTools$FilterType() {
        int[] iArr = $SWITCH_TABLE$com$pencil$sketch$photo$GPUImageFilterTools$FilterType;
        if (iArr == null) {
            iArr = new int[FilterType.values().length];
            try {
                iArr[FilterType.BLUE.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[FilterType.GREEN.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[FilterType.POSTERIZE.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[FilterType.RED.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$pencil$sketch$photo$GPUImageFilterTools$FilterType = iArr;
        }
        return iArr;
    }

    public static void showDialog(Context context, OnGpuImageFilterChosenListener listener) {
        FilterList filters = new FilterList();
        filters.addFilter("Posterize", FilterType.POSTERIZE);
        Builder builder = new Builder(context);
        builder.setTitle("Choose a filter");
        builder.setItems((CharSequence[]) filters.names.toArray(new String[filters.names.size()]), new AnonymousClass1(listener, context, filters));
        builder.create().show();
    }

    private static GPUImageFilter createFilterForType(Context context, FilterType type) {
        switch ($SWITCH_TABLE$com$pencil$sketch$photo$GPUImageFilterTools$FilterType()[type.ordinal()]) {
            case 1:
                return new GPUImagePosterizeFilter();
            case 2:
                return new GPUImageRGBFilter(1.5f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
            case 3:
                return new GPUImageRGBFilter(TextTrackStyle.DEFAULT_FONT_SCALE, 1.5f, TextTrackStyle.DEFAULT_FONT_SCALE);
            case 4:
                return new GPUImageRGBFilter(TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 1.5f);
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    }

    public static void Applyeffects(int i, Context context, OnGpuImageFilterChosenListener ongpuimagefilterchosenlistener) {
        switch (i) {
            case 0:
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.GREEN));
                return;
            case 1:
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.BLUE));
                return;
            case 2:
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.RED));
                return;
            case 3:
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.POSTERIZE));
                return;
            default:
                return;
        }
    }
}
