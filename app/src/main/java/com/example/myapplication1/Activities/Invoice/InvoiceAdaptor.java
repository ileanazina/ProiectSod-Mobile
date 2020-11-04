package com.example.myapplication1.Activities.Invoice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Activities.Payments;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.R;
import java.text.SimpleDateFormat;
import java.util.List;

public class InvoiceAdaptor extends RecyclerView.Adapter<InvoiceAdaptor.ExempleViewHolder> {

    private Context mContext;
    private List<InvoiceModel> mList;
    private OnInvoiceListener monInvoiceListener;

    public InvoiceAdaptor(Context mContext, List<InvoiceModel> mList,OnInvoiceListener onInvoiceListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.monInvoiceListener=onInvoiceListener;
    }

    @NonNull
    @Override
    public ExempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.invoice_row_view,parent,false);
        return new ExempleViewHolder(v,monInvoiceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExempleViewHolder holder, int position) {
        InvoiceModel item = mList.get(position);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dataDue = formatter.format(item.getDueDate());
            //String aux = "Numar facura: " + item.getInvoiceId() + "\nTotal de plata: " + item.getValueWithVat() + "\nData scadenta: " + dataDue;
            holder.tvInvoiceNr.setText("Numar facura: " + item.getInvoiceId());
            holder.tvTotalPay.setText("Total de plata: " + item.getValueWithVat());
            holder.tvDueDate.setText("Data scadenta: " + dataDue);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ExempleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvInvoiceNr,tvTotalPay, tvDueDate;
        public Button viewInfo,payInvoice;
        OnInvoiceListener onInvoiceListener;

        public ExempleViewHolder(@NonNull View itemView, OnInvoiceListener onInvoiceListener) {
            super(itemView);
            tvInvoiceNr = itemView.findViewById(R.id.invoiceNr);
            tvTotalPay = itemView.findViewById(R.id.toaltoPay);
            tvDueDate = itemView.findViewById(R.id.dataDue);

            viewInfo = itemView.findViewById(R.id.Viewbtn);
            payInvoice = itemView.findViewById(R.id.Paybtn);

            viewInfo.setOnClickListener(this);
            payInvoice.setOnClickListener(this);

            this.onInvoiceListener=onInvoiceListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onInvoiceListener.onInvoiceListener(getAdapterPosition());
            int position;
            position = getAdapterPosition();
            InvoiceModel item = mList.get(position);

            switch (v.getId()) {
                case R.id.Viewbtn:
                    viewInvoice(item.getInvoiceId());
                    break;
                case R.id.Paybtn:
                    paingInvoice(item.getInvoiceId());
                    break;
            }
        }

        private void viewInvoice(final int id)
        {
            Intent intentDetais = new Intent(mContext , InvoiceDetails.class);
            intentDetais.putExtra("extra", id);
            intentDetais.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentDetais);
        }

        private void paingInvoice(final int id)
        {
            Intent intentPay = new Intent(mContext , Payments.class);
            intentPay.putExtra("extra", id);
            intentPay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentPay);
        }
    }

    public interface OnInvoiceListener {
        void onInvoiceListener(int position);
    }
}
