package utils;

import android.content.Context;

public class PortIpAddress {
//    public static String host = "http://192.168.5.228:9788/LEMS/mobile/";
//    public static String host = "http://192.168.6.57:8080/LEMS/mobile/";

    //宜章
    public static String host = "http://218.76.156.141:8083/mobile/";
    public static String myUrl = "http://218.76.156.141:8083/";

    //军
//    public static String host = "http://192.168.5.92/LEMS/mobile/";
//    public static String myUrl = "http://192.168.5.92/LEMS/";


    //获取token
    public static String getToken(Context context) {
        return SharedPrefsUtil.getValue(context, "userInfo", "user_token", "");
    }

    //登陆
    public static String LoginAddress() {
        return host + "login.action";
    }

    public static String Companyinfo() {
        return host + "mobilecompanybase/getCompanyinfo.action";
    }

    //企业详细信息
    public static String CompanyDetailInfo() {
        return host + "mobilecompanybase/getCompanydetailinfo.action";
    }

    //法律法规列表
    public static String GetLawregulations() {
        return host + "mobilelawregulations/getLawregulations.action";
    }

    //法律法规详情
    public static String GetLawregulationsdetail() {
        return host + "mobilelawregulations/getLawregulationsdetail.action";
    }

    //通知公告
    public static String MessageList() {
        return host + "notice/getNoticesent.action";
    }

    //公告详情
    public static String MessageDetail() {
        return host + "notice/noticeInfo.action";
    }

    public static String AllReadInfo() {
        return host + "notice/allReadInfo.action";
    }


    //各分类信息话题
    public static String Mobiletopic() {
        return host + "mobiletopic/getMobileToplist.action";
    }

    //修改密码
    public static String ModifyPwd() {
        return host + "user/updatepwd.action";
    }

    //设置头像
    public static String SaveHead() {
        return host + "user/saveHeadImg.action";
    }

    //行业分类接口
    public static String GetIndustrylist() {
        return host + "mobiletopic/getIndustrylist.action";
    }

    //各分类信息话题接口
    public static String GetMobileToplist() {
        return host + "mobiletopic/getMobileToplist.action";
    }


    //我发表的话题
    public static String WeJoinlist() {
        return host + "mobiletopic/selectMobileWeJoinlist.action";
    }

    //删除我发布的话题
    public static String DelMobileTopicInfo() {
        return host + "mobiletopic/delMobileTopicInfo.action";
    }

    //议事厅tab
    public static String IndustryCat() {
        return host + "mobiletopic/getIndustrylist.action";
    }

    //话题列表
    public static String TopicList() {
        return host + "mobiletopic/getMobileToplist.action";
    }

    //取发布的图片列表
    public static String TopicImage() {
        return host + "mobiletopic/getMobilePicturelist.action";
    }

    //点赞
    public static String Like() {
        return host + "mobiletopic/addMobileTopicFabulousInfo.action";
    }

    //取消赞
    public static String Dislike() {
        return host + "mobiletopic/delMobileTopicFabulousInfo.action";
    }

    //发表话题
    public static String PostTopic() {
        return host + "mobiletopic/addMobileTopInfo.action";
    }

    //获取分类信息列表
    public static String GetTypeList() {
        return host + "";
    }

    //点赞列表
    public static String LikeList() {
        return host + "mobiletopic/getMobileTopicFabulouslist.action";
    }

    //评论列表
    public static String CommentList() {
        return host + "mobiletopic/getMobileTopicReviewlist.action";
    }

    //添加评论
    public static String Comment() {
        return host + "mobiletopic/addMobileTopicReviewInfo.action";
    }

    //点赞
    public static String Favourite() {
        return host + "mobiletopic/addMobileTopicFollowInfo.action";
    }

    //取消赞
    public static String NoFavor() {
        return host + "mobiletopic/delMobileTopicFollowInfo.action";
    }

    //关注
    public static String AddFollow() {
        return host + "mobiletopic/addMobileTopicFollowInfo.action";
    }

    //取消关注
    public static String DelFollow() {
        return host + "mobiletopic/delMobileTopicFollowInfo.action";
    }


    //删除我发表的话题
    public static String DeleteTopic() {
        return host + "mobiletopic/delMobileTopicInfo.action";
    }

    //删除评论
    public static String DeleteComment() {
        return host + "mobiletopic/delMobileTopicReviewInfo.action";
    }

    //我的话题列表
    public static String MyTopicList() {
        return host + "mobiletopic/selectMobileWeReleaselist.action";
    }

    //我关注的话题列表
    public static String MyFavorList() {
        return host + "mobiletopic/selectMobileWeFollowlist.action";
    }

    //现场检查记录列表
    public static String GetXccheckrecordlist() {
        return host + "mobilecheck/getXccheckrecordlist.action";
    }

    //现场检查记录详情
    public static String GetXccheckrecorddetail() {
        return host + "mobilecheck/getXccheckrecorddetail.action";
    }

    //整改情况详情接口
    public static String GetClzgqk() {
        return host + "mobilecheck/getClzgqk.action";
    }

    //新增检查记录
    public static String AddXccheckrecord() {
        return host + "mobilecheck/addXccheckrecord.action";
    }

    //搜索被检查单位
    public static String GetSelectQyname() {
        return host + "mobilecheck/getSelectQyname.action";
    }

}
