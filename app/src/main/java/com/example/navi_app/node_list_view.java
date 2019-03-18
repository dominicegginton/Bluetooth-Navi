package com.example.navi_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class node_list_view extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Node> arrayList = new ArrayList<>();

    public node_list_view(Context context, ArrayList<Node> nodes) {

        this.context = context;
        inflater = LayoutInflater.from(context);

        this.nodes = nodes;

        arrayList.addAll(nodes);

    }

    public class ViewHolder {
        TextView txt_location_name, txt_node_address;
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Node getItem(int position) {
        return nodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        node_list_view.ViewHolder holder;

        if (view == null) {

            holder = new node_list_view.ViewHolder();
            view = inflater.inflate(R.layout.location_list_view, null);

            holder.txt_location_name = (TextView) view.findViewById(R.id.txt_location_name);
            holder.txt_node_address = (TextView) view.findViewById(R.id.txt_node_address);
            view.setTag(holder);

        } else {
            holder = (node_list_view.ViewHolder) view.getTag();
        }
        Log.i("xxx", holder.txt_node_address.toString());
        // Set the results into TextViews
        holder.txt_location_name.setText("test");
        holder.txt_node_address.setText(nodes.get(position).address);
        return view;
    }

}