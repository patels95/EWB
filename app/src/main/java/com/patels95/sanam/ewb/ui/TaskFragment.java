package com.patels95.sanam.ewb.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParseProjectId = ProjectsActivity.getParseProjectId();
        mProjectTitle = ProjectsActivity.getProjectTitle();

        getParseTasks();

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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            intent.putExtra(ParseConstants.PROJECT_TITLE, mProjectTitle);
            startActivity(intent);
        }
    }

    private void getParseTasks() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TASK_CLASS).whereEqualTo(ParseConstants.TASK_PROJECT_ID, mParseProjectId);
        try {
            List<ParseObject> list = query.find();
            mTasks = new Task[list.size()];
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
                mTasks[i] = task;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        public void onFragmentInteraction(String id);
    }

}
