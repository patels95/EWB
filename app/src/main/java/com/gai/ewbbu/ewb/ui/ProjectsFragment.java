package com.gai.ewbbu.ewb.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.adapters.ProjectAdapter;
import com.gai.ewbbu.ewb.util.Constants;
import com.gai.ewbbu.ewb.model.Project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private OnFragmentInteractionListener mListener;
    private Project[] mProjectCards;
    private DatabaseReference mDatabase;

    @BindView(R.id.projectRecyclerView) RecyclerView mProjectRecyclerView;

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

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mProjectRecyclerView.setLayoutManager(layoutManager);

        //getParseProjects();
        getProjectsFromFirebase();
        return view;
    }

    // get project list from firebase database
    private void getProjectsFromFirebase() {

        DatabaseReference firebaseProjects = mDatabase.child(Constants.PROJECTS_KEY);
        firebaseProjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Project[] projects = new Project[4];
                int index = 0;
                for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                    projects[index] = projectSnapshot.getValue(Project.class);
                    projects[index].setFirebaseKey(projectSnapshot.getKey());
                    index++;
                }
                ProjectAdapter adapter = new ProjectAdapter(getActivity(), projects);
                mProjectRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "get projects:onCancelled", databaseError.toException());
            }
        });
    }

    // add project info to firebase database
    private void setFirebaseProjects() {
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

        mDatabase.child(Constants.PROJECTS_KEY).push().setValue(filter);
        mDatabase.child(Constants.PROJECTS_KEY).push().setValue(collection);
        mDatabase.child(Constants.PROJECTS_KEY).push().setValue(sanitation);
        mDatabase.child(Constants.PROJECTS_KEY).push().setValue(solar);
    }

    private void updateParseProjects() throws IOException {

        // get file from assets folder and convert to byte array
//        AssetManager am = getResources().getAssets();
//        InputStream inputStream = am.open("SPSpring2016.pdf");
//        byte[] data = IOUtils.toByteArray(inputStream);

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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
