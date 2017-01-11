package com.gai.ewbbu.ewb.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.gai.ewbbu.ewb.R;

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

    @BindView(R.id.resourceCardView) CardView mResourceCard;
    @BindView(R.id.pdfImage) ImageView mPDFImage;
    @BindView(R.id.fileName) TextView mFileName;

    public ResourceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        externalStoragePermission();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        ButterKnife.bind(this, view);

        // Inflate the layout for this fragment
        mProjectTitle = ProjectsActivity.getProjectTitle();

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
                copyAsset(mFileNameMap.get(mProjectTitle));
                Toast.makeText(getActivity(), "File Download Complete", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void setCardView(String fileName, int color) {
        mFileName.setText(fileName);
        mPDFImage.setColorFilter(color);
    }

    private void copyAsset(String filename) {
        AssetManager am = getResources().getAssets();
        try {
            InputStream inputStream = am.open(filename);
            File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
            OutputStream outputStream = new FileOutputStream(outFile);
            copyFile(inputStream, outputStream);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            Log.d(TAG, "write file complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
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
