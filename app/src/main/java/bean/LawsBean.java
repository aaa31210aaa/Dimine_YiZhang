package bean;

public class LawsBean {
    //法律法规标题
    private String lrtitle;
    //法律法规ID
    private String flfgid;
    //所属行业
    private String industry;
    //所属具体类别
    private String lrtypename;
    private boolean tag;
    //文号
    private String docnumber;
    //颁布时间
    private String bbdate;
    //生效时间
    private String effdate;
    //法律法规信息
    private String lrdesc;

    public String getLrtitle() {
        return lrtitle;
    }

    public void setLrtitle(String lrtitle) {
        this.lrtitle = lrtitle;
    }

    public String getFlfgid() {
        return flfgid;
    }

    public void setFlfgid(String flfgid) {
        this.flfgid = flfgid;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLrtypename() {
        return lrtypename;
    }

    public void setLrtypename(String lrtypename) {
        this.lrtypename = lrtypename;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public String getDocnumber() {
        return docnumber;
    }

    public void setDocnumber(String docnumber) {
        this.docnumber = docnumber;
    }

    public String getBbdate() {
        return bbdate;
    }

    public void setBbdate(String bbdate) {
        this.bbdate = bbdate;
    }

    public String getEffdate() {
        return effdate;
    }

    public void setEffdate(String effdate) {
        this.effdate = effdate;
    }

    public String getLrdesc() {
        return lrdesc;
    }

    public void setLrdesc(String lrdesc) {
        this.lrdesc = lrdesc;
    }
}
