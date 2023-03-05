package tws.foodforlife.app.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import tws.foodforlife.app.firebase.COMMCONS;

public class Comment {
    @PropertyName(COMMCONS.C_ID)
    private String comId;

    @PropertyName(COMMCONS.C_POST_ID)
    private String postId;

    @PropertyName(COMMCONS.C_COMMENT_BY_ID)
    private String userId;

    @PropertyName(COMMCONS.C_COMMENT)
    private String comment;

    @ServerTimestamp
    @PropertyName(COMMCONS.C_TIMESTAMP)
    private Timestamp timestamp;

    public Comment() {
    }

    @PropertyName(COMMCONS.C_ID)
    public String getComId() {
        return comId;
    }

    @PropertyName(COMMCONS.C_ID)
    public void setComId(String comId) {
        this.comId = comId;
    }

    @PropertyName(COMMCONS.C_POST_ID)
    public String getPostId() {
        return postId;
    }

    @PropertyName(COMMCONS.C_POST_ID)
    public void setPostId(String postId) {
        this.postId = postId;
    }

    @PropertyName(COMMCONS.C_COMMENT_BY_ID)
    public String getUserId() {
        return userId;
    }

    @PropertyName(COMMCONS.C_COMMENT_BY_ID)
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PropertyName(COMMCONS.C_COMMENT)
    public String getComment() {
        return comment;
    }

    @PropertyName(COMMCONS.C_COMMENT)
    public void setComment(String comment) {
        this.comment = comment;
    }

    @PropertyName(COMMCONS.C_TIMESTAMP)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @PropertyName(COMMCONS.C_TIMESTAMP)
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
