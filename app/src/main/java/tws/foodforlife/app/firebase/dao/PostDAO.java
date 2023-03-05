package tws.foodforlife.app.firebase.dao;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import tws.foodforlife.app.model.Post;

public interface PostDAO {
    Task<DocumentReference> addPost(Post post);

    Task<Void> updatePostSingleData(String where, String key, Object object);

    Task<Void> updatePostMultipleData(String where, HashMap<String, Object> hashMap);

    UploadTask uploadPostImage(String folder, Uri uri);

    Task<Uri> getDownloadUrl(String folder);

    Query getAllPost();

    Query qAllPostById(String uId);

    Query qAllPostByIdAfter(String uId, DocumentSnapshot documentSnapshot);

    Query qAllSharePost();

    Query qSharePostLimit();

    Query qSharePostLimitAfter(DocumentSnapshot documentSnapshot);

    Query qAllRequestPost();

    Query qRequestPostsLimit();

    Query qRequestPostsLimitAfter(DocumentSnapshot documentSnapshot);
}
