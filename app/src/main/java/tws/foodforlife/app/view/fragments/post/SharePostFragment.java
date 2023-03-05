package tws.foodforlife.app.view.fragments.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tws.foodforlife.app.R;
import tws.foodforlife.app.adapters.PostAdapter;
import tws.foodforlife.app.firebase.POSTCONS;
import tws.foodforlife.app.model.Post;
import tws.foodforlife.app.repository.firebase.PostRepository;
import tws.foodforlife.app.utils.MyGsonParser;

public class SharePostFragment extends Fragment implements PostAdapter.OnPostClickListener {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvShowSharePost;
    private PostAdapter postAdapter;

    private PostRepository postRepo;

    private boolean isLoading = false;
    private DocumentSnapshot lastDocument;

    private List<Post> postList = new ArrayList<>();

    private NavController navController;
    private ShimmerFrameLayout shimmerFrameLayout;
    private View mView;


    public SharePostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postRepo = new PostRepository();
        postAdapter = new PostAdapter(postList, this);
        populateList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init view for init nav controller
        mView = view;


        shimmerFrameLayout = view.findViewById(R.id.fsp_sfl_layout_shimmer);
        rvShowSharePost = view.findViewById(R.id.fsp_rv_showpost);
        postAdapter.setPostList(postList);
        rvShowSharePost.setLayoutManager(new LinearLayoutManager(getContext()));
        rvShowSharePost.setHasFixedSize(true);
        rvShowSharePost.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        swipeRefresh = view.findViewById(R.id.fsp_srl_refresh);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
                populateList();
                swipeRefresh.setRefreshing(false);
            }
        });

        rvShowSharePost.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        postRepo.qSharePostLimitAfter(lastVisible)
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
        postAdapter.notifyDataSetChanged();
        postRepo.qSharePostLimit()
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
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                postAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onPostClick(int position) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        //Toast.makeText(getContext(), "onPostClick " + position, Toast.LENGTH_SHORT).show();
        Post p = postList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(POSTCONS.P_KEY, MyGsonParser.getGsonParser().toJson(p));
        Navigation.findNavController(mView).navigate(R.id.action_global_to_post_details, bundle);
    }

    @Override
    public void onPostLongClick(int position) {
        //Toast.makeText(getContext(), "onPostLongClick " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkClick(int position, boolean isMarked) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

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
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

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
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

    }
}