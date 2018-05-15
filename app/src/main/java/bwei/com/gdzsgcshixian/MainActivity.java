package bwei.com.gdzsgcshixian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import bwei.com.gdzsgcshixian.dao.DaoMaster;
import bwei.com.gdzsgcshixian.dao.DaoSession;
import bwei.com.gdzsgcshixian.dao.UserDao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity=====";
    private EditText et_name;
    private EditText et_age;
    private EditText et_id;
    private Button bt_insert;
    private Button bt_select;
    private Button bt_delete;
    private Button bt_update;
    private RecyclerView rlv;
    private MyAdapter adapter;
    private UserDao userDao;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this,"pwd.db",null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();

    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_age = (EditText) findViewById(R.id.et_age);
        et_id = (EditText) findViewById(R.id.et_id);
        bt_insert = (Button) findViewById(R.id.bt_insert);
        bt_insert.setOnClickListener(this);
        bt_select = (Button) findViewById(R.id.bt_select);
        bt_select.setOnClickListener(this);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(this);
        bt_update = (Button) findViewById(R.id.bt_update);
        bt_update.setOnClickListener(this);
        rlv = (RecyclerView) findViewById(R.id.rlv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_insert:
                insert();
                break;
            case R.id.bt_select:
                select();
                break;
            case R.id.bt_delete:
                delete();
                break;
            case R.id.bt_update:
                update();
                break;
            default:
                break;
        }
    }
    private void insert() {
        name = et_name.getText().toString().trim();
        String age = et_age.getText().toString().trim();

        User user = new User(null, name, age);
        userDao.insert(user);
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();

        select();

    }

    private void select() {
        List<User> users = userDao.loadAll();

        for(int i = 0;i<users.size();i++){
            Long id = users.get(i).getId();
            Log.d(TAG, "select: "+id);
        }

        adapter = new MyAdapter(users,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rlv.setLayoutManager(linearLayoutManager);
        rlv.setAdapter(adapter);
    }
    private void delete(){
        String id = et_id.getText().toString().trim();

        if(id.isEmpty()){
            Toast.makeText(this,"请输入ID",Toast.LENGTH_SHORT).show();
        }else{
            userDao.queryBuilder().where(UserDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
            Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
        }
        select();
    }
    private void update(){
        String id = et_id.getText().toString().trim();

        long ids = Long.parseLong(id);

        String s = et_name.getText().toString();

        String age = et_age.getText().toString();

        User user = new User(ids,s,age);

        userDao.update(user);

        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        select();;
    }
}
