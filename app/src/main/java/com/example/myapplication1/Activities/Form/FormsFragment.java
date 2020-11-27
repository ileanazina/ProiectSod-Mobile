package com.example.myapplication1.Activities.Form;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication1.Model.DocumentDownloadModel;
import com.example.myapplication1.Model.DownloadFilter;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormsFragment extends Fragment implements FormAdaptor.OnFormListener {
    private RecyclerView recyclerView;
    private List<DocumentDownloadModel> forAdaptorForms;
    private FormAdaptor invoiceAdaptor;
    private RevealDetailsCallbacks callback;

    public interface RevealDetailsCallbacks {
        void getDocuments(List<DocumentDownloadModel> list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_page, container, false);

        recyclerView = view.findViewById(R.id.formRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        forAdaptorForms = new ArrayList<>();
        invoiceAdaptor = new FormAdaptor(getContext(), forAdaptorForms,FormsFragment.this);
        recyclerView.setAdapter(invoiceAdaptor);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDocuments(List<DocumentDownloadModel> list) {
                forAdaptorForms.clear();
                invoiceAdaptor.notifyDataSetChanged();
                if(list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        forAdaptorForms.add(list.get(i));
                        invoiceAdaptor.notifyDataSetChanged();
                    }
                }
            }
        };

        getDocumentsForDownload();
        return view;
    }

    public void getDocumentsForDownload() {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<DocumentDownloadModel>> call = invoiceAPI.downloadAllDocuments(new DownloadFilter());
        call.enqueue(new Callback<List<DocumentDownloadModel>>() {
            @Override
            public void onResponse(Call<List<DocumentDownloadModel>> call, Response<List<DocumentDownloadModel>> response) {
                List<DocumentDownloadModel> documents = response.body();
                if(callback != null) {
                    callback.getDocuments(documents);
                }
            }

            @Override
            public void onFailure(Call<List<DocumentDownloadModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onFormListener(int position) {

    }
}