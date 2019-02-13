package com.example.fanfeng.recyclerheaddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.RecyclerView rvShow;
    private List<ClassBean> mListClass = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.rvShow = (RecyclerView) findViewById(R.id.rvShow);

        for (int i = 1; i < 4; i++) {


            List<String> studentName = new ArrayList<>();

            for (int j = 1; j < 56; j++) {
                studentName.add(i + "班 学生" + j);
            }

            ClassBean bean = new ClassBean();
            bean.className = "二年级" + i + "班";

            bean.classStudents = studentName;

            mListClass.add(bean);

        }

//        rvShow.setLayoutManager(new GridLayoutManager(this, 3));
        rvShow.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter mWrapper = new MyAdapter(this, mListClass);

        rvShow.setAdapter(mWrapper);
    }
}
