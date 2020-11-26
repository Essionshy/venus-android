package com.tingyu.venus.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tingyu.venus.R;
import com.tingyu.venus.controller.fragment.ContactFragment;
import com.tingyu.venus.controller.fragment.MeFragment;
import com.tingyu.venus.controller.fragment.MessageFragment;
import com.tingyu.venus.fragment.IndexFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ImageView ibIndex;
    private ImageView ibNews;
    private ImageView ibFound;
    private ImageView ibMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //设置标题不显示
        initView();
    }



    private void initView() {

        ibIndex = findViewById(R.id.ib_index);
        ibNews = findViewById(R.id.ib_news);
        ibFound = findViewById(R.id.ib_found);
        ibMe = findViewById(R.id.ib_me);

        //为ImageButton分别绑定事件监听器
        ibIndex.setOnClickListener(clickListener);
        ibNews.setOnClickListener(clickListener);
        ibFound.setOnClickListener(clickListener);
        ibMe.setOnClickListener(clickListener);

        fragmentManager = getSupportFragmentManager();
        //初始加载IndexFragment
        IndexFragment indexFragment = new IndexFragment();
        fragmentManager.beginTransaction().add(R.id.fragment, indexFragment).commitAllowingStateLoss();
    }

    //创建ImageButton单击事件监听器

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //获取FragmentManager
            //获取FragmentTransaction
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment f = null;
            switch (v.getId()) {
                case R.id.ib_index:
                    f = new IndexFragment();
                    break;

                case R.id.ib_news:
                    f = new ContactFragment();

                    break;
                case R.id.ib_found:
                    f = new MessageFragment();

                    break;
                case R.id.ib_me:
                    f = new MeFragment();

                    break;
                default:
                    break;
            }

            ft.replace(R.id.fragment, f);
            //提交事务
            ft.commit();


        }
    };

    /**
     * 加载菜单资源文件
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                break;

            case R.id.menu_add_contact:
                startActivity(new Intent(MainActivity.this, ContactAddActivity.class));
                break;

            case R.id.menu_about:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();

                break;

            case R.id.menu_settings:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();

                break;
            case R.id.menu_create_group:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, GroupCreateActivity.class));

                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
