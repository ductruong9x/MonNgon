package com.appfree.monngonvietnam.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appfree.monngonvietnam.R;

public class AdapterDanhMuc extends ArrayAdapter {
	Activity context=null;
	int layout;
	int[] anhdanhmuc;
	String[] tendanhmuc;

	public AdapterDanhMuc(Activity context, int layout, String[] tendanhmuc,int[] anhdanhmuc) {
		super(context, layout, tendanhmuc);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.layout=layout;
		this.tendanhmuc=tendanhmuc;
		this.anhdanhmuc=anhdanhmuc;
	}

//cvbcbcv
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater inflater = context.getLayoutInflater();
		convertView=inflater.inflate(R.layout.layout_list_menu, null);
		TextView tv=(TextView)convertView.findViewById(R.id.tvTenDanhMuc);
		ImageView img=(ImageView)convertView.findViewById(R.id.imgAnhDanhMuc);
		tv.setText(tendanhmuc[position]);
		img.setImageResource(anhdanhmuc[position]);
		return convertView;
	}

}
