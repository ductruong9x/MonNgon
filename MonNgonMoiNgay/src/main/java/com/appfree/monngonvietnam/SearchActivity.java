package com.appfree.monngonvietnam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appfree.monngonvietnam.adapter.AdapterMonAn;
import com.appfree.monngonvietnam.model.MonAnChiTiet;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.startapp.android.publish.StartAppAd;

import java.util.ArrayList;

public class SearchActivity extends Activity {
    private ListView lvSearch;
    private ArrayList<MonAnChiTiet> listMonAn = new ArrayList<MonAnChiTiet>();
    private SQLiteDatabase db;
    private AdapterMonAn adapter;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_color);
            tintManager.setNavigationBarTintResource(R.color.app_color);
        }
        setContentView(R.layout.activity_search);
        StartAppAd.showSlider(this);
        actionBar=getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9c27b0")));
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff' size='25'>" + getString(R.string.title_activity_search) + "</font>"));
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        searchMonAn();
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int postition, long arg3) {
                // TODO Auto-generated method stub
                MonAnChiTiet monan = null;
                monan = (MonAnChiTiet) lvSearch.getItemAtPosition(postition);
                int id = monan.getID();
                Intent iChiTiet = new Intent();
                iChiTiet.setClass(getApplication(), DetailActivity.class);
                iChiTiet.putExtra("ID", id);
                startActivity(iChiTiet);
            }
        });
    }

    public void searchMonAn() {
        listMonAn.removeAll(listMonAn);
        Intent iSearch = getIntent();
        String query = iSearch.getStringExtra("SEARCH").replace(' ', '%').toLowerCase();
        String pName = this.getClass().getPackage().getName();
        String folder = "/data/data/" + pName + "/databases/";
        String dbPath = folder + "database.db";
        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READONLY);
//		query.;
        String sql = "SELECT * FROM CHITIETMONAN WHERE SEARCH LIKE" + "'%"
                + query + "%'";
        Cursor cs1 = db.rawQuery(sql, null);
        while (cs1.moveToNext()) {
            int id = cs1.getInt(cs1.getColumnIndexOrThrow("ID"));
            String name = cs1.getString(cs1.getColumnIndexOrThrow("TENMON"));
            String image = cs1.getString(cs1.getColumnIndexOrThrow("IMAGE"));
            String nguyenlieu = cs1.getString(cs1
                    .getColumnIndexOrThrow("NGUYENLIEU"));
            String congthuc = cs1.getString(cs1
                    .getColumnIndexOrThrow("CONGTHUC"));
            String makieu = cs1.getString(cs1.getColumnIndexOrThrow("MAKIEU"));
            double vido = cs1.getDouble(cs1.getColumnIndexOrThrow("VIDO"));
            double kinhdo = cs1.getDouble(cs1.getColumnIndexOrThrow("KINHDO"));
            String search = cs1.getString(cs1.getColumnIndexOrThrow("SEARCH"));
            String vungmien = cs1.getString(cs1
                    .getColumnIndexOrThrow("VUNGMIEN"));
            String diachi = cs1.getString(cs1.getColumnIndexOrThrow("DIACHI"));
            MonAnChiTiet monan = new MonAnChiTiet(id, name, image, makieu,
                    nguyenlieu, congthuc, kinhdo, vido, search, vungmien,
                    diachi);
            listMonAn.add(monan);
        }
        cs1.close();
        db.close();
        adapter=new AdapterMonAn(listMonAn,SearchActivity.this);
        lvSearch.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
