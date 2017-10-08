package com.pragyaware.sarbjit.jkpddapp.mAdaptar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.mActivity.PreviewImageActivity;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sarbjit on 06/27/2017.
 */

public class ViewPagerAdaptar extends PagerAdapter {

    private Context context;
    private ArrayList<String> strings;

    public ViewPagerAdaptar(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(context).inflate(R.layout.viewpager_item, null);
        ImageView photoImgVw = (ImageView) itemView.findViewById(R.id.photoImgVw);
        String Url = Constants.IMG_URL + strings.get(position) + "|300|150|60";
        Picasso.with(context).load(Url).into(photoImgVw);

        photoImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PreviewImageActivity.class).putExtra("data", strings.get(position)));
            }
        });

        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((LinearLayout) object);

    }


}
