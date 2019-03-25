package com.example.navi_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class location_list_view extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<Location> arrayList = new ArrayList<>();

    private LocationSystem ls;

    public location_list_view(Context context, LocationSystem locationSystem) {
        this.ls = locationSystem;

        this.context = context;
        inflater = LayoutInflater.from(context);

        // Add each location to array list of locations with in the location system
        for(Building building: ls.buildings) {
            for(Level level: building.levels) {
                for(Location location: level.locations) {
                    locations.add(location);
                }
            }
        }

        arrayList.addAll(locations);

    }

    public location_list_view(Context context, ArrayList<Location> locations, LocationSystem locationSystem) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        this.locations.addAll(locations);
        this.arrayList.addAll(locations);
        this.ls = locationSystem;
    }

    public class ViewHolder {
        TextView txt_location_name, txt_location_level, txt_location_building;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Location getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.location_list_view, null);

            holder.txt_location_name = (TextView) view.findViewById(R.id.txt_location_name);
            holder.txt_location_level = (TextView) view.findViewById(R.id.txt_location_level);
            holder.txt_location_building = (TextView) view.findViewById(R.id.txt_location_building);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.txt_location_name.setText(locations.get(position).name);
        //holder.txt_location_level.setText(ls.getCurrentLevel(locations.get(position)).name);
        //holder.txt_location_building.setText(ls.getCurrentBuilding(locations.get(position)).name);
        return view;
    }

    public void filter(String filterText) {
        locations.clear();

        if (filterText.length() == 0) {
            locations.addAll(arrayList);
        } else {
            for (Location location : arrayList) {
                Log.i("filter", location.name.toLowerCase(Locale.getDefault()));
                if (location.name.toLowerCase(Locale.getDefault()).contains(filterText.toLowerCase(Locale.getDefault()))) {
                    this.locations.add(location);
                }
            }
        }
        notifyDataSetChanged();
    }

}
