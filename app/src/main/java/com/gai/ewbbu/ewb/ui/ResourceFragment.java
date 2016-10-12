package com.gai.ewbbu.ewb.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
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

    @BindView(R.id.resourceCardView) CardView mResourceCard;
    @BindView(R.id.pdfImage) ImageView mPDFImage;
    @BindView(R.id.fileName) TextView mFileName;

    public ResourceFragment() {
        // Required empty public constructor
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
}
