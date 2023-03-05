package tws.foodforlife.app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tws.foodforlife.app.R;
import tws.foodforlife.app.firebase.POSTCONS;
import tws.foodforlife.app.model.Post;
import tws.foodforlife.app.sharedpref.UserSharedPref;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList = new ArrayList<>();
    private UserSharedPref userSharedPref;
    private String currentUid;
    private String currentUserType;

    private OnPostClickListener listener;

    public PostAdapter(List<Post> postList, OnPostClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userSharedPref = new UserSharedPref(parent.getContext());
        currentUid = userSharedPref.getU_ID();
        currentUserType = userSharedPref.getU_TYPE();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_single_item_post, parent, false);
        return new PostViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        Post post = postList.get(position);
        String name = post.getPostedByName();
        if (name != null) {
            holder.tvName.setText(name);
        }

        SimpleDateFormat sfd = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        String timestamp = sfd.format((post.getTimestamp().toDate()));
        holder.tvTime.setText(timestamp);

        holder.tvLocation.setText(post.getpLocationName());
        holder.tvPost.setText(post.getMainPost());

        String pUrl = post.getpImageUrl();
        try {
            if (pUrl != null && !pUrl.isEmpty()) {
                try {
                    Picasso.get()
                            .load(pUrl)
                            .resizeDimen(R.dimen.post_img_width, R.dimen.post_img_height)
                            .onlyScaleDown()
                            .into(holder.ivPostImage);
                } catch (Exception e) {
                    Log.d("Post Adapter", "onBindViewHolder: " + e.toString());
                }
            } else {
                holder.ivPostImage.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d("TAG", "onBindViewHolder: " + e.toString());
        }

        String uUrl = post.getPostedByImageUrl();
        try {
            if (uUrl != null && !uUrl.isEmpty()) {
                Picasso.get().load(uUrl)
                        .placeholder(R.drawable.ic_user)
                        .into(holder.civProfile);
            }
        } catch (Exception e) {
            Log.d("TAG", "onBindViewHolder: " + e.toString());
        }

        //mark invisible
        /*
        String postedByUid = post.getPostedById();
        if (currentUid.equals(postedByUid)) {
            holder.cbMark.setVisibility(View.VISIBLE);
            String status = post.getStatus();
            String postType = post.getpType();

            if(currentUserType.equals(USERCONS.U_TYPE_DONOR)){
                if(status.equals(POSTCONS.P_STATUS_AVAILABLE)) {
                    holder.cbMark.setChecked(false);
                    holder.cbMark.setEnabled(true);
                } else if (status.equals(POSTCONS.P_STATUS_DONATED)) {
                    holder.cbMark.setChecked(false);
                    holder.cbMark.setEnabled(true);
                } else {
                    holder.cbMark.setChecked(true);
                    holder.cbMark.setEnabled(false);
                }
            } else {
                if (status.equals(POSTCONS.P_STATUS_AVAILABLE)) {
                    holder.cbMark.setChecked(false);
                    holder.cbMark.setEnabled(true);
                } else if (status.equals(POSTCONS.P_STATUS_DONATED)) {
                    holder.cbMark.setChecked(false);
                    holder.cbMark.setEnabled(true);
                } else {
                    holder.cbMark.setChecked(true);
                    holder.cbMark.setEnabled(false);
                }
            }


            if (postType.equals(POSTCONS.P_TYPE_SHARE)) {
                holder.cbMark.setText(R.string.mark_as_donated);
            } else {
                holder.cbMark.setText(R.string.mark_as_distributed);
            }
        } else {
            holder.cbMark.setEnabled(false);
            holder.cbMark.setVisibility(View.INVISIBLE);
        }*/
        //

        String status = post.getStatus();
        if (status.equals(POSTCONS.P_STATUS_DISTRIBUTED)) {
            holder.ivStatus.setImageResource(R.drawable.ic_baseline_distributed);
            //holder.ivStatus.setColorFilter(R.color.colorDistributed);
        } else if (status.equals(POSTCONS.P_STATUS_DONATED)) {
            holder.ivStatus.setImageResource(R.drawable.ic_baseline_donated);
            //holder.ivStatus.setColorFilter(R.color.colorDonated);
        } else {
            holder.ivStatus.setImageResource(R.drawable.ic_baseline_available);
        }


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }


    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvLocation, tvPost;
        ImageView ivPostImage, ivStatus, ivReact, ivCall, ivComment, ivShare;
        CircleImageView civProfile;
        CheckBox cbMark;

        OnPostClickListener listener;


        public PostViewHolder(@NonNull View itemView, OnPostClickListener listener) {
            super(itemView);

            tvName = itemView.findViewById(R.id.lsip_tv_name);
            tvTime = itemView.findViewById(R.id.lsip_tv_post_time);
            tvLocation = itemView.findViewById(R.id.lsip_tv_location);
            tvPost = itemView.findViewById(R.id.lsip_tv_post_text);

            civProfile = itemView.findViewById(R.id.lsip_civ_profile);
            ivPostImage = itemView.findViewById(R.id.lsip_iv_post_image);
            ivStatus = itemView.findViewById(R.id.lsip_iv_status_image);
            ivReact = itemView.findViewById(R.id.lsip_iv_react);
            ivCall = itemView.findViewById(R.id.lsip_iv_call);
            ivComment = itemView.findViewById(R.id.lsip_iv_comment);
            ivShare = itemView.findViewById(R.id.lsip_iv_share);
            cbMark = itemView.findViewById(R.id.lsip_cb_mark);

            this.listener = listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostClick(getAdapterPosition());
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onPostLongClick(getAdapterPosition());
                    return true;
                }
            });


            cbMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMarkClick(getAdapterPosition(), cbMark.isChecked());
                }
            });


            ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCallClick(getAdapterPosition());
                }
            });

            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCommentClick(getAdapterPosition());
                }
            });

            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onShareClick();
                }
            });
        }
    }

    public interface OnPostClickListener {
        void onPostClick(int position);

        void onPostLongClick(int position);

        void onMarkClick(int position, boolean isMarked);

        void onCommentClick(int position);

        void onCallClick(int position);

        void onShareClick();
    }

}
