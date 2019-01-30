package com.example.samy.firebaseuploadimage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class FirePostRecyclerAdapter extends FirestoreRecyclerAdapter <Post,FirePostRecyclerAdapter.PostViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public FirePostRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
        //Picasso.get().load(model.getImage()).into(holder.imageView);
    //    Glide.with(context).asGif().load(model.getImage()).into(holder.imageView);
        // Set the image
       // RequestOptions defaultOptions = new RequestOptions()
       //         .error(R.drawable.ic_launcher_background);
        Glide.with(context)
             //   .setDefaultRequestOptions(defaultOptions)
                .load(model.getImage())
             //   .load("https://firebasestorage.googleapis.com/v0/b/fir-uploadimage-9e02e.appspot.com/o/images%2Fimage%3A17?alt=media&token=673353ce-ebaf-461c-9aa1-bc1b2c3b3e09")
                .into(((PostViewHolder)holder).imageView);

        holder.titleView.setText(model.getTitle());
        holder.descView.setText(model.getImage());
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_layout, viewGroup,false);
        PostViewHolder postViewHolder=new PostViewHolder(inflate);
        return postViewHolder;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        TextView titleView,descView;
        ImageView imageView;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view);
            descView=itemView.findViewById(R.id.desc_recycler);
            titleView=itemView.findViewById(R.id.title_recycler);
        }
    }
}
