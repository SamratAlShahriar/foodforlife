package tws.foodforlife.app.view.fragments.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tws.foodforlife.app.R;
import tws.foodforlife.app.adapters.PostAdapter;
import tws.foodforlife.app.firebase.POSTCONS;
import tws.foodforlife.app.firebase.USERCONS;
import tws.foodforlife.app.model.Post;
import tws.foodforlife.app.repository.firebase.PostRepository;
import tws.foodforlife.app.repository.firebase.UserRepository;
import tws.foodforlife.app.sharedpref.UserSharedPref;
import tws.foodforlife.app.utils.MyGsonParser;

public class YourActivityFragment extends Fragment implements PostAdapter.OnPostClickListener {
    private RatingBar ratingBar;
    private TextView tvShOrDis, tvTotalShOrDis, tvUName, tvUType;
    private CircleImageView ivProPic;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private UserRepository userRepository;
    private PostRepository postRepository;

    private boolean isLoading = false;
    private DocumentSnapshot lastDocument;

    private List<Post> postList = new ArrayList<>();

    private NavController navController;

    private UserSharedPref userSharedPref;

    public YourActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
        postRepository = new PostRepository();
        postAdapter = new PostAdapter(postList, this);
        userSharedPref = new UserSharedPref(getContext());

        populateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        ratingBar = view.findViewById(R.id.fya_rb_rating);
        tvShOrDis = view.findViewById(R.id.fya_tv_sh_or_dis);
        tvTotalShOrDis = view.findViewById(R.id.fya_tv_total_sh_or_dis);
        tvUName = view.findViewById(R.id.fya_tv_u_name);
        tvUType = view.findViewById(R.id.fya_tv_u_type);
        ivProPic = view.findViewById(R.id.fya_iv_pro_pic);
        recyclerView = view.findViewById(R.id.fya_rv_show_u_list);

        populateViews();

        postAdapter.setPostList(postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                Log.d("onscrolled", "onScrolled: " + dx + " " + dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (totalItem < lastVisible + 3) {
                    if (!isLoading) {
                        isLoading = true;
                        if (lastDocument != null) {
                            loadMoreList(lastDocument);
                        }
                    }
                }
            }
        });

    }

    void loadMoreList(DocumentSnapshot lastVisible) {
        postRepository.qAllPostByIdAfter(userRepository.getCurrentUser().getUid(), lastVisible)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Post singlePost = documentSnapshot.toObject(Post.class);
                    singlePost.setpId(documentSnapshot.getId());
                    postList.add(singlePost);
                    Log.d("TAG", "onSuccess: " + singlePost.getpId());
                }
                isLoading = false;
                if (queryDocumentSnapshots.size() <= 0) {
                    isLoading = true;
                    return;
                }
                if (queryDocumentSnapshots.size() > 1) {
                    lastDocument = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                }
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    private void populateList() {
        postList.clear();
        postRepository.qAllPostById(userRepository.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Post singlePost = documentSnapshot.toObject(Post.class);
                    singlePost.setpId(documentSnapshot.getId());
                    postList.add(singlePost);
                    Log.d("TAG", "onSuccess: " + singlePost.getpId());
                }
                if (queryDocumentSnapshots.size() > 1) {
                    lastDocument = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                }
                postAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void populateViews() {
        String name = userSharedPref.getU_NAME();
        String type = userSharedPref.getU_TYPE();
        String imgUrl = userSharedPref.getU_IMAGE_URL();

        tvUName.setText(name);
        tvUType.setText(type);

        if (type.equals(USERCONS.U_TYPE_DONOR)) {
            tvUType.setTextColor(getResources().getColor(R.color.colorDon));
        } else {
            tvUType.setTextColor(getResources().getColor(R.color.colorOrg));
        }

        if (imgUrl != null && !imgUrl.isEmpty()) {
            try {
                Picasso.get().load(imgUrl)
                        .placeholder(R.drawable.ic_user)
                        .into(ivProPic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPostClick(int position) {
        //Toast.makeText(getContext(), "onPostClick " + position, Toast.LENGTH_SHORT).show();
        Post p = postList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(POSTCONS.P_KEY, MyGsonParser.getGsonParser().toJson(p));
        navController.navigate(R.id.action_yourActivityFragment_to_postDetailsFragment, bundle);
    }

    @Override
    public void onPostLongClick(int position) {
        //Toast.makeText(getContext(), "onPostLongClick " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkClick(int position, boolean isMarked) {
        if (isMarked) {
            //Toast.makeText(getContext(), "Checked " + position, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getContext(), "Not Checked " + position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShareClick() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.food_for_life));
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ffl_link));
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onCallClick(int position) {
        if (postList.size() > 0 && position != -1) {
            String phone = postList.get(position).getPhoneNo().trim();
            Uri uri = Uri.parse("tel:" + phone);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @Override
    public void onCommentClick(int position) {

    }
}