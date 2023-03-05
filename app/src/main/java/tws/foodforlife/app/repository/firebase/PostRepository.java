package tws.foodforlife.app.repository.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import tws.foodforlife.app.firebase.FirestoreConstant;
import tws.foodforlife.app.firebase.POSTCONS;
import tws.foodforlife.app.firebase.dao.PostDAO;
import tws.foodforlife.app.model.Post;

public class PostRepository implements PostDAO {
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public PostRepository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection(FirestoreConstant.POSTS);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference(FirestoreConstant.POSTS);
    }


    @Override
    public Task<DocumentReference> addPost(Post post) {
        return collectionReference.add(post);
    }

    @Override
    public Task<Void> updatePostSingleData(String where, String key, Object object) {
        return collectionReference.document(where).update(key, object);
    }

    @Override
    public Task<Void> updatePostMultipleData(String where, HashMap<String, Object> hashMap) {
        return null;
    }

    @Override
    public UploadTask uploadPostImage(String folder, Uri uri) {
        return storageReference.child(folder).child("image.jpg").putFile(uri);
    }

    @Override
    public Task<Uri> getDownloadUrl(String folder) {
        return storageReference.child(folder).child("image.jpg").getDownloadUrl();
    }

    @Override
    public Query getAllPost() {
        Query query = collectionReference;
        return query;
    }

    @Override
    public Query qAllPostById(String uId) {
        return collectionReference.whereEqualTo(POSTCONS.P_POSTED_BY_ID, uId)
                .limit(5);
    }

    @Override
    public Query qAllPostByIdAfter(String uId, DocumentSnapshot documentSnapshot) {
        return collectionReference.whereEqualTo(POSTCONS.P_POSTED_BY_ID, uId)
                .startAfter(documentSnapshot).limit(5);
    }

    @Override
    public Query qAllSharePost() {
        return collectionReference.whereEqualTo(POSTCONS.P_TYPE, POSTCONS.P_TYPE_SHARE);
    }

    @Override
    public Query qSharePostLimit() {
        return collectionReference.whereEqualTo(POSTCONS.P_TYPE, POSTCONS.P_TYPE_SHARE)
                .limit(5);
    }

    @Override
    public Query qSharePostLimitAfter(DocumentSnapshot documentSnapshot) {
        return collectionReference.whereEqualTo(POSTCONS.P_TYPE, POSTCONS.P_TYPE_SHARE)
                .startAfter(documentSnapshot)
                .limit(5);
    }

    @Override
    public Query qAllRequestPost() {
        return collectionReference.whereEqualTo(POSTCONS.P_TYPE, POSTCONS.P_TYPE_REQUEST);
    }

    @Override
    public Query qRequestPostsLimit() {
        return collectionReference.whereEqualTo(POSTCONS.P_TYPE, POSTCONS.P_TYPE_REQUEST)
                .limit(5);
    }

    @Override
    public Query qRequestPostsLimitAfter(DocumentSnapshot documentSnapshot) {
        return collectionReference.whereEqualTo(POSTCONS.P_TYPE, POSTCONS.P_TYPE_REQUEST)
                .startAfter(documentSnapshot)
                .limit(5);
    }
}

