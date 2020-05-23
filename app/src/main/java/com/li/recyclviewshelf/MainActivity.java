package com.li.recyclviewshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.li.mylibrary.dialog.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_shelf)
    RecyclerView rvShelf;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_del)
    TextView tvDel;
    @BindView(R.id.tv_search_shelf)
    TextView tvSearchShelf;
    private int width;
    private int type = 0;
    AddShelfListAdapter addShelfListAdapter;
    List<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        strings.add("6");
        strings.add("7");
        strings.add("8");

        ViewTreeObserver viewTreeObserver = rvShelf.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rvShelf.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = rvShelf.getWidth();
            }

        });
    }

    @OnClick({R.id.tv_add, R.id.tv_del, R.id.tv_add1, R.id.tv_del1, R.id.rl_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                if (type != 0) {
                    RefrashAdpter(type, 1);
                } else
                    Toast.makeText(this, "请选择", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_del:
                if (type != 0) {
                    RefrashAdpter(type, 0);
                } else
                    Toast.makeText(this, "请选择", Toast.LENGTH_SHORT).show();

                break;

            case R.id.rl_choose:
                customDialog1(strings);
                break;
            case R.id.tv_add1:
                if (type != 0) {
                    MyShelf myShelf = addShelfListAdapter.getmData().get(addShelfListAdapter.getSelectedPos());
                    int length = myShelf.getLength();
                    if (length > 1) {
                        myShelf.setLength(length - 1);
                        UpdateSize(myShelf);
                    }
                } else
                    Toast.makeText(this, "请选择", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_del1:
                if (type != 0) {
                    MyShelf myShelf = addShelfListAdapter.getmData().get(addShelfListAdapter.getSelectedPos());
                    int length = myShelf.getLength();
                    if (length < 8) {
                        myShelf.setLength(length + 1);
                        UpdateSize(myShelf);
                    }
                } else
                    Toast.makeText(this, "请选择", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void customDialog1(List<String> strings) {
        final BaseDialog baseDialog = new BaseDialog.Builder(this)
                .setContentView(R.layout.dialog_menu)
                .setPercentWidthAndHeight(85,40)
                .create() ;
        RecyclerView rv_menu = baseDialog.findViewById(R.id.rv_menu);
        MenuAdapter1 menuAdapter = new MenuAdapter1(this, strings);
        rv_menu.setLayoutManager(new LinearLayoutManager(this));
        rv_menu.setAdapter(menuAdapter);
        menuAdapter.SetOnclickLister(new MenuAdapter1.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                switch (position) {
                    case 0:
                        tvSearchShelf.setText("6");
                        type = 6;
                        break;
                    case 1:
                        tvSearchShelf.setText("7");
                        type = 7;
                        break;
                    case 2:
                        tvSearchShelf.setText("8");
                        type = 8;
                        break;
                }
                RefrashAdpter(type, 0);
                baseDialog.dismiss();
            }
        });
        baseDialog.show();
    }

    private void UpdateSize(MyShelf myShelf) {
        if (addShelfListAdapter != null) {
            addShelfListAdapter.update(myShelf);
        }

    }

    private void RefrashAdpter(int pos, int mType) {
        if (mType == 0) {//普通刷新
            addShelfListAdapter = new AddShelfListAdapter(this, getMySelf(pos), 0, width);
        } else if (mType == 1) {//合并刷新
            int pos1 = addShelfListAdapter.getSelectedPos();
            int size = addShelfListAdapter.getItemCount();
            List<MyShelf> myShelves = addShelfListAdapter.getmData();
            List<MyShelf> newShelves = getMySelf(pos1, size, myShelves);
            if (newShelves != null) {
                addShelfListAdapter = new AddShelfListAdapter(this, newShelves, pos1, width);
            }

        }
        LinearLayoutManager exceptionLayoutManager = new LinearLayoutManager(this);
        exceptionLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvShelf.setLayoutManager(exceptionLayoutManager);
        rvShelf.setAdapter(addShelfListAdapter);
        addShelfListAdapter.SetOnclickLister(new AddShelfListAdapter.OnItemClickListerner() {
            @Override
            public void onClick(View view, int position) {

            }
        });

    }


    public List<MyShelf> getMySelf(int pos) {
        List<MyShelf> myShelves = new ArrayList<>();
        for (int i = 0; i < pos; i++) {
            MyShelf myShelf = new MyShelf();
            myShelf.setSize(1);
            myShelf.setLength(8);
            myShelves.add(myShelf);
        }
        return myShelves;
    }


    public List<MyShelf> getMySelf(int pos, int size, List<MyShelf> myShelves) {

        MyShelf myShelf = myShelves.get(pos);
        if (pos != size - 1) {
            int oneSize = myShelf.getSize();
            for (int i = pos + 1; i < size; i++) {
                MyShelf myShelf2 = myShelves.get(i);
                int twoSize = myShelf2.getSize();
                if (twoSize != 0) {
                    myShelf.setSize(oneSize + twoSize);
                    myShelf2.setSize(0);
                    myShelves.set(pos, myShelf);
                    myShelves.set(pos + 1, myShelf2);
                    return myShelves;
                }
            }
        }
        return null;
    }
}
