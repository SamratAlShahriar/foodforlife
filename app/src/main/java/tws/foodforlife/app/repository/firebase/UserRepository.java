package tws.foodforlife.app.repository.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import tws.foodforlife.app.firebase.FirestoreConstant;
import tws.foodforlife.app.firebase.dao.UserDAO;
import tws.foodforlife.app.model.User;

public class UserRepository implements UserDAO {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection(FirestoreConstant.USERS);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference(FirestoreConstant.USERS);
    }

    @Override
    public Task<SignInMethodQueryResult> fetchSigninMethodForEmail(String email) {
        return getAuth().fetchSignInMethodsForEmail(email);
    }

    @Override
    public Task<Void> sendEmailVerification() {
        return getCurrentUser().sendEmailVerification();
    }

    @Override
    public Task<AuthResult> signInWithEmailAndPass(String email, String pass) {
        return getAuth().signInWithEmailAndPassword(email, pass);
    }

    @Override
    public Task<AuthResult> createUserWithEmailAndPassword(String email, String pass) {
        return getAuth().createUserWithEmailAndPassword(email, pass);
    }

    @Override
    public Task<Void> setUserDetails(String userId, User user) {
        return collectionReference.document(userId).set(user);
    }

    @Override
    public UploadTask uploadUserProImage(String folder, Uri uri) {
        return storageReference.child(folder).child("profile.jpg").putFile(uri);
    }

    @Override
    public Task<Uri> getDownloadUrl(String folder) {
        return storageReference.child(folder).child("profile.jpg").getDownloadUrl();
    }

    @Override
    public Task<DocumentSnapshot> getUserDetails(String userId) {
        return collectionReference.document(userId).get();
    }

    @Override
    public Task<Void> updateUserMultiData(String userId, HashMap<String, Object> mMap) {
        return collectionReference.document(userId).update(mMap);
    }

    @Override
    public Task<Void> updateUserSingleData(String userId, String key, Object object) {
        return collectionReference.document(userId).update(key, object);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return getAuth().getCurrentUser();
    }

    @Override
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    @Override
    public void signout() {
        firebaseAuth = getAuth();
        if (firebaseAuth == null) {
            return;
        }
        firebaseAuth.signOut();
    }


}
