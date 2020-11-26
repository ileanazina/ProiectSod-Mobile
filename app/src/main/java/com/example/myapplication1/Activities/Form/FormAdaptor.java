package com.example.myapplication1.Activities.Form;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication1.Model.DocumentDownloadModel;
import com.example.myapplication1.R;

import java.io.File;
import java.text.SimpleDateFormat;
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String uploadDate = formatter.format(item.getUploadDate());

        String aux= "";
        if(item.isActive() == true)
            aux = "Dispoibil pentru descarcare";
        else aux = "Indisponibi";

        holder.tvDocumentType.setText(item.getDocumentTypeName());
        holder.tvDocumentInfo.setText( "Nume: " + item.getDocumentName() + "\nData la care a fost incarcat: " + uploadDate + "\n" + aux);
    }


    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public interface OnInvoiceListener {
    }

    public class ExempleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvDocumentInfo, tvDocumentType;
        public ImageButton btndown, btnup;
        FormAdaptor.OnFormListener onFormListener;

        public ExempleViewHolder(@NonNull View itemView, FormAdaptor.OnFormListener onFormListener) {
            super(itemView);
            tvDocumentInfo = itemView.findViewById(R.id.documentInfo);
            tvDocumentType = itemView.findViewById(R.id.documentType);
            btndown = itemView.findViewById(R.id.downloadButton);
            btnup = itemView.findViewById(R.id.uploadButton);

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
                case R.id.downloadButton:
                    downloadDoc(item);
                    break;
                case R.id.uploadButton:
                    uploadDoc(item);
                    break;
            }
        }

        private void uploadDoc(DocumentDownloadModel item) {

        }

        private void downloadDoc(DocumentDownloadModel item) {
            String url = item.getDownloadLink();
            String docName = item.getDocumentName();
            Uri uri = Uri.parse(url);
            if(docName.contains(".pdf")){

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
            else{

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
            }
        }
    }

    public interface OnFormListener {
         void onFormListener(int position);
    }
}