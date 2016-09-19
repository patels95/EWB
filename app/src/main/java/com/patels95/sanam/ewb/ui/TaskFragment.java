package com.patels95.sanam.ewb.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.adapters.TaskAdapter;
import com.patels95.sanam.ewb.model.ParseConstants;
import com.patels95.sanam.ewb.model.Task;

import java.util.Calendar;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TaskFragment extends ListFragment {

    private static final String TAG = TaskFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private String mParseProjectId;
    private String mProjectTitle;
    private Task[] mTasks;
    private String mFilter = ParseConstants.ALL_TASKS;

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

        mParseProjectId = ProjectsActivity.getParseProjectId();
        mProjectTitle = ProjectsActivity.getProjectTitle();
    }

    @Override
    public void onStart() {
        super.onStart();

        mTasks = getParseTasks();
        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), mTasks);
        setListAdapter(taskAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText("There are no tasks for this project");
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            intent.putExtra(ParseConstants.PROJECT_TITLE, mProjectTitle);
            intent.putExtra(ParseConstants.PARSE_ID, mParseProjectId);
            intent.putExtra(ParseConstants.TASK_ID, mTasks[position].getTaskId());
            startActivity(intent);
        }
    }

    // return list of tasks from parse
    private Task[] getParseTasks() {
        ParseQuery<ParseObject> query;
        switch (mFilter) {
            case ParseConstants.ALL_TASKS:
                query = ParseQuery.getQuery(ParseConstants.TASK_CLASS)
                        .whereEqualTo(ParseConstants.TASK_PROJECT_ID, mParseProjectId);
                break;
            case ParseConstants.COMPLETE_TASKS:
                query = ParseQuery.getQuery(ParseConstants.TASK_CLASS)
                        .whereEqualTo(ParseConstants.TASK_PROJECT_ID, mParseProjectId)
                        .whereEqualTo(ParseConstants.TASK_COMPLETE, true);
                break;
            case ParseConstants.INCOMPLETE_TASKS:
                query = ParseQuery.getQuery(ParseConstants.TASK_CLASS)
                        .whereEqualTo(ParseConstants.TASK_PROJECT_ID, mParseProjectId)
                        .whereEqualTo(ParseConstants.TASK_COMPLETE, false);
                break;
            default:
                query = ParseQuery.getQuery(ParseConstants.TASK_CLASS)
                        .whereEqualTo(ParseConstants.TASK_PROJECT_ID, mParseProjectId);
        }

        Task[] tasks = null;
        try {
            List<ParseObject> list = query.find();
            tasks = new Task[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Task task = new Task();
                task.setTitle(list.get(i).getString(ParseConstants.TASK_TITLE));
                task.setDescription(list.get(i).getString(ParseConstants.TASK_DESCRIPTION));
                task.setTaskId(list.get(i).getObjectId());
                task.setProjectId(list.get(i).getString(ParseConstants.TASK_PROJECT_ID));
                task.setComplete(list.get(i).getBoolean(ParseConstants.TASK_COMPLETE));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(list.get(i).getDate(ParseConstants.TASK_DUE_DATE));
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
                        setListAdapter(taskAdapter);
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
