package hb.me.instagramsave.Adapters;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.geniusforapp.fancydialog.FancyAlertDialog.Builder;
import com.geniusforapp.fancydialog.FancyAlertDialog.OnNegativeClicked;
import com.geniusforapp.fancydialog.FancyAlertDialog.OnPositiveClicked;

import java.io.File;
import java.util.ArrayList;

import hb.me.instagramsave.Model.Files;
import hb.me.instagramsave.R;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Files> filesList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        ImageView deleteID;
        ImageView playIcon;
        ImageView repostID;
        ImageView savedImage;
        ImageView shareID;
        TextView userName;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.userName = (TextView) itemView.findViewById(R.id.profileUserName);
            this.savedImage = (ImageView) itemView.findViewById(R.id.mainImageView);
            this.playIcon = (ImageView) itemView.findViewById(R.id.playButtonImage);
            this.repostID = (ImageView) itemView.findViewById(R.id.repostID);
            this.shareID = (ImageView) itemView.findViewById(R.id.shareID);
            this.deleteID = (ImageView) itemView.findViewById(R.id.deleteID);
        }
    }

    public RecyclerViewAdapter(Context context, ArrayList<Files> filesList) {
        this.context = context;
        this.filesList = filesList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, null, false), this.context);
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Files files = (Files) this.filesList.get(position);
        final File file = new File(Uri.parse(files.getUri().toString()).getPath());
        holder.userName.setText(files.getName());
        if (files.getUri().toString().endsWith(".mp4")) {
            holder.playIcon.setVisibility(View.VISIBLE);
        } else {
            holder.playIcon.setVisibility(View.INVISIBLE);
        }
        Glide.with(this.context).load(files.getUri()).into(holder.savedImage);
        holder.savedImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Uri VideoURI;
                Intent intent;
                if (files.getUri().toString().endsWith(".mp4")) {
                    VideoURI = FileProvider.getUriForFile(RecyclerViewAdapter.this.context, RecyclerViewAdapter.this.context.getApplicationContext().getPackageName() + ".provider", file);
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addFlags(1);
                    intent.addFlags(2);
                    intent.setDataAndType(VideoURI, "video/*");
                    try {
                        RecyclerViewAdapter.this.context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(RecyclerViewAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                } else if (files.getUri().toString().endsWith(".jpg")) {
                    VideoURI = FileProvider.getUriForFile(RecyclerViewAdapter.this.context, RecyclerViewAdapter.this.context.getApplicationContext().getPackageName() + ".provider", file);
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addFlags(1);
                    intent.addFlags(2);
                    intent.setDataAndType(VideoURI, "image/*");
                    try {
                        RecyclerViewAdapter.this.context.startActivity(intent);
                    } catch (ActivityNotFoundException e2) {
                        Toast.makeText(RecyclerViewAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        holder.repostID.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Uri mainUri = Uri.fromFile(file);
                Intent sharingIntent;
                if (files.getUri().toString().endsWith(".jpg")) {
                    sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra("android.intent.extra.STREAM", mainUri);
                    sharingIntent.addFlags(1);
                    sharingIntent.setPackage("com.instagram.android");
                    try {
                        RecyclerViewAdapter.this.context.startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(RecyclerViewAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                } else if (files.getUri().toString().endsWith(".mp4")) {
                    sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("video/*");
                    sharingIntent.putExtra("android.intent.extra.STREAM", mainUri);
                    sharingIntent.addFlags(1);
                    sharingIntent.setPackage("com.instagram.android");
                    try {
                        RecyclerViewAdapter.this.context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
                    } catch (ActivityNotFoundException e2) {
                        Toast.makeText(RecyclerViewAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        holder.deleteID.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final String path = ((Files) RecyclerViewAdapter.this.filesList.get(position)).getFilename();
                final File file = new File(path);
                new Builder(RecyclerViewAdapter.this.context).setTextTitle("DELETE?").setBody("Are you sure you want to delete this file?").setNegativeColor(R.color.colorNegative).setNegativeButtonText("Cancel").setOnNegativeClicked(new OnNegativeClicked() {
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                }).setPositiveButtonText("Delete").setPositiveColor(R.color.colorPositive).setOnPositiveClicked(new OnPositiveClicked() {
                    public void OnClick(View view, Dialog dialog) {
                        try {
                            if (file.exists()) {
                                boolean del = file.delete();
                                RecyclerViewAdapter.this.filesList.remove(position);
                                RecyclerViewAdapter.this.notifyItemRemoved(position);
                                RecyclerViewAdapter.this.notifyItemRangeChanged(position, RecyclerViewAdapter.this.filesList.size());
                                RecyclerViewAdapter.this.notifyDataSetChanged();
                                Toast.makeText(RecyclerViewAdapter.this.context, "File was Deleted", Toast.LENGTH_SHORT).show();
                                if (del) {
                                    MediaScannerConnection.scanFile(RecyclerViewAdapter.this.context, new String[]{path, path}, new String[]{"image/jpg", "video/mp4"}, new MediaScannerConnectionClient() {
                                        public void onMediaScannerConnected() {
                                        }

                                        public void onScanCompleted(String path, Uri uri) {
                                            Log.d("Video path: ", path);
                                        }
                                    });
                                }
                            }
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).build().show();
            }
        });
        holder.shareID.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Uri mainUri = Uri.fromFile(file);
                Intent sharingIntent;
                if (files.getUri().toString().endsWith(".jpg")) {
                    sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra("android.intent.extra.STREAM", mainUri);
                    sharingIntent.addFlags(1);
                    try {
                        RecyclerViewAdapter.this.context.startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(RecyclerViewAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                } else if (files.getUri().toString().endsWith(".mp4")) {
                    sharingIntent = new Intent("android.intent.action.SEND");
                    sharingIntent.setType("video/*");
                    sharingIntent.putExtra("android.intent.extra.STREAM", mainUri);
                    sharingIntent.addFlags(1);
                    try {
                        RecyclerViewAdapter.this.context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
                    } catch (ActivityNotFoundException e2) {
                        Toast.makeText(RecyclerViewAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public int getItemCount() {
        return this.filesList.size();
    }
}
