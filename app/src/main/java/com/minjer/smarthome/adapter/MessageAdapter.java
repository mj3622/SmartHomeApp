package com.minjer.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.minjer.smarthome.R;
import com.minjer.smarthome.pojo.Message;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private int resourceLayout;
    private Context mContext;

    public MessageAdapter(Context context, int resource, List<Message> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            view = vi.inflate(resourceLayout, null);
        }

        Message m = getItem(position);

        if (m != null) {
            TextView titleTextView = view.findViewById(R.id.message_title);
            TextView contentTextView = view.findViewById(R.id.message_context);
            TextView timeTextView = view.findViewById(R.id.message_time);

            if (titleTextView != null) {
                titleTextView.setText(m.getTitle());
            }
            if (contentTextView != null) {
                contentTextView.setText(m.getContent());
            }
            if (timeTextView != null) {
                timeTextView.setText(m.getTime());
            }
        }

        return view;
    }
}
