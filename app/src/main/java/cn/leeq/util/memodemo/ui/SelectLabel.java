package cn.leeq.util.memodemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;
import cn.leeq.util.memodemo.bean.LabelFirst;
import cn.leeq.util.memodemo.bean.LabelSecond;
import cn.leeq.util.memodemo.widget.CommentListView;

public class SelectLabel extends AppCompatActivity {

    private CommentListView lvFirst;
    private CommentListView lvSecond;
    private List<LabelFirst> lfData = new ArrayList<>();
    private String lfName;
    private int lfCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_laebl);
        fillData();
        init();

    }

    private void fillData() {
        //台式电脑
        List<LabelSecond> lsData1 = new ArrayList<>();
        lsData1.add(new LabelSecond(1, "联想"));
        lsData1.add(new LabelSecond(2, "华硕"));
        lsData1.add(new LabelSecond(3, "惠普"));
        lsData1.add(new LabelSecond(4, "神舟"));
        lsData1.add(new LabelSecond(5, "戴尔"));
        lsData1.add(new LabelSecond(6, "海尔"));
        lsData1.add(new LabelSecond(7, "SONY"));
        lfData.add(new LabelFirst(1, "台式电脑", lsData1));
        //平板电脑
        List<LabelSecond> lsData2 = new ArrayList<>();
        lsData2.add(new LabelSecond(1, "联想"));
        lsData2.add(new LabelSecond(2, "iPad"));
        lsData2.add(new LabelSecond(3, "七彩虹"));
        lsData2.add(new LabelSecond(4, "酷比魔方"));
        lsData2.add(new LabelSecond(5, "昂达"));
        lsData2.add(new LabelSecond(6, "台电"));
        lfData.add(new LabelFirst(2, "平板电脑", lsData2));
        //鼠标
        List<LabelSecond> lsData3 = new ArrayList<>();
        lsData3.add(new LabelSecond(1, "雷蛇"));
        lsData3.add(new LabelSecond(2, "富勒"));
        lsData3.add(new LabelSecond(3, "新盟"));
        lsData3.add(new LabelSecond(4, "双飞燕"));
        lsData3.add(new LabelSecond(5, "微软"));
        lfData.add(new LabelFirst(3, "鼠标", lsData3));
        //投影仪
        List<LabelSecond> lsData4 = new ArrayList<>();
        lsData4.add(new LabelSecond(1, "明基"));
        lsData4.add(new LabelSecond(2, "NEC"));
        lsData4.add(new LabelSecond(3, "奥图码"));
        lsData4.add(new LabelSecond(4, "松下"));
        lsData4.add(new LabelSecond(5, "索尼"));
        lfData.add(new LabelFirst(4, "投影仪", lsData4));
        //键盘
        List<LabelSecond> lsData5 = new ArrayList<>();
        lsData5.add(new LabelSecond(1, "雷蛇"));
        lsData5.add(new LabelSecond(2, "双飞燕"));
        lsData5.add(new LabelSecond(3, "微软"));
        lsData5.add(new LabelSecond(4, "富勒"));
        lsData5.add(new LabelSecond(5, "海盗船"));
        lfData.add(new LabelFirst(5, "键盘", lsData5));
        //耳机
        List<LabelSecond> lsData6 = new ArrayList<>();
        lsData6.add(new LabelSecond(1, "森海塞尔"));
        lsData6.add(new LabelSecond(2, "BEATS"));
        lsData6.add(new LabelSecond(3, "漫步者"));
        lsData6.add(new LabelSecond(4, "飞利浦"));
        lsData6.add(new LabelSecond(5, "铁三角"));
        lsData6.add(new LabelSecond(6, "SONY"));
        lsData6.add(new LabelSecond(7, "拜亚"));
        lsData6.add(new LabelSecond(8, "FIIL"));
        lfData.add(new LabelFirst(6, "耳机", lsData6));
        //家庭工具
        List<LabelSecond> lsData7 = new ArrayList<>();
        lsData7.add(new LabelSecond(1, "门锁"));
        lsData7.add(new LabelSecond(2, "吸尘器"));
        lsData7.add(new LabelSecond(3, "漫步者"));
        lsData7.add(new LabelSecond(4, "飞利浦"));
        lsData7.add(new LabelSecond(5, "铁三角"));
        lsData7.add(new LabelSecond(6, "SONY"));
        lfData.add(new LabelFirst(7, "家庭工具", lsData7));
        //U盘
        List<LabelSecond> lsData8 = new ArrayList<>();
        lsData8.add(new LabelSecond(1, "闪迪"));
        lsData8.add(new LabelSecond(2, "东芝"));
        lsData8.add(new LabelSecond(3, "威刚"));
        lsData8.add(new LabelSecond(4, "金士顿"));
        lsData8.add(new LabelSecond(5, "惠普"));
        lsData8.add(new LabelSecond(6, "台电"));
        lfData.add(new LabelFirst(8, "U盘", lsData8));
        //投影仪
        List<LabelSecond> lsData9 = new ArrayList<>();
        lsData9.add(new LabelSecond(1, "明基"));
        lsData9.add(new LabelSecond(2, "NEC"));
        lsData9.add(new LabelSecond(3, "奥图码"));
        lsData9.add(new LabelSecond(4, "松下"));
        lsData9.add(new LabelSecond(5, "索尼"));
        lfData.add(new LabelFirst(9, "投影仪", lsData9));
        //照相机
        List<LabelSecond> lsDataX = new ArrayList<>();
        lsDataX.add(new LabelSecond(1, "佳能"));
        lsDataX.add(new LabelSecond(2, "尼康"));
        lsDataX.add(new LabelSecond(3, "索尼"));
        lsDataX.add(new LabelSecond(4, "徕卡"));
        lsDataX.add(new LabelSecond(5, "理光"));
        lsDataX.add(new LabelSecond(6, "富士"));
        lfData.add(new LabelFirst(10, "照相机", lsDataX));
        //手机
        List<LabelSecond> lsDataX1 = new ArrayList<>();
        lsDataX1.add(new LabelSecond(1, "Nexus"));
        lsDataX1.add(new LabelSecond(2, "三星"));
        lsDataX1.add(new LabelSecond(3, "苹果"));
        lsDataX1.add(new LabelSecond(4, "华为"));
        lsDataX1.add(new LabelSecond(5, "OPPO"));
        lsDataX1.add(new LabelSecond(6, "魅族"));
        lsDataX1.add(new LabelSecond(7, "Vivo"));
        lfData.add(new LabelFirst(11, "手机", lsDataX1));
        //钟表
        List<LabelSecond> lsDataX2 = new ArrayList<>();
        lsDataX2.add(new LabelSecond(1, "卡西欧"));
        lsDataX2.add(new LabelSecond(2, "苹果"));
        lsDataX2.add(new LabelSecond(3, "华为"));
        lsDataX2.add(new LabelSecond(4, "三星"));
        lsDataX2.add(new LabelSecond(5, "MOTO"));
        lsDataX2.add(new LabelSecond(6, "Fitbit"));
        lsDataX2.add(new LabelSecond(7, "普耐尔"));
        lfData.add(new LabelFirst(12, "钟表", lsDataX2));

    }

    private void init() {
        lvFirst = (CommentListView) findViewById(R.id.sl_first_list);
        lvSecond = (CommentListView) findViewById(R.id.sl_second_list);

        lvFirst.setAdapter(new GeneralAdapter<LabelFirst>(this,lfData,android.R.layout.simple_list_item_1) {
            @Override
            public void convert(ViewsHolder holder, LabelFirst item, int position) {
                holder.setText(android.R.id.text1, item.getLfName());
            }
        });

        setDefaultSelect();

        lvFirst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                lfCode = lfData.get(position).getLfCode();
                lfName = lfData.get(position).getLfName();
                List<LabelSecond> lfSecondList = lfData.get(position).getLfSecondList();
                lvSecond.setAdapter(new GeneralAdapter<LabelSecond>(SelectLabel.this,lfSecondList,android.R.layout.simple_list_item_1) {
                    @Override
                    public void convert(ViewsHolder holder, LabelSecond item, int position) {
                        holder.setText(android.R.id.text1, item.getLsName());
                    }
                });
                lvSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                        int lsCode = lfData.get(position).getLfSecondList().get(position2).getLsCode();
                        String lsName = lfData.get(position).getLfSecondList().get(position2).getLsName();
                        Log.e("test", "第一 " + lfCode + " -- " + lfName + "\n第二 " + lsCode + " -- " + lsName);
                        Intent intent = new Intent();
                        intent.putExtra("lfCode", lfCode + "");
                        intent.putExtra("lfName", lfName);
                        intent.putExtra("lsCode", lsCode + "");
                        intent.putExtra("lsName", lsName);
                        setResult(102, intent);
                        finish();
                    }
                });
            }
        });





    }

    private void setDefaultSelect() {
        lfCode = lfData.get(0).getLfCode();
        lfName = lfData.get(0).getLfName();
        List<LabelSecond> lfSecondList = lfData.get(0).getLfSecondList();
        lvSecond.setAdapter(new GeneralAdapter<LabelSecond>(SelectLabel.this,lfSecondList,android.R.layout.simple_list_item_1) {
            @Override
            public void convert(ViewsHolder holder, LabelSecond item, int position) {
                holder.setText(android.R.id.text1, item.getLsName());
            }
        });
        lvSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                int lsCode = lfData.get(0).getLfSecondList().get(position2).getLsCode();
                String lsName = lfData.get(0).getLfSecondList().get(position2).getLsName();
                Log.e("test", "第一 " + lfCode + " -- " + lfName + "\n第二 " + lsCode + " -- " + lsName);
                Intent intent = new Intent();
                intent.putExtra("lfCode", lfCode + "");
                intent.putExtra("lfName", lfName);
                intent.putExtra("lsCode", lsCode + "");
                intent.putExtra("lsName", lsName);
                setResult(102, intent);
                finish();
            }
        });
    }
}
