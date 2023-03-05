package tws.foodforlife.app.firebase.dao;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import tws.foodforlife.app.model.User;

public interface UserDAO {
    Task<SignInMethodQueryResult> fetchSigninMethodForEmail(String email);

    Task<Void> sendEmailVerification();

    Task<AuthResult> signInWithEmailAndPass(String email, String pass);

    Task<AuthResult> createUserWithEmailAndPassword(String email, String pass);

    Task<Void> setUserDetails(String userId, User user);

    UploadTask uploadUserProImage(String folder, Uri uri);

    Task<Uri> getDownloadUrl(String folder);

    Task<DocumentSnapshot> getUserDetails(String userId);

    Task<Void> updateUserMultiData(String userId, HashMap<String, Object> mMap);

    Task<Void> updateUserSingleData(String userId, String key, Object object);

    FirebaseUser getCurrentUser();

    FirebaseAuth getAuth();

    void signout();
}
