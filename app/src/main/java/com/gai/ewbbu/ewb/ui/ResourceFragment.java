package com.gai.ewbbu.ewb.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourceFragment extends Fragment {

    private static final String TAG = ResourceFragment.class.getSimpleName();

    private String mProjectTitle;
    private Map<String, String> mFileNameMap = new HashMap<>();
    private Map<String, Integer> mColorMap = new HashMap<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private StorageReference mStorageRef;
    private String mFileType;

    @BindView(R.id.resourceCardView) CardView mResourceCard;
    @BindView(R.id.pdfImage) ImageView mPDFImage;
    @BindView(R.id.fileName) TextView mFileName;
    @BindView(R.id.resourceProgressBar) ProgressBar mProgressBar;
    @BindView(R.id.downloadResourceText) TextView mDownloadResourceText;

    public ResourceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProjectTitle = ProjectsActivity.getProjectTitle();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        externalStoragePermission();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        ButterKnife.bind(this, view);

        // set progress bar color
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(
                getActivity(), R.color.primary), PorterDuff.Mode.SRC_IN);

        // hide progress bar and text
        mProgressBar.setVisibility(View.GONE);
        mDownloadResourceText.setVisibility(View.GONE);

        // set hashmap with (projectName, fileName) pairs
        mFileNameMap.put(getString(R.string.filter_title), getString(R.string.filter_file));
        mFileNameMap.put(getString(R.string.collection_title), getString(R.string.collection_file));
        mFileNameMap.put(getString(R.string.sanitation_title), getString(R.string.sanitation_file));
        mFileNameMap.put(getString(R.string.solar_title), getString(R.string.solar_file));

        // set hashmap with (projectName, color) pairs
        mColorMap.put(getString(R.string.filter_title), ContextCompat.getColor(getActivity() , R.color.filter));
        mColorMap.put(getString(R.string.collection_title), ContextCompat.getColor(getActivity(), R.color.collection));
        mColorMap.put(getString(R.string.sanitation_title), ContextCompat.getColor(getActivity(), R.color.sanitation));
        mColorMap.put(getString(R.string.solar_title), ContextCompat.getColor(getActivity(), R.color.solar));

        setCardView(mFileNameMap.get(mProjectTitle), mColorMap.get(mProjectTitle));

        mResourceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mDownloadResourceText.setVisibility(View.VISIBLE);
                getResourceFromFirebase();
            }
        });

        return view;
    }

    private void setCardView(String fileName, int color) {
        mFileName.setText(fileName);
        mPDFImage.setColorFilter(color);
    }

    private void getResourceFromFirebase() {
        final StorageReference fileRef = mStorageRef.child(mFileNameMap.get(mProjectTitle));

        // get content type of file
        fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                mFileType = storageMetadata.getContentType();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mFileType = Constants.FILE_TYPE_PDF;
            }
        });

        // get file from firebase and save as resourceFile
        final File resourceFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                mFileNameMap.get(mProjectTitle));
        fileRef.getFile(resourceFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                mProgressBar.setVisibility(View.GONE);
                mDownloadResourceText.setVisibility(View.GONE);
                openFile(resourceFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "failed to download resource file");
            }
        });
    }

    // create intent to view resourceFile
    private void openFile(File resourceFile) {
        // target intent for opening pdf file
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(resourceFile), mFileType);
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        // create chooser to open file
        Intent intent = Intent.createChooser(target, Constants.CHOOSER_TITLE);
        startActivity(intent);
    }

    // verify write external storage permission
    private void externalStoragePermission() {
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // ask user for permission
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // permission denied
                    Toast.makeText(getActivity(), "Permission must be granted to download pdf files.", Toast.LENGTH_LONG).show();
                }
        }
    }
}
