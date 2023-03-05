package tws.foodforlife.app.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import tws.foodforlife.app.firebase.RESPCONS;

public class PostResponse {
    @PropertyName(RESPCONS.RE_ID)
    private String resId;

    @PropertyName(RESPCONS.RE_POST_ID)
    private String postId;

    @PropertyName(RESPCONS.RE_RESPOND_BY_ID)
    private String responderId;

    @PropertyName(RESPCONS.RE_IS_ACCEPTED)
    private boolean isAccepted;

    @ServerTimestamp
    @PropertyName(RESPCONS.RE_TIMESTAMP)
    private Timestamp timestamp;

    public PostResponse() {
    }

    @PropertyName(RESPCONS.RE_ID)
    public String getResId() {
        return resId;
    }

    @PropertyName(RESPCONS.RE_ID)
    public void setResId(String resId) {
        this.resId = resId;
    }

    @PropertyName(RESPCONS.RE_POST_ID)
    public String getPostId() {
        return postId;
    }

    @PropertyName(RESPCONS.RE_POST_ID)
    public void setPostId(String postId) {
        this.postId = postId;
    }

    @PropertyName(RESPCONS.RE_RESPOND_BY_ID)
    public String getResponderId() {
        return responderId;
    }

    @PropertyName(RESPCONS.RE_RESPOND_BY_ID)
    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    @PropertyName(RESPCONS.RE_IS_ACCEPTED)
    public boolean isAccepted() {
        return isAccepted;
    }

    @PropertyName(RESPCONS.RE_IS_ACCEPTED)
    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    @PropertyName(RESPCONS.RE_TIMESTAMP)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @PropertyName(RESPCONS.RE_TIMESTAMP)
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
