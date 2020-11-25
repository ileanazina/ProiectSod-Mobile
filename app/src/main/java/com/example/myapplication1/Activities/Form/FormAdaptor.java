package com.example.myapplication1.Activities.Form;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication1.Model.DocumentDownloadModel;
import com.example.myapplication1.R;

import java.util.List;

public class FormAdaptor extends RecyclerView.Adapter<FormAdaptor.ExempleViewHolder> {

    private Context mContext;
    private List<DocumentDownloadModel> documentList;
    private OnFormListener monFormListener;

    public FormAdaptor(Context mContext, List<DocumentDownloadModel> mList, OnFormListener monFormListener) {
        this.mContext = mContext;
        this.documentList = mList;
        this.monFormListener=monFormListener;
    }

    @NonNull
    @Override
    public ExempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.form_row_view,parent,false);
        return new FormAdaptor.ExempleViewHolder(v,monFormListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExempleViewHolder holder, int position) {
        DocumentDownloadModel item = documentList.get(position);
        holder.tvDocumentType.setText("Documentul" + item.getDocumentName());
    }


    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public interface OnInvoiceListener {
    }

    public class ExempleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvDocumentType;
        public Button btndown, btnup;
        FormAdaptor.OnFormListener onFormListener;

        public ExempleViewHolder(@NonNull View itemView, FormAdaptor.OnFormListener onFormListener) {
            super(itemView);
            tvDocumentType = itemView.findViewById(R.id.documentType);
            btndown = itemView.findViewById(R.id.download);
            btnup = itemView.findViewById(R.id.upload);

            btndown.setOnClickListener(this);
            btnup.setOnClickListener(this);

            this.onFormListener=onFormListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onFormListener.onFormListener(getAdapterPosition());
            int position;
            position = getAdapterPosition();
            DocumentDownloadModel item = documentList.get(position);

            switch (v.getId()) {
                case R.id.download:
                    downloadDoc(item);
                    break;
                case R.id.upload:
                    uploadDoc(item);
                    break;
            }
        }

        private void uploadDoc(DocumentDownloadModel item) {
        }

        private void downloadDoc(DocumentDownloadModel item) {
        }
    }

    public interface OnFormListener {
         void onFormListener(int position);
    }
}