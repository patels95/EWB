package com.patels95.sanam.ewb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.patels95.sanam.ewb.R;
import com.patels95.sanam.ewb.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskAdapter extends BaseAdapter {

    private Context mContext;
    private Task[] mTasks;

    public TaskAdapter(Context context, Task[] tasks) {
        mContext = context;
        mTasks = tasks;
    }

    @Override
    public int getCount() {
        return mTasks.length;
    }

    @Override
    public Object getItem(int position) {
        return mTasks[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // new view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_list_item, null);
            holder = new ViewHolder();
            holder.taskTitle = (TextView) convertView.findViewById(R.id.taskListTitle);
            holder.taskDueDate = (TextView) convertView.findViewById(R.id.taskListDueDate);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = mTasks[position];
        holder.taskTitle.setText(task.getTitle());
        Calendar calendar = task.getDueDate();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        String dueDate = format.format(calendar.getTime());
        holder.taskDueDate.setText(dueDate);

        return convertView;
    }

    private static class ViewHolder {
        public TextView taskTitle;
        public TextView taskDueDate;
    }
}

