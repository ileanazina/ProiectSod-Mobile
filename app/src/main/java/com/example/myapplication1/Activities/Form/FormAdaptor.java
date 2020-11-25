package com.example.myapplication1.Activities.Form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication1.Activities.Index.IndexAdaptor;
import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class FormAdaptor extends RecyclerView.Adapter<FormAdaptor.ExempleViewHolder> {

    private Context mContext;
   private List<IndexModel> indexList;
    private OnIndexListener monIndexListener;

    public FormAdaptor(Context mContext, List<IndexModel> mList, OnIndexListener onIndexListener) {
        this.mContext = mContext;
        this.indexList = mList;
        this.monIndexListener=onIndexListener;
    }

    @NonNull
    @Override
    public ExempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.form_row_view,parent,false);
        return new FormAdaptor.ExempleViewHolder(v,monIndexListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExempleViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return indexList.size();
    }

    public class ExempleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//        public TextView tvIndexNr,tvIndexDate, tvIndexavr;
//        IndexAdaptor.OnIndexListener onIndexListener;

        public ExempleViewHolder(@NonNull View itemView, FormAdaptor.OnIndexListener onIndexListener) {
            super(itemView);
//            tvIndexNr = itemView.findViewById(R.id.indexValue);
//            tvIndexDate = itemView.findViewById(R.id.indexDate);
//            tvIndexavr = itemView.findViewById(R.id.indexAvr);
//
//            this.onIndexListener=onIndexListener;
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            monIndexListener.onIndexListener(getAdapterPosition());
        }
    }

    public interface OnIndexListener {
        void onIndexListener(int position);
    }
}