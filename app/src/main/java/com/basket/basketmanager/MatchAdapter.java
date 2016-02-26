package com.basket.basketmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.basket.basketmanager.model.Match;

import java.util.List;

public class MatchAdapter extends BaseAdapter {

    private List<Match> matchList;
    private View itemView;

    public MatchAdapter(List<Match> matchList) {
        this.matchList = matchList;
    }

    public int getItemCount() {
        return matchList.size();
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return matchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MatchViewHolder viewHolder;
        if (view == null) {
            viewHolder = onCreateViewHolder(viewGroup, i);
            view = itemView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (MatchViewHolder) view.getTag();
        }
        onBindViewHolder(viewHolder, i);
        return view;
    }

    public void onBindViewHolder(MatchViewHolder viewHolder, int i) {
        Match match = matchList.get(i);
        viewHolder.date.setText(match.getDate());
        viewHolder.time.setText(match.getTime());
        viewHolder.place.setText(match.getPlace());
    }

    public MatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.short_match_info, viewGroup, false);
        return new MatchViewHolder(itemView);
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView time;
        TextView place;

        public MatchViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.match_date);
            time = (TextView) view.findViewById(R.id.match_time);
            place = (TextView) view.findViewById(R.id.match_place);
        }
    }
}
