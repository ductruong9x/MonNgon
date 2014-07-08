package com.appfree.monngonvietnam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appfree.monngonvietnam.R;
import com.appfree.monngonvietnam.model.MonAnChiTiet;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

public class AdapterMonAn extends BaseAdapter {
	private  ArrayList<MonAnChiTiet> listItem;
	private Context mcontext;
	private LayoutInflater mLayoutInflater;
	private ImageLoader imvLoader;
	private DisplayImageOptions options;

	public AdapterMonAn(ArrayList<MonAnChiTiet> list, Context context) {
		super();
		listItem = list;
		mLayoutInflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.update)
				.showImageOnFail(R.drawable.error).cacheInMemory(true)
				.cacheOnDisc(false).displayer(new RoundedBitmapDisplayer(3))
				.build();
		imvLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {
		return listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.layout_list_monan,
					null);
			holder = new ViewHolder();
			//holder.tvID = (TextView) convertView.findViewById(R.id.tvID);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvTenMonAn);
			holder.imv = (ImageView) convertView
					.findViewById(R.id.imgListMonAn);
            holder.loading=(ProgressBar)convertView.findViewById(R.id.loading);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//holder.tvID.setText(listItem.get(position).getID() + "");
		holder.tvName.setText(listItem.get(position).getTenMonAn().toString());
		ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
                holder.loading.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				// TODO Auto-generated method stub
                holder.loading.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub

			}
		};
		imvLoader.displayImage(listItem.get(position).getAnhMonAn(),
				holder.imv, options, imageLoadingListener);

		return convertView;
	}

	private class ViewHolder {
		ImageView imv;
		TextView tvName;
        ProgressBar loading;
	}

}
