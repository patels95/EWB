package com.gai.ewbbu.ewb.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gai.ewbbu.ewb.R;
import com.gai.ewbbu.ewb.util.Constants;
import com.gai.ewbbu.ewb.model.Project;
import com.gai.ewbbu.ewb.ui.ProjectsActivity;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private Project[] mProjectCards;
    private Context mContext;

    public ProjectAdapter(Context context, Project[] projectCards){
        mContext = context;
        mProjectCards = projectCards;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_card, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        holder.bindProject(mProjectCards[position]);
    }

    @Override
    public int getItemCount() {
        return mProjectCards.length;
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle;
        TextView mDescription;
        ImageView mImage;
        TextView mFirebaseKey;


        public ProjectViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.projectTitle);
            mDescription = (TextView) itemView.findViewById(R.id.projectDescription);
            mImage = (ImageView) itemView.findViewById(R.id.projectImage);
            mFirebaseKey = (TextView) itemView.findViewById(R.id.firebaseKey);

            itemView.setOnClickListener(this);
        }

        public void bindProject(Project projectCard) {
            String uri = projectCard.getImageUri();
            int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
            Drawable drawable = mContext.getResources().getDrawable(imageResource);

            mTitle.setText(projectCard.getTitle());
            mDescription.setText(projectCard.getDescription());
            mImage.setImageDrawable(drawable);
            mFirebaseKey.setText(projectCard.getFirebaseKey());
        }

        @Override
        public void onClick(View v) {
            TextView title = (TextView) v.findViewById(R.id.projectTitle);
            TextView firebaseKey = (TextView) v.findViewById(R.id.firebaseKey);

            // start ProjectsActivity for clicked project
            Intent intent = new Intent(mContext, ProjectsActivity.class);
            intent.putExtra(Constants.PROJECT_TITLE, title.getText().toString());
            intent.putExtra(Constants.FIREBASE_KEY, firebaseKey.getText().toString());
            mContext.startActivity(intent);
        }
    }

}
