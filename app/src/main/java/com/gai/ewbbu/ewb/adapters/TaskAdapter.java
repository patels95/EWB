package com.gai.ewbbu.ewb.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.model.Task;
import com.gai.ewbbu.ewb.ui.TaskActivity;
import com.gai.ewbbu.ewb.util.Constants;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context mContext;
    private Task[] mTasks;
    private String mProjectTitle;
    public TaskAdapter(Context context, Task[] tasks, String projectTitle) {
        mContext = context;
        mTasks = tasks;
        mProjectTitle = projectTitle;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.bindTask(mTasks[position]);
    }

    @Override
    public int getItemCount() {
        return mTasks.length;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle;
        TextView mDueDate;


        public TaskViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.taskListTitle);
            mDueDate = (TextView) itemView.findViewById(R.id.taskListDueDate);

            itemView.setOnClickListener(this);
        }

        public void bindTask(Task task) {
            mTitle.setText(task.getTitle());
//            Calendar calendar = task.getDueDate();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
//            mDueDate.setText(dateFormat.format(calendar.getTime()));
            mDueDate.setText(task.getDateString());

        }

        @Override
        public void onClick(View v) {
            // task item click -> start task activity
            Task selectedTask = mTasks[getLayoutPosition()];
            Intent intent = new Intent(mContext, TaskActivity.class);
            intent.putExtra(Constants.PROJECT_TITLE, mProjectTitle);
            intent.putExtra(Constants.PROJECT_KEY, selectedTask.getFirebaseProjectKey());
            intent.putExtra(Constants.TASK_KEY, selectedTask.getFirebaseKey());
            mContext.startActivity(intent);
        }
    }
}

