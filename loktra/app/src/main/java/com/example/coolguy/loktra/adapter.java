package com.example.coolguy.loktra;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by coolguy on 02-06-2016.
 */
public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
    public ArrayList<content> data = new ArrayList<content>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView personcontainer;
        public TextView messageidcontainer;
        public TextView messagecontainer;

        public ViewHolder(View v) {
            super(v);
            personcontainer = (TextView) v.findViewById(R.id.person);
            messageidcontainer = (TextView) v.findViewById(R.id.commitid);
            messagecontainer = (TextView) v.findViewById(R.id.commitmsg);
        }
    }
    public void add(content item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }
    public adapter(ArrayList<content> set) {
            data = set;
    }
    public adapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public int getItemCount() {
        return data.size();
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.personcontainer.setText("NAME:  "+data.get(position).person);
        holder.messageidcontainer.setText("COMMIT ID:  " +data.get(position).messageid);
        holder.messagecontainer.setText("MESSAGE:  " +data.get(position).message);
    }
}


class content{
    String person,messageid,message ;

    content(String person,String messageid,String message){
        this.person = person;
        this.messageid = messageid;
        this.message = message ;
    }
}