package com.wangjie.rapidorm.example;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.rapidorm.example.database.DatabaseFactory;
import com.wangjie.rapidorm.example.database.dao.PersonDaoImpl;
import com.wangjie.rapidorm.example.database.model.Person;

import java.sql.SQLException;
import java.util.List;

@AILayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @AIView(R.id.activity_main_db_data_list_tv)
    private TextView dataListTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseFactory.getInstance().resetDatabase("hello_rapid_orm.db");
        queryAll();

    }


    @Override
    @AIClick({R.id.activity_main_insert_btn, R.id.activity_main_insert_performance_btn,
            R.id.activity_main_update_btn, R.id.activity_main_delete_btn,
            R.id.activity_main_delete_all_btn, R.id.activity_main_query_by_builder_btn,
            R.id.activity_main_update_by_builder_btn, R.id.activity_main_delete_by_builder_btn
    })
    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.activity_main_insert_btn:
                insert();
                queryAll();
                break;
            case R.id.activity_main_insert_performance_btn:
                insertPerformance();
                break;
            case R.id.activity_main_update_btn:
                update();
                queryAll();
                break;
            case R.id.activity_main_delete_btn:
                delete();
                queryAll();
                break;
            case R.id.activity_main_delete_all_btn:
                deleteAll();
                queryAll();
                break;

            case R.id.activity_main_query_by_builder_btn:
                queryByBuilder();
                break;
            case R.id.activity_main_update_by_builder_btn:
                updateByBuilder();
                queryAll();
                break;
            case R.id.activity_main_delete_by_builder_btn:
                deleteByBuilder();
                queryAll();
                break;

            default:
                break;
        }
    }

    private void deleteByBuilder() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).deletePerson();
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }
    }

    private void updateByBuilder() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).updatePerson();
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }
    }

    private void queryByBuilder() {
        try {
            List<Person> personList = DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).findPersons();
            dataListTv.setText("query by builder" + personList.toString());
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }
    }

    private void deleteAll() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).deleteAll();
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }
    }

    private void insertPerformance() {
        Log.d(TAG, "insertPerformance start...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteAll();
                long start = System.currentTimeMillis();
                for (int i = 0; i < 5000; i++) {
                    insert();
                }
                Log.i(TAG, "insert performance time: " + (System.currentTimeMillis() - start));
                deleteAll();
            }
        }).start();
    }


    private void insert() {
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).insert(getPerson());
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }
    }

    private void update() {
        showToastMessage("update");
        Person p = new Person();
        p.setId(100);
        p.setTypeId(1);
        p.setName("wangjie_modified");
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).update(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        showToastMessage("delete");
        Person p = new Person();
        p.setId(100);
        p.setTypeId(1);
        try {
            DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).delete(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Person getPerson() {
        Person person = new Person();
        person.setId(100);
        person.setTypeId(1);
        person.setName("wangjie");
        person.setAge(29);
        person.setBirth(System.currentTimeMillis());
        person.setAddress("address");
        return person;
    }

    private void queryAll() {
        try {
            List<Person> personList = DatabaseFactory.getInstance().getDao(PersonDaoImpl.class).queryAll();
            dataListTv.setText("all data" + personList.toString());
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
