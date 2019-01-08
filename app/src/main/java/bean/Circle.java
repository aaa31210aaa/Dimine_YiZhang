package bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Circle implements Parcelable {
    private String id;
    //头像
    private int headimage;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    private String headUrl;
    //用户名
    private String username;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;
    //发布的内容
    private String content;
    private String imageUrls[];
    private String recycleImage;
    private int recycleImageInt;
    //发布的时间
    private String sendTime;
    //点赞标识
    private boolean dz_tag;

    public boolean isFv_tag() {
        return fv_tag;
    }

    public void setFv_tag(boolean fv_tag) {
        this.fv_tag = fv_tag;
    }

    private boolean fv_tag;
    public List<FavortBean> favorters = new ArrayList<>(5);
    public List<CommentBean> comments = new ArrayList<>(5);
    //点赞数量
    private int dzNum;
    //评论数量
    private int commentNum;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHeadimage() {
        return headimage;
    }

    public void setHeadimage(int headimage) {
        this.headimage = headimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isDz_tag() {
        return dz_tag;
    }

    public void setDz_tag(boolean dz_tag) {
        this.dz_tag = dz_tag;
    }

    public String getRecycleImage() {
        return recycleImage;
    }

    public void setRecycleImage(String recycleImage) {
        this.recycleImage = recycleImage;
    }

    public int getRecycleImageInt() {
        return recycleImageInt;
    }

    public void setRecycleImageInt(int recycleImageInt) {
        this.recycleImageInt = recycleImageInt;
    }

    public boolean hasFavort() {
        if (favorters != null && favorters.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean hasComment() {
        if (comments != null && comments.size() > 0) {
            return true;
        }
        return false;
    }

    public int getDzNum() {
        return dzNum;
    }

    public void setDzNum(int dzNum) {
        this.dzNum = dzNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(headimage);
        dest.writeString(headUrl);
        dest.writeString(username);
        dest.writeString(content);
        dest.writeStringArray(imageUrls);
        dest.writeString(recycleImage);
        dest.writeInt(recycleImageInt);
        dest.writeString(sendTime);
        dest.writeInt(dz_tag?1:0);
        dest.writeInt(dzNum);
        dest.writeInt(commentNum);
        dest.writeString(title);
    }

    public Circle() {
        headimage = 0x7f020066;
    }

    protected Circle(Parcel in) {
        id = in.readString();
        headimage = in.readInt();
        headUrl = in.readString();
        username = in.readString();
        content = in.readString();
        imageUrls = in.createStringArray();
        recycleImage = in.readString();
        recycleImageInt = in.readInt();
        sendTime = in.readString();
        dz_tag = in.readByte() != 0;
        dzNum = in.readInt();
        commentNum = in.readInt();
        title = in.readString();
    }

    public static final Creator<Circle> CREATOR = new Creator<Circle>() {
        @Override
        public Circle createFromParcel(Parcel in) {
            return new Circle(in);
        }

        @Override
        public Circle[] newArray(int size) {
            return new Circle[size];
        }
    };
}
