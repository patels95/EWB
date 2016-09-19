package com.patels95.sanam.ewb.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.adapters.ProjectAdapter;
import com.patels95.sanam.ewb.model.ParseConstants;
import com.patels95.sanam.ewb.model.Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = ProjectsFragment.class.getSimpleName();
    private int mSectionNumber;

    @BindView(R.id.projectRecyclerView) RecyclerView mProjectRecyclerView;

    private OnFragmentInteractionListener mListener;
    private Project[] mProjectCards;

    public static ProjectsFragment newInstance(int sectionNumber) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mProjectRecyclerView.setLayoutManager(layoutManager);

        getParseProjects();

        return view;
    }


    private void setProjectsArray(ArrayList<String> projectStrings) {

        Project filter = new Project();
        filter.setTitle(projectStrings.get(0));
        filter.setDescription(projectStrings.get(1));
        filter.setImageUri(projectStrings.get(2));
        filter.setParseId(projectStrings.get(3));

        Project collection = new Project();
        collection.setTitle(projectStrings.get(4));
        collection.setDescription(projectStrings.get(5));
        collection.setImageUri(projectStrings.get(6));
        collection.setParseId(projectStrings.get(7));

        Project sanitation = new Project();
        sanitation.setTitle(projectStrings.get(8));
        sanitation.setDescription(projectStrings.get(9));
        sanitation.setImageUri(projectStrings.get(10));
        sanitation.setParseId(projectStrings.get(11));

        Project solar = new Project();
        solar.setTitle(projectStrings.get(12));
        solar.setDescription(projectStrings.get(13));
        solar.setImageUri(projectStrings.get(14));
        solar.setParseId(projectStrings.get(15));

        mProjectCards = new Project[]{filter, collection, sanitation, solar};
        ProjectAdapter adapter = new ProjectAdapter(getActivity(), mProjectCards);
        mProjectRecyclerView.setAdapter(adapter);
    }

    private void updateParseProjects() throws IOException {

        // get file from assets folder and convert to byte array
//        AssetManager am = getResources().getAssets();
//        InputStream inputStream = am.open("SPSpring2016.pdf");
//        byte[] data = IOUtils.toByteArray(inputStream);

//        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.PROJECT_CLASS);

        // Biosand Filter
//        query.getInBackground(ParseConstants.FILTER_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject filter, ParseException e) {
//                if (e == null) {
//                    filter.saveInBackground();
//                }
//            }
//        });

        // Water Transportation & Collection
//        query.getInBackground(ParseConstants.COLLECTION_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject collection, ParseException e) {
//                if (e == null) {
//                    collection.saveInBackground();
//                }
//            }
//        });

        // Sanitation Systems
//        query.getInBackground(ParseConstants.SANITATION_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject sanitation, ParseException e) {
//                if (e == null) {
//                    sanitation.saveInBackground();
//                }
//            }
//        });

        // Solar Pump
//        query.getInBackground(ParseConstants.SOLAR_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject solar, ParseException e) {
//                if (e == null) {
//                    solar.saveInBackground();
//                }
//            }
//        });
    }

    private File createFileFromInputStream(InputStream inputStream) {
        try {
            File file = new File("BSFSpring2016.pdf");
            OutputStream outputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getParseProjects() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.PROJECT_CLASS);
        try {
            List<ParseObject> list = query.find();
            ArrayList<String> projectStrings = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++){
                projectStrings.add(list.get(i).getString(ParseConstants.PROJECT_TITLE));
                projectStrings.add(list.get(i).getString(ParseConstants.PROJECT_DESCRIPTION));
                projectStrings.add(list.get(i).getString(ParseConstants.PROJECT_IMAGEURI));
                projectStrings.add(list.get(i).getObjectId());
            }
            setProjectsArray(projectStrings);
        }
        catch (ParseException e) {
            e.printStackTrace();
            setProjectsArrayLocal();
        }
    }

    private void setProjectsArrayLocal() {
        Project filter = new Project();
        filter.setTitle(getString(R.string.filter_title));
        filter.setDescription(getString(R.string.filter_description));
        filter.setImageUri(getString(R.string.filter_image_uri));

        Project collection = new Project();
        collection.setTitle(getString(R.string.collection_title));
        collection.setDescription(getString(R.string.collection_description));
        collection.setImageUri(getString(R.string.collection_image_uri));

        Project sanitation = new Project();
        sanitation.setTitle(getString(R.string.sanitation_title));
        sanitation.setDescription(getString(R.string.sanitation_description));
        sanitation.setImageUri(getString(R.string.sanitation_image_uri));

        Project solar = new Project();
        solar.setTitle(getString(R.string.solar_title));
        solar.setDescription(getString(R.string.solar_description));
        solar.setImageUri(getString(R.string.solar_image_uri));

        mProjectCards = new Project[]{filter, collection, sanitation, solar};
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
