package com.example.abanoub.emailsystemdemo;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abanoub.emailsystemdemo.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Hassan on 12/4/2017.
 */

class SingleRow {
    String company, title, date,firstline;
         int images;

    public SingleRow(String company ,String title ,String date ,String firstline, int images)
    {
        this.company=company;
        this.date=date;
        this.firstline=firstline;
        this.title=title;
        this.images =images;
    }
}

public class MessageAdapter extends BaseAdapter {
    ArrayList<SingleRow> list;
    Context context;
    public MessageAdapter(Context context)
    {
        this.context=context;
        list=new ArrayList<SingleRow>();
        Resources res =context.getResources();
        String company[]=res.getStringArray(R.array.company);
        String title[]=res.getStringArray(R.array.titles);
        String data[]=res.getStringArray(R.array.date);
        String firstline[]=res.getStringArray(R.array.firstline);
        int images[]={R.drawable.web_hi_res_512,R.drawable.gg,R.drawable.web_hi_res_512,R.drawable.gg,R.drawable.web_hi_res_512,R.drawable.gg,R.drawable.gg};
        for (int i =0 ;i<7;i++)
        {
            list.add(new SingleRow(company[i],title[i],data[i],firstline[i],images[i]));
        }

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);


            TextView Company = (TextView) convertView.findViewById(R.id.Company_name);
            TextView Title = (TextView) convertView.findViewById(R.id.Title);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView firstline = (TextView) convertView.findViewById(R.id.firstLine);
            ImageView images = (ImageView) convertView.findViewById(R.id.imagelist);

            SingleRow fullList =list.get(position);
            Company.setText(fullList.company);
            Title.setText(fullList.title);
            date.setText(fullList.date);
            firstline.setText(fullList.firstline);
            images.setImageResource(fullList.images);
        }
        return convertView;
    }
}
