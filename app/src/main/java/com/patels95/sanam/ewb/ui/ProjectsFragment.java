package com.patels95.sanam.ewb.ui;

import android.app.Activity;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.adapters.ProjectAdapter;
import com.patels95.sanam.ewb.model.ParseConstants;
import com.patels95.sanam.ewb.model.Project;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @InjectView(R.id.projectRecyclerView) RecyclerView mProjectRecyclerView;


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
        getParseProjects();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        ButterKnife.inject(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mProjectRecyclerView.setLayoutManager(layoutManager);
        ProjectAdapter adapter = new ProjectAdapter(getActivity(), mProjectCards);
        mProjectRecyclerView.setAdapter(adapter);

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
    }

    private void updateParseProjects() {
        //ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.PROJECT_CLASS);


        // Biosand Filter
//        query.getInBackground(ParseConstants.FILTER_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject filter, ParseException e) {
//                if (e == null) {
//                    filter.put(ParseConstants.PROJECT_TITLE, getString(R.string.filter_title));
//                    filter.put(ParseConstants.PROJECT_DESCRIPTION, getString(R.string.filter_description));
//                    filter.saveInBackground();
//                }
//            }
//        });

        // Water Transportation & Collection
//        query.getInBackground(ParseConstants.COLLECTION_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject collection, ParseException e) {
//                if (e == null) {
//                    collection.put(ParseConstants.PROJECT_TITLE, getString(R.string.collection_title));
//                    collection.put(ParseConstants.PROJECT_DESCRIPTION, getString(R.string.collection_description));
//                    collection.saveInBackground();
//                }
//            }
//        });

//        // Sanitation Systems
//        query.getInBackground(ParseConstants.SANITATION_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject sanitation, ParseException e) {
//                if (e == null) {
//                    sanitation.put(ParseConstants.PROJECT_TITLE, getString(R.string.sanitation_title));
//                    sanitation.put(ParseConstants.PROJECT_DESCRIPTION, getString(R.string.sanitation_description));
//                    sanitation.saveInBackground();
//                }
//            }
//        });
//
//        // Solar Pump
//        query.getInBackground(ParseConstants.SOLAR_ID, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject solar, ParseException e) {
//                if (e == null) {
//                    solar.put(ParseConstants.PROJECT_TITLE, getString(R.string.solar_title));
//                    solar.put(ParseConstants.PROJECT_DESCRIPTION, getString(R.string.solar_description));
//                    solar.saveInBackground();
//                }
//            }
//        });
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
