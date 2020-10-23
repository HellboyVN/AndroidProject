package com.photo.sketch.gallery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.aerotools.photo.sketch.maker.editor.R;

import java.io.File;
import java.util.ArrayList;

public class ImageFragment extends Fragment {
    private OnImageSelectedListener mCallback;
    private ArrayList<MediaModel> mGalleryModelList;
    private GridViewAdapter mImageAdapter;
    private Cursor mImageCursor;
    private GridView mImageGridView;
    private ArrayList<String> mSelectedItems = new ArrayList();
    private View mView;

    public interface OnImageSelectedListener {
        void onImageSelected(int i);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mCallback = (OnImageSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnImageSelectedListener");
        }
    }

    public ImageFragment() {
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.mView == null) {
            this.mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);
            this.mImageGridView = (GridView) this.mView.findViewById(R.id.gridViewFromMediaChooser);
            if (getArguments() != null) {
                initPhoneImages(getArguments().getString("name"));
            } else {
                initPhoneImages();
            }
        } else {
            ((ViewGroup) this.mView.getParent()).removeView(this.mView);
            if (this.mImageAdapter == null || this.mImageAdapter.getCount() == 0) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), 0).show();
            }
        }
        return this.mView;
    }

    private void initPhoneImages(String bucketName) {
        try {
            String orderBy = "datetaken";
            String[] columns = new String[]{"_data", "_id"};
            this.mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, "bucket_display_name = \"" + bucketName + "\"", null, "datetaken DESC");
            setAdapter(this.mImageCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPhoneImages() {
        try {
            String orderBy = "datetaken";
            this.mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, null, null, "datetaken DESC");
            setAdapter(this.mImageCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(Cursor imagecursor) {
        if (imagecursor.getCount() > 0) {
            this.mGalleryModelList = new ArrayList();
            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                this.mGalleryModelList.add(new MediaModel(imagecursor.getString(imagecursor.getColumnIndex("_data")).toString(), false));
            }
            this.mImageAdapter = new GridViewAdapter(getActivity(), 0, this.mGalleryModelList, false);
            this.mImageGridView.setAdapter(this.mImageAdapter);
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), 0).show();
        }
        this.mImageGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(((GridViewAdapter) parent.getAdapter()).getItem(position).url);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                ImageFragment.this.startActivity(intent);
                return true;
            }
        });
        this.mImageGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = adapter.getItem(position);
                if (!galleryModel.status) {
                    if (MediaChooserConstants.ChekcMediaFileSize(new File(galleryModel.url.toString()), false) != 0) {
                        Toast.makeText(ImageFragment.this.getActivity(), new StringBuilder(String.valueOf(ImageFragment.this.getActivity().getResources().getString(R.string.file_size_exeeded))).append("  ").append(MediaChooserConstants.SELECTED_IMAGE_SIZE_IN_MB).append(" ").append(ImageFragment.this.getActivity().getResources().getString(R.string.mb)).toString(), 0).show();
                        return;
                    } else if (MediaChooserConstants.MAX_MEDIA_LIMIT == MediaChooserConstants.SELECTED_MEDIA_COUNT) {
                        if (MediaChooserConstants.SELECTED_MEDIA_COUNT < 2) {
                            Toast.makeText(ImageFragment.this.getActivity(), new StringBuilder(String.valueOf(ImageFragment.this.getActivity().getResources().getString(R.string.max_limit_file))).append("  ").append(MediaChooserConstants.SELECTED_MEDIA_COUNT).append(" ").append(ImageFragment.this.getActivity().getResources().getString(R.string.file)).toString(), 0).show();
                            return;
                        } else {
                            Toast.makeText(ImageFragment.this.getActivity(), new StringBuilder(String.valueOf(ImageFragment.this.getActivity().getResources().getString(R.string.max_limit_file))).append("  ").append(MediaChooserConstants.SELECTED_MEDIA_COUNT).append(" ").append(ImageFragment.this.getActivity().getResources().getString(R.string.files)).toString(), 0).show();
                            return;
                        }
                    }
                }
                galleryModel.status = !galleryModel.status;
                adapter.notifyDataSetChanged();
                if (galleryModel.status) {
                    ImageFragment.this.mSelectedItems.add(galleryModel.url.toString());
                    MediaChooserConstants.SELECTED_MEDIA_COUNT++;
                    Intent imageIntent = new Intent();
                    imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
                    imageIntent.putStringArrayListExtra("list", ImageFragment.this.mSelectedItems);
                    ImageFragment.this.getActivity().sendBroadcast(imageIntent);
                    ImageFragment.this.getActivity().finish();
                } else {
                    ImageFragment.this.mSelectedItems.remove(galleryModel.url.toString().trim());
                    MediaChooserConstants.SELECTED_MEDIA_COUNT--;
                }
                if (ImageFragment.this.mCallback != null) {
                    ImageFragment.this.mCallback.onImageSelected(ImageFragment.this.mSelectedItems.size());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("list", ImageFragment.this.mSelectedItems);
                    ImageFragment.this.getActivity().setResult(-1, intent);
                }
            }
        });
    }

    public ArrayList<String> getSelectedImageList() {
        return this.mSelectedItems;
    }

    public void addItem(String item) {
        if (this.mImageAdapter != null) {
            this.mGalleryModelList.add(0, new MediaModel(item, false));
            this.mImageAdapter.notifyDataSetChanged();
            return;
        }
        initPhoneImages();
    }
}
