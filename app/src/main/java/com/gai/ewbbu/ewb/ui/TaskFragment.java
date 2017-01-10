package com.gai.ewbbu.ewb.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.adapters.TaskAdapter;
import com.gai.ewbbu.ewb.model.Task;
import com.gai.ewbbu.ewb.util.Constants;
import com.gai.ewbbu.ewb.util.DividerItemDecoration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TaskFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = TaskFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private String mFirebaseProjectKey;
    private String mProjectTitle;
    private Task[] mTasks;
    private String mFilter = Constants.ALL_TASKS;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.taskRecyclerView) RecyclerView mTaskRecyclerView;
    @BindView(R.id.newTaskButton) FloatingActionButton mNewTaskButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mFirebaseProjectKey = ProjectsActivity.getFirebaseKey();
        mProjectTitle = ProjectsActivity.getProjectTitle();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mTaskRecyclerView.setLayoutManager(layoutManager);
        mTaskRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        if (mFirebaseAuth.getCurrentUser() == null) {
            // hide new task button for members
            mNewTaskButton.setVisibility(View.GONE);
        }
        else {
            // admins can add new task
            mNewTaskButton.setOnClickListener(this);
        }



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getTasksFromFirebase();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setEmptyText("There are no tasks for this project");
    }

    // change activity parameter to context
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filters:
                showFilterAlertDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTaskButton:
                // intent to NewTaskActivity
                Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                intent.putExtra(Constants.FIREBASE_KEY, mFirebaseProjectKey);
                startActivity(intent);
                break;
        }
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        //super.onListItemClick(l, v, position, id);
//
//        if (null != mListener) {
//            // Notify the active callbacks interface (the activity, if the
//            // fragment is attached to one) that an item has been selected.
////            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
//            Intent intent = new Intent(getActivity(), TaskActivity.class);
//            intent.putExtra(ParseConstants.PROJECT_TITLE, mProjectTitle);
//            intent.putExtra(ParseConstants.PARSE_ID, mFirebaseProjectKey);
//            intent.putExtra(ParseConstants.TASK_ID, mTasks[position].getFirebaseKey());
//            startActivity(intent);
//        }
//    }

    // get task list from firebase database
    private void getTasksFromFirebase() {
        DatabaseReference firebaseTasks = mDatabase.child(Constants.FIREBASE_TASKS_KEY).child(mFirebaseProjectKey);
        firebaseTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task[] tasks = new Task[(int) dataSnapshot.getChildrenCount()];
                int index = 0;
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    tasks[index] = taskSnapshot.getValue(Task.class);
                    tasks[index].setFirebaseKey(taskSnapshot.getKey());
                    index++;
                }
                TaskAdapter adapter = new TaskAdapter(getActivity(), tasks, mProjectTitle);
                mTaskRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showFilterAlertDialog() {
        final String[] filters = {"All Tasks", "Complete Tasks", "Incomplete Tasks"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(filters, 0, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        mFilter = filters[position];
                        // TODO: refresh task list
//                        mTasks = getParseTasks();
//                        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), mTasks);
//                        mTaskRecyclerView.setAdapter(taskAdapter);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

}
