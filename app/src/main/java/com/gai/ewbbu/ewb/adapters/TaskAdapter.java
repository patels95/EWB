package com.gai.ewbbu.ewb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context mContext;
    private Task[] mTasks;
    public TaskAdapter(Context context, Task[] tasks) {
        mContext = context;
        mTasks = tasks;
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

    static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            Calendar calendar = task.getDueDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
            mDueDate.setText(dateFormat.format(calendar.getTime()));

        }

        @Override
        public void onClick(View v) {
            // start task activity
        }
    }
}

