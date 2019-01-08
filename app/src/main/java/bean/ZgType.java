package bean;

/**
 * Created by Administrator on 2017-03-24.
 */
public class ZgType {
    private int zgid;
    //图片
    private int image;
    //企业名字
    private String qyname;
    //整改类型
    private String zgtype;
    //整改内容
    private String content;
    //整改时间
    private String zgdate;
    //整改来源
    private String zgly;
    //整改状态
    private String zgtag;

    public int getZgid() {
        return zgid;
    }

    public void setZgid(int zgid) {
        this.zgid = zgid;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getQyname() {
        return qyname;
    }

    public void setQyname(String qyname) {
        this.qyname = qyname;
    }

    public String getZgtype() {
        return zgtype;
    }

    public void setZgtype(String zgtype) {
        this.zgtype = zgtype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getZgdate() {
        return zgdate;
    }

    public void setZgdate(String zgdate) {
        this.zgdate = zgdate;
    }

    public String getZgly() {
        return zgly;
    }

    public void setZgly(String zgly) {
        this.zgly = zgly;
    }

    public String getZgtag() {
        return zgtag;
    }

    public void setZgtag(String zgtag) {
        this.zgtag = zgtag;
    }
}
