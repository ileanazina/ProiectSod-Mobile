package com.example.myapplication1.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.R;

import java.util.List;

public class InvoiceAdaptor extends RecyclerView.Adapter<InvoiceAdaptor.ExempleViewHolder> {
    private Context mContext;
    private List<InvoiceModel> mList;
    private OnInvoiceListener monInvoiceListener;

    public InvoiceAdaptor(Context mContext, List<InvoiceModel> mList, OnInvoiceListener onInvoiceListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.monInvoiceListener=onInvoiceListener;
    }

    @NonNull
    @Override
    public ExempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.invoice_recycle_view, parent, false);
        return new ExempleViewHolder(v,monInvoiceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExempleViewHolder holder, int position) {
        InvoiceModel item = mList.get(position);
        String aux = "Numar factura: " + item.getInvoiceId() + "\nTotal de plata: " + item.getValueWithVat() + "\nEmisa la data: " + item.getDueDate();
        holder.textView.setText(aux);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ExempleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        OnInvoiceListener onInvoiceListener;

        public ExempleViewHolder(@NonNull View itemView,OnInvoiceListener onInvoiceListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.invoiceRecycleView);
            this.onInvoiceListener=onInvoiceListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onInvoiceListener.onInvoiceListener(getAdapterPosition());
        }
    }
    public interface OnInvoiceListener {
        void onInvoiceListener(int position);
    }
}
