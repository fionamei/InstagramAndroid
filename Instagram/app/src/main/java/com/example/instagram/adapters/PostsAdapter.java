package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.PostFavoriteCallback;
import com.example.instagram.PostFavoriteListener;
import com.example.instagram.R;
import com.example.instagram.activities.PostDetailActivity;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
        holder.ivImage.setTag(post);
        holder.ibLike.setTag(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> postList) {
        posts.addAll(postList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvTimeAgo;
        private ImageView ivProfilePic;
        private TextView tvLikes;
        private ImageButton ibLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            listenerSetup();
        }

        private void initViews(@NonNull View itemView) {
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            ibLike = itemView.findViewById(R.id.ibLike);
        }

        private void listenerSetup() {
            detailViewListener();
            likeListener();
        }

        private void detailViewListener() {
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = (Post) v.getTag();
                    Intent i = new Intent(context, PostDetailActivity.class);
                    i.putExtra("post", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
        }

        private void likeListener() {
            ibLike.setOnClickListener(new PostFavoriteListener(new PostFavoriteCallback() {
                @Override
                public void onFavoriteSuccess() {
                    Log.i("PostsAdapter", "post is clicked!!");
                    notifyDataSetChanged();
                }
            }));
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            tvTimeAgo.setText(post.getTimeAgo());
            ParseFile profilePic = post.getProfilePic();
            if (profilePic != null) {
                Glide.with(context).load(profilePic.getUrl()).circleCrop().into(ivProfilePic);
            }
            Resources res = context.getResources();
            String likes = res.getQuantityString(R.plurals.likes, post.getLikes(), post.getLikes());
            tvLikes.setText(likes);


        }

    }
}
