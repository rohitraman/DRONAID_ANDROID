package com.dronaid.dronaid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bennyhawk on 12/21/17.
 */

public class SearchDoctorAdapter extends RecyclerView.Adapter<SearchDoctorAdapter.DoctorViewHolder> {

    private List<Username> list;

    public SearchDoctorAdapter(List<Username> list) {
        this.list = list;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoctorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false));
    }

    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        Username temp = list.get(position);
        holder.docType.setText(temp.getAcctype());
        holder.docUsername.setText(temp.getUsername());
    }

    public void update(List<Username> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView docUsername;
        TextView docType;
        public DoctorViewHolder(View itemView) {
            super(itemView);
            docUsername =  itemView.findViewById(R.id.docUsername);
            docType =  itemView.findViewById(R.id.docType);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

        }
    }


}
