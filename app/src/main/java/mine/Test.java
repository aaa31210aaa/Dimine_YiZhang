package mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.administrator.dimine_yizhang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.TestAdapter;
import bean.Circle;
import utils.BaseActivity;
import utils.DateUtils;

public class Test extends BaseActivity {
    private ListView testList;
    private List<Circle> mDatas;  //文字内容集合

    private String username[] = {"admin", "红领巾", "测试员", "小队长", "中队长", "大队长", "机器人", "小冰"};
    private String sendtime[] = {"2017-04-21 10:00:00", "2017-04-19 20:30:50", "2017-04-18 06:31:20", "2017-04-20 11:40:32", "2017-03-21 10:30:36", "2017-02-23 14:00:01", "2017-02-28 23:36:40", "2017-03-01 16:21:33"};
    private int image[] = {R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4, R.drawable.head5, R.drawable.head6, R.drawable.head7, R.drawable.head8,};
    private String content[] = {"测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息测试信息息测试信息测试信息测试信息测试信息测试信息息测试信息测试信息测试信息测试信息测试信息", "今天天气真好啊", "真的是累啊累", "今天出去烧烤有人一起吗", "听，这里有人在唱歌", "风萧萧兮易水寒", "壮士一去兮不复还", "长使英雄泪满襟"};
    private int[] dz_arr = {10, 50, 23, 102, 32, 99, 78, 6};
    private int[] comment_arr = {50, 23, 13, 6, 110, 99, 74, 10};

    private Map<Integer, String[]> imageMap;
    private List<String[]> paths;

    private String path1[] = {"http://img1.hao661.com/uploads/allimg/160113/234J23407-0.jpg"
            , "http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4e8d38e8eef81a4c2667e4cde21a4c6f/b8014a90f603738debc986beb61bb051f919ecaf.jpg"
            , "http://img2.imgtn.bdimg.com/it/u=523770096,2288688314&fm=23&gp=0.jpg"
            , "http://www.wangmingdaquan.cc/tx71/135.jpg"};

    private String path2[] = {"http://img4.imgtn.bdimg.com/it/u=540284258,3211756076&fm=23&gp=0.jpg"
            , "http://img2.imgtn.bdimg.com/it/u=4272553786,4102977818&fm=23&gp=0.jpg"
            , "http://img0.imgtn.bdimg.com/it/u=921333011,866123641&fm=23&gp=0.jpg"
    };
    private String path3[] = {
    };

//    "http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4e8d38e8eef81a4c2667e4cde21a4c6f/b8014a90f603738debc986beb61bb051f919ecaf.jpg"
//            , "http://img2.imgtn.bdimg.com/it/u=523770096,2288688314&fm=23&gp=0.jpg"
//            , "http://www.wangmingdaquan.cc/tx71/135.jpg"

    private String path4[] = {"http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4e8d38e8eef81a4c2667e4cde21a4c6f/b8014a90f603738debc986beb61bb051f919ecaf.jpg"
            , "http://www.wangmingdaquan.cc/tx71/135.jpg"
            , "http://img2.imgtn.bdimg.com/it/u=523770096,2288688314&fm=23&gp=0.jpg"
    };
    private String path5[] = {"http://www.wangmingdaquan.cc/tx71/135.jpg"
    };
    private String path6[] = {"http://img1.hao661.com/uploads/allimg/160113/234J23407-0.jpg"
            , "http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4e8d38e8eef81a4c2667e4cde21a4c6f/b8014a90f603738debc986beb61bb051f919ecaf.jpg"
            , "http://img2.imgtn.bdimg.com/it/u=523770096,2288688314&fm=23&gp=0.jpg"
    };
    private String path8[] = {"http://img1.hao661.com/uploads/allimg/160113/234J23407-0.jpg"
            , "http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4e8d38e8eef81a4c2667e4cde21a4c6f/b8014a90f603738debc986beb61bb051f919ecaf.jpg"
            , "http://img2.imgtn.bdimg.com/it/u=523770096,2288688314&fm=23&gp=0.jpg"
    };

    private String path7[] = {"http://img2.imgtn.bdimg.com/it/u=523770096,2288688314&fm=23&gp=0.jpg"
            , "http://img1.hao661.com/uploads/allimg/160113/234J23407-0.jpg"
            , "http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4e8d38e8eef81a4c2667e4cde21a4c6f/b8014a90f603738debc986beb61bb051f919ecaf.jpg"
    };


    private TestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        testList = (ListView) findViewById(R.id.testList);
    }

    @Override
    protected void initData() {
        initListView();
    }

    /**
     * 设置listview
     */
    private void initListView() {
        mDatas = new ArrayList<>();
        paths = new ArrayList<>();
        imageMap = new HashMap<>();
        paths.add(path1);
        paths.add(path2);
        paths.add(path3);
        paths.add(path4);
        paths.add(path5);
        paths.add(path6);
        paths.add(path7);
        paths.add(path8);

        for (int i = 0; i < username.length; i++) {
            Circle bean = new Circle();
            bean.setUsername(username[i]);
            bean.setSendTime(DateUtils.getRencentTime(sendtime[i], "yyyy-MM-dd HH:mm:ss"));
            bean.setContent(content[i]);
            bean.setDzNum(dz_arr[i]);
            bean.setCommentNum(comment_arr[i]);
            bean.setHeadimage(image[i]);
            imageMap.put(i, paths.get(i));
            mDatas.add(bean);
        }
        adapter = new TestAdapter(this, mDatas, imageMap);
        testList.setAdapter(adapter);
    }


    @Override
    protected void setOnClick() {

    }

    @Override
    public void onClick(View v) {

    }
}
