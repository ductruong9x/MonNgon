package com.appfree.monngonvietnam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class DetailActivity extends Activity {
    private TextView tvTenMonAn, tvNguyenLieu, tvCachLam, tvViTri;
    private ImageView imgAnhMonAn;
    private ActionBar actionBar;
    private SQLiteDatabase db;

    private ImageLoader imvLoader;
    private DisplayImageOptions options;
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
        setContentView(R.layout.activity_detail);

        adView=(AdView)findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#9c27b0")));
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(
                Html.fromHtml("<font color='#ffffff'>Chi Tiết Món Ăn</font>"));
        tvTenMonAn = (TextView) findViewById(R.id.tvTenChiTiet);
        tvNguyenLieu = (TextView) findViewById(R.id.tvNguyenLieu);
        tvCachLam = (TextView) findViewById(R.id.tvCachLam);
        imgAnhMonAn = (ImageView) findViewById(R.id.imgMonAn);
        Intent iChiTiet = getIntent();
        int id = iChiTiet.getIntExtra("ID", 0);
        String idDieuKien = id + "";
        loadDuLieu(idDieuKien);
    }

    public void loadDuLieu(String str) {
        String pName = getPackageName();
        String folder = "/data/data/" + pName + "/databases/";
        String dbPath = folder + "database.db";
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.update)
                .showImageOnFail(R.drawable.error).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
                .build();
        imvLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getBaseContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
        db = SQLiteDatabase.openDatabase(dbPath, null,
                SQLiteDatabase.OPEN_READONLY);

        String sql = "SELECT * FROM CHITIETMONAN WHERE ID=" + str;
        Cursor cs1 = db.rawQuery(sql, null);
        while (cs1.moveToNext()) {

            String name = cs1.getString(cs1.getColumnIndexOrThrow("TENMON"));
            String image = cs1.getString(cs1.getColumnIndexOrThrow("IMAGE"));
            String nguyenlieu = cs1.getString(cs1
                    .getColumnIndexOrThrow("NGUYENLIEU"));
            String congthuc = cs1.getString(cs1
                    .getColumnIndexOrThrow("CONGTHUC"));
            tvTenMonAn.setText(name);
            tvNguyenLieu.setText(nguyenlieu);
            tvCachLam.setText(congthuc);
            actionBar.setTitle(
                    Html.fromHtml("<font color='#ffffff' size='25'>" + name
                            + "</font>")
            );
            ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    // TODO Auto-generated method stub

                }
            };
            imvLoader.displayImage(image, imgAnhMonAn, options,
                    imageLoadingListener);

        }
        cs1.close();
        db.close();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
