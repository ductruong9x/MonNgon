package com.appfree.monngonvietnam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.appfree.monngonvietnam.adapter.AdapterMonAn;
import com.appfree.monngonvietnam.model.MonAnChiTiet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;


public class MyActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBar actionBar;
    private ListView lvLietKe;
    private ArrayList<MonAnChiTiet> listMonAn = new ArrayList<MonAnChiTiet>();
    private SQLiteDatabase db;
    private AdapterMonAn adapter;
    private SearchView mSearchView;
    private String UNIT_ID="ca-app-pub-1857950562418699/3254233562";
    private InterstitialAd interstitialAd;

    private AdView adView;
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

     //   StartAppAd.showSplash(this, savedInstanceState);
        setContentView(R.layout.activity_my);
        adView=(AdView)findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId(UNIT_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        danhGia();

        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9c27b0")));
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff' size='25'>" + getString(R.string.app_name) + "</font>"));

        lvLietKe = (ListView) findViewById(R.id.lvListContent);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        checkAndCreateDatabase();
        lvLietKe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonAnChiTiet monan = null;
                monan = (MonAnChiTiet) lvLietKe.getItemAtPosition(position);
                int id_mon = monan.getID();
                Intent iChiTiet = new Intent();
                iChiTiet.setClass(getApplication(), DetailActivity.class);
                iChiTiet.putExtra("ID", id_mon);
                startActivity(iChiTiet);
            }
        });
    }

    private void checkAndCreateDatabase() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean firstUse = sharedPreferences.getBoolean("firstUse", true);
        if (firstUse) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstUse", true);
            editor.commit();
            saveDataBase();
        } else {
            loadDuLieu();
        }
    }

    public void saveDataBase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.show();
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                progressDialog.cancel();
                loadDuLieu();
            }
        };
        new Thread() {
            public void run() {
                try {
                    copyDatabase(getApplicationContext());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();

    }


    public void loadDuLieu() {
        String pName = getPackageName();
        String folder = "/data/data/" + pName + "/databases/";
        String dbPath = folder + "database.db";
        Log.v("duong dan", dbPath);
        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        // Cursor cs = db
        // .query("CHITIETMONAN", null, null, null, null, null, null);
        String sql = "SELECT * FROM CHITIETMONAN ORDER BY ID DESC";
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
            Collections.shuffle(listMonAn);
        }
        cs1.close();
        db.close();
        actionBar.setTitle(
                Html.fromHtml("<font color='#ffffff' size='25'>" + "Tất cả"
                        + "(" + listMonAn.size() + ")" + "</font>")
        );
        adapter = new AdapterMonAn(listMonAn, MyActivity.this);

        lvLietKe.setAdapter(adapter);
    }


    private void copyDatabase(Context context) throws IOException {
        String pName = getPackageName();
        String folder = "/data/data/" + pName + "/databases/";
        File CheckDirectory;
        CheckDirectory = new File(folder);
        if (!CheckDirectory.exists()) {
            CheckDirectory.mkdir();
        }
        File file = new File(folder, "database.db");
        if (!file.exists()) {
            Log.w("dsads", " not exist");
            try {
                file.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else {
            Log.w("dsads", "exist");
            file.delete();
            file.createNewFile();
        }
        OutputStream databaseOutputStream = new FileOutputStream(folder
                + "database.db");
        InputStream databaseInputStream;

        byte[] buffer = new byte[1024];
        int length;

        databaseInputStream = context.getResources().openRawResource(
                R.raw.database);
        while ((length = databaseInputStream.read(buffer)) > 0) {
            databaseOutputStream.write(buffer);
        }
        databaseInputStream.close();

        databaseInputStream.close();
        databaseOutputStream.flush();
        databaseOutputStream.close();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position,String name) {
        String ten = name;
        switch (position) {
            case 0:
                listMonAn.removeAll(listMonAn);
                loadDuLieu();
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 1:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'BA'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 2:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'CA'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 3:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'CH'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 4:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'CHI'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 5:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'HA'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 6:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'KH'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 7:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'NU'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 8:

                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'ST'");
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );

                break;
            case 9:
                actionBar.setTitle(
                        Html.fromHtml("<font color='#ffffff' size='25'>"
                                + ten + "(" + listMonAn.size() + ")"
                                + "</font>")
                );
                listMonAn.removeAll(listMonAn);
                loadDuLieuTheoCachNau("'XA'");

                break;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
                if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                }
                break;
            case R.id.moreApp:
                Intent goMoreApp = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri
                                .parse("https://play.google.com/store/apps/developer?id=.FreeVN"));
                startActivity(goMoreApp);
                break;
            case R.id.danhgia:
                Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
                        .parse("market://details?id=" + getPackageName()));
                startActivity(goToMarket);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent iSearch = new Intent(getApplication(),SearchActivity.class);
                iSearch.putExtra("SEARCH", query);
                startActivity(iSearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return true;
    }

    public void loadDuLieuTheoCachNau(String dieukien) {
        String pName = getPackageName();
        String folder = "/data/data/" + pName + "/databases/";
        String dbPath = folder + "database.db";
        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READONLY);
        String sql = "SELECT * FROM CHITIETMONAN WHERE MAKIEU=" + dieukien
                + " ORDER BY ID DESC";
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
        adapter = new AdapterMonAn(listMonAn, MyActivity.this);
        lvLietKe.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MyActivity.this);
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setTitle(R.string.title);
        dialog.setMessage(R.string.content);
        dialog.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                interstitialAd.show();
            }
        });
        dialog.setNeutralButton(R.string.more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
                        .parse("market://details?id=com.gamefree.choosecolor"));
                startActivity(goToMarket);

            }
        });
        dialog.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }
    public void danhGia() {
        SharedPreferences getPre = getSharedPreferences("setting", MODE_PRIVATE);
        int i = getPre.getInt("VOTE", 0);
        SharedPreferences pre;
        SharedPreferences.Editor edit;
        switch (i) {
            case 0:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", 1);
                edit.commit();
                break;
            case 1:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 2:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 3:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 4:
                pre = getSharedPreferences("setting", MODE_PRIVATE);
                edit = pre.edit();
                edit.putInt("VOTE", i + 1);
                edit.commit();
                break;
            case 5:
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("Vote Application");
                dialog.setMessage("You can vote for Quick Reboot");
                dialog.setIcon(R.drawable.ic_launcher);
                dialog.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse("market://details?id="
                                        + getPackageName()));
                        startActivity(goToMarket);
                        SharedPreferences pre = getSharedPreferences("setting", MODE_PRIVATE);
                        SharedPreferences.Editor edit = pre.edit();
                        edit.putInt("VOTE",6);
                        edit.commit();
                    }
                });
                dialog.setNeutralButton("Do not show",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pre = getSharedPreferences("setting",
                                MODE_PRIVATE);
                        SharedPreferences.Editor edit = pre.edit();
                        edit.putInt("VOTE", 6);
                        edit.commit();
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("Later",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
                break;
        }
    }
}
