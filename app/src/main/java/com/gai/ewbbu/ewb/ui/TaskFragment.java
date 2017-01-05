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
import com.gai.ewbbu.ewb.util.Constants;
import com.gai.ewbbu.ewb.model.Task;
import com.gai.ewbbu.ewb.util.DividerItemDecoration;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mTaskRecyclerView.setLayoutManager(layoutManager);
        mTaskRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        mNewTaskButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mTasks = getParseTasks();
        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), mTasks);
        mTaskRecyclerView.setAdapter(taskAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setEmptyText("There are no tasks for this project");
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
//            intent.putExtra(ParseConstants.TASK_ID, mTasks[position].getTaskId());
//            startActivity(intent);
//        }
//    }

    // return list of tasks from parse
    private Task[] getParseTasks() {
        ParseQuery<ParseObject> query;
        switch (mFilter) {
            case Constants.ALL_TASKS:
                query = ParseQuery.getQuery(Constants.TASK_CLASS)
                        .whereEqualTo(Constants.TASK_PROJECT_ID, mFirebaseProjectKey);
                break;
            case Constants.COMPLETE_TASKS:
                query = ParseQuery.getQuery(Constants.TASK_CLASS)
                        .whereEqualTo(Constants.TASK_PROJECT_ID, mFirebaseProjectKey)
                        .whereEqualTo(Constants.TASK_COMPLETE, true);
                break;
            case Constants.INCOMPLETE_TASKS:
                query = ParseQuery.getQuery(Constants.TASK_CLASS)
                        .whereEqualTo(Constants.TASK_PROJECT_ID, mFirebaseProjectKey)
                        .whereEqualTo(Constants.TASK_COMPLETE, false);
                break;
            default:
                query = ParseQuery.getQuery(Constants.TASK_CLASS)
                        .whereEqualTo(Constants.TASK_PROJECT_ID, mFirebaseProjectKey);
        }

        Task[] tasks = null;
        try {
            List<ParseObject> list = query.find();
            tasks = new Task[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Task task = new Task();
                task.setTitle(list.get(i).getString(Constants.TASK_TITLE));
                task.setDescription(list.get(i).getString(Constants.TASK_DESCRIPTION));
                task.setTaskId(list.get(i).getObjectId());
                task.setFirebaseProjectKey(list.get(i).getString(Constants.TASK_PROJECT_ID));
                task.setComplete(list.get(i).getBoolean(Constants.TASK_COMPLETE));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(list.get(i).getDate(Constants.TASK_DUE_DATE));
                task.setDueDate(calendar);
                tasks[i] = task;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tasks;
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
                        mTasks = getParseTasks();
                        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), mTasks);
                        mTaskRecyclerView.setAdapter(taskAdapter);
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
