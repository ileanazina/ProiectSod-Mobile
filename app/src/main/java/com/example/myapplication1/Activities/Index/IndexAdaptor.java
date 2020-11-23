package com.example.myapplication1.Activities.Index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Model.IndexModel;
import com.example.myapplication1.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class IndexAdaptor extends RecyclerView.Adapter<IndexAdaptor.ExempleViewHolder> {

    private Context mContext;
    private List<IndexModel> indexList;
    private IndexAdaptor.OnIndexListener monIndexListener;

    public IndexAdaptor(Context mContext, List<IndexModel> mList, IndexAdaptor.OnIndexListener onIndexListener) {
        this.mContext = mContext;
        this.indexList = mList;
        this.monIndexListener=onIndexListener;
    }

    @NonNull
    @Override
    public IndexAdaptor.ExempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.index_row_view,parent,false);
        return new IndexAdaptor.ExempleViewHolder(v,monIndexListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IndexAdaptor.ExempleViewHolder holder, int position) {

        IndexModel item = indexList.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dataDue = formatter.format(item.getIndexDate());
        holder.tvIndexNr.setText(mContext.getResources().getString(R.string.index_adaptor_value) + item.getIndexValue());
        holder.tvIndexDate.setText(mContext.getResources().getString(R.string.index_adaptor_date) + dataDue);
        holder.tvIndexavr.setText(mContext.getResources().getString(R.string.index_adaptor_consumption) + item.getAverageIndex());
    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }

    public class ExempleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvIndexNr,tvIndexDate, tvIndexavr;
        IndexAdaptor.OnIndexListener onIndexListener;

        public ExempleViewHolder(@NonNull View itemView, IndexAdaptor.OnIndexListener onIndexListener) {
            super(itemView);
            tvIndexNr = itemView.findViewById(R.id.indexValue);
            tvIndexDate = itemView.findViewById(R.id.indexDate);
            tvIndexavr = itemView.findViewById(R.id.indexAvr);

            this.onIndexListener=onIndexListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onIndexListener.onIndexListener(getAdapterPosition());
        }
    }

    public interface OnIndexListener {
        void onIndexListener(int position);
    }
}
