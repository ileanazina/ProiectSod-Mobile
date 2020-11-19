package com.example.myapplication1.Activities.Invoice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Activities.Payments;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.R;
import com.example.myapplication1.MainActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class InvoiceAdaptor extends RecyclerView.Adapter<InvoiceAdaptor.ExempleViewHolder>{

    private Context mContext;
    private List<InvoiceModel> mList;
    private OnInvoiceListener monInvoiceListener;
    private AlertDialog.Builder builder;
    private AlertDialog dialogError;

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
            holder.tvInvoiceNr.setText(mContext.getResources().getString(R.string.invoice_adaptor_invoice_number) + item.getInvoiceId());
            holder.tvTotalPay.setText(mContext.getResources().getString(R.string.invoice_adaptor_to_paid) + item.getValueWithVat());
            holder.tvDueDate.setText(mContext.getResources().getString(R.string.invoice_adaptor_due_date) + dataDue);
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
            tvTotalPay = itemView.findViewById(R.id.totalToPay);
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
                    viewInvoice(item);
                    break;
                case R.id.Paybtn:
                    if(!item.isPaid()) {
                        paingInvoice(item);
                    break;
                    }
                    else
                    {
                        showalertDialog();
                        break;
                    }
            }
        }

        private void showalertDialog(){
            TextView textView = new TextView(viewInfo.getRootView().getContext());
            textView.setTextSize(20F);
            textView.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);

            builder = new AlertDialog.Builder(viewInfo.getRootView().getContext());
            builder.setMessage("Factura este deja platita");
            builder.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0,
                                            int arg1) {

                        }
                    });

            builder.show();

        }

        private void viewInvoice( InvoiceModel obj)
        {
            Bundle newBundle = new Bundle();
            newBundle.putSerializable("extra", obj);
            Fragment objects = new InvoiceDetailsFragment();
            objects.setArguments(newBundle);

            ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, objects).addToBackStack(null).commit();
        }

        private void paingInvoice(InvoiceModel obj)
        {
            Intent intentPay = new Intent(mContext , Payments.class);
            intentPay.putExtra("extra", obj);
            intentPay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentPay);
        }
    }

    public interface OnInvoiceListener {
        void onInvoiceListener(int position);
    }
}
