package cn.leeq.util.memodemo.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.MyApp;
import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;
import cn.leeq.util.memodemo.bean.MemoBean;
import cn.leeq.util.memodemo.widget.CommentListView;

public class XUtilsDBDemo extends AppCompatActivity implements AdapterView.OnItemLongClickListener, Toolbar.OnMenuItemClickListener {

    private DbManager db;
    private List<MemoBean> data = new ArrayList<>();
    private CommentListView listView;
    private GeneralAdapter<MemoBean> adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xutils_dbdemo);
        setTitle("");
        init();

        loadData();
    }

    private void loadData() {
        try {
            List<MemoBean> all = db.findAll(MemoBean.class);
            if (all != null) {
                data.addAll(all);
                adapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        DbManager.DaoConfig daoconfig = MyApp.getInstance().getDaoconfig();
        db = x.getDb(daoconfig);
        listView = (CommentListView) findViewById(R.id.xd_listview);
        adapter = new GeneralAdapter<MemoBean>(this,data,R.layout.item_xdb) {
            @Override
            public void convert(ViewsHolder holder, MemoBean item, int position) {
                holder.setText(R.id.item_xdb_tv_age, item.getAge()+" 岁");
                holder.setText(R.id.item_xdb_tv_name, item.getName());
                holder.setText(R.id.item_xdb_tv_signture, item.getSignture());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.xd_toolbar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setOnMenuItemClickListener(this);
    }

    public void dataBase(View view) {
        switch (view.getId()){
            case R.id.xd_btn_add:
                final View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_add_memo, null);
                new AlertDialog.Builder(this)
                        .setTitle("添加")
                        .setView(dialogView)
                        .setCancelable(false)
                        .setNegativeButton("取消",null)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addSomePerson(dialogView);
                            }
                        }).create().show();
                break;
            case R.id.xd_btn_clear:
                try {
                    db.dropTable(MemoBean.class);
                    data.clear();
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void addSomePerson(View dialogView) {
        EditText etAge = (EditText) dialogView.findViewById(R.id.dialog_et_age);
        EditText etName = (EditText) dialogView.findViewById(R.id.dialog_et_name);
        EditText etSigntrue = (EditText) dialogView.findViewById(R.id.dialog_et_signture);

        if (TextUtils.isEmpty(etAge.getText()) || TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etSigntrue.getText())) {
            Toast.makeText(XUtilsDBDemo.this, "请补充完整", Toast.LENGTH_SHORT).show();
            return;
        }

        String getAge = etAge.getText().toString();
        String getName = etName.getText().toString();
        String getSign = etSigntrue.getText().toString();

        MemoBean memoBean = new MemoBean();
        memoBean.setName(getName);
        memoBean.setAge(getAge);
        memoBean.setSignture(getSign);
        try {
            db.save(memoBean);
            data.add(memoBean);
            adapter.notifyDataSetChanged();

        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定删除此项吗?")
                .setCancelable(false)
                .setNeutralButton("忽略",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.deleteById(MemoBean.class,position);
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }).create().show();
        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);
        return true;
    }


}
