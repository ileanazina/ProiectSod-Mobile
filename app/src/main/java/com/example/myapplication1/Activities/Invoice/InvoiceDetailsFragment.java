package com.example.myapplication1.Activities.Invoice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.AddressModel;
import com.example.myapplication1.Model.InvoiceDetailsModel;
import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.Model.UnitMeasurementsModel;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class  InvoiceDetailsFragment extends Fragment {

    public interface RevealDetailsCallbacks {
        void getDataFromInvoiceDetails(InvoiceDetailsModel invoiceDetails);
        void getDataFromAddress (List<AddressModel> address);
        void getDataFromUnitMeasurement(UnitMeasurementsModel unitMeasurements);
    }

    private RevealDetailsCallbacks callback;
    private AccountModel account;

    private InvoiceModel invoice_obj;
    private InvoiceDetailsModel invoiceDetails_obj;
    private AddressModel address_obj;
    private UnitMeasurementsModel unitMeasurements_obj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_details, container, false);

        final ProgressDialog dialog = ProgressDialog.show(getContext(), null, getResources().getString(R.string.invoice_detail_please_wait));
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            invoice_obj = (InvoiceModel) bundle.getSerializable("extra");
        }

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("AccountInfo",null);
        account = gson.fromJson(json, AccountModel.class);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public void getDataFromInvoiceDetails(InvoiceDetailsModel invoiceDetails) {
                invoiceDetails_obj = invoiceDetails;
                if(invoiceDetails != null)
                    getUnitMeasurementFromInvoiceDetail(getContext(), callback);
                else getAddressByAccount(getContext(), callback);
            }

            @Override
            public void getDataFromUnitMeasurement(UnitMeasurementsModel unitMeasurements) {
                unitMeasurements_obj = unitMeasurements;
                getAddressByAccount(getContext(), callback);
            }

            @Override
            public void getDataFromAddress(List<AddressModel> address) {
                for (int i = 0; i < address.size(); i++)
                    if (invoice_obj.getAddressId() == address.get(i).getAddressId()) {
                        address_obj = address.get(i);
                        break;
                    }
                completeTheView(view);
                dialog.dismiss();
            }
        };

        Button sentEmail = view.findViewById(R.id.buttonEmailMyInvoice);
        sentEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvoiceByMail();
            }
        });

        getInvoiceDetail(callback);
        return  view;
    }

    public void getInvoiceDetail(RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<InvoiceDetailsModel> call = invoiceAPI.getInvoiceDetailsByInvoice("Bearer " + account.getToken(),invoice_obj.getInvoiceId());
        call.enqueue(new Callback<InvoiceDetailsModel>() {
            @Override
            public void onResponse(Call<InvoiceDetailsModel> call, Response<InvoiceDetailsModel> response) {
                InvoiceDetailsModel invoiceDetails = response.body();
                if(callback != null) {
                    callback.getDataFromInvoiceDetails(invoiceDetails);
                }
            }

            @Override
            public void onFailure(Call<InvoiceDetailsModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getAddressByAccount(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<List<AddressModel>> call = invoiceAPI.getAddressesByAccountId("Bearer " + account.getToken());
        call.enqueue(new Callback<List<AddressModel>>() {
            @Override
            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
                List<AddressModel> address = response.body();
                if(callback != null) {
                    callback.getDataFromAddress(address);
                }
            }

            @Override
            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getUnitMeasurementFromInvoiceDetail(Context context, RevealDetailsCallbacks callback) {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<UnitMeasurementsModel> call = invoiceAPI.getUnitMeasurementsByUMId("Bearer " + account.getToken(), invoiceDetails_obj.getUnitMeasurementType());
        call.enqueue(new Callback<UnitMeasurementsModel>() {
            @Override
            public void onResponse(Call<UnitMeasurementsModel> call, Response<UnitMeasurementsModel> response) {
                UnitMeasurementsModel unitMeasurements = response.body();
                if(callback != null) {
                    callback.getDataFromUnitMeasurement(unitMeasurements);
                }
            }

            @Override
            public void onFailure(Call<UnitMeasurementsModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void completeTheView(View view)
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        //invoice id
        TextView textView_invoiceID = view.findViewById(R.id.invoiceId);
        textView_invoiceID.setText(Integer.toString(invoice_obj.getInvoiceId()));

        //dateOfIssue
        TextView textView_dateOfIssue = view.findViewById(R.id.dateOfIssue);
        if(invoice_obj.getDateOfIssue() == null)
            textView_dateOfIssue.setText("-");
        else textView_dateOfIssue.setText(df.format(invoice_obj.getDateOfIssue()));

        //dueData
        TextView textView_dueData = view.findViewById(R.id.dueDate);
        if(invoice_obj.getDueDate() == null)
            textView_dueData.setText("-");
        else textView_dueData.setText(df.format(invoice_obj.getDueDate()));

        //accountId
        TextView textView_accountId = view.findViewById(R.id.accountId_invoicesdetail);
        textView_accountId.setText(String.valueOf(invoice_obj.getAccountId()));

        //clientAddress
        TextView textView_addressClient = view.findViewById(R.id.addressId);
        if(address_obj.getFullAddressName() == null || address_obj.getFullAddressName() =="")
            textView_addressClient.setText("-");
        else textView_addressClient.setText(address_obj.getFullAddressName());

        //CUI issue company
        TextView textView_cui = view.findViewById(R.id.IssueCUI);
        if(invoice_obj.getCUIIssuer() == 0)
            textView_cui.setText("-");
        else textView_cui.setText(String.valueOf(invoice_obj.getCUIIssuer()));

        //addressIssue
        TextView textView_addressIssue = view.findViewById(R.id.IssueAddress);
        if(invoice_obj.getIssuerAddress() == null || invoice_obj.getIssuerAddress() == "")
            textView_addressIssue.setText("-");
        else textView_addressIssue.setText(invoice_obj.getIssuerAddress());

        if(invoiceDetails_obj != null){
            //youHaveToPay
            TextView textView_toPay = view.findViewById(R.id.youHaveToPay);
            textView_toPay.setText(String.valueOf(invoiceDetails_obj.getValueWithVat()+invoiceDetails_obj.getSold()));

            //sold
            TextView textView_sold = view.findViewById(R.id.soldFromPast);
            if(invoiceDetails_obj.getSold() == 0)
                textView_sold.setText("-");
            else textView_sold.setText(String.valueOf(invoiceDetails_obj.getSold()));

            //unitMeasurementType
            TextView textView_unitM = view.findViewById(R.id.unitMeasurementType);
            if(invoiceDetails_obj.getUnitMeasurementType() == 0)
                textView_unitM.setText("-");
            else textView_unitM.setText(unitMeasurements_obj.getUnitMeasurementType());

            //pricePerUnit
            TextView textView_pricePerUnit = view.findViewById(R.id.pricePerUnit);
            if(invoiceDetails_obj.getPricePerUnit() == 0)
                textView_pricePerUnit.setText("-");
            else textView_pricePerUnit.setText(String.valueOf(invoiceDetails_obj.getPricePerUnit()));

            //consumedQuantity
            TextView textView_consumedQuantity = view.findViewById(R.id.consumedQuantity);
            if(invoiceDetails_obj.getConsumedQuantity() == 0)
                textView_consumedQuantity.setText("-");
            else textView_consumedQuantity.setText(String.valueOf(invoiceDetails_obj.getConsumedQuantity()));

            //VAT
            TextView textView_VAT = view.findViewById(R.id.simplyVAT);
            if(invoiceDetails_obj.getVAT() == 0)
                textView_VAT.setText("-");
            else textView_VAT.setText(String.valueOf(invoiceDetails_obj.getVAT()));

            //valueWithoutVAT
            TextView textView_valueWithoutVAT = view.findViewById(R.id.valueWithoutVAT);
            if(invoiceDetails_obj.getValueWithoutVAT() == 0)
                textView_valueWithoutVAT.setText("-");
            else textView_valueWithoutVAT.setText(String.valueOf(invoiceDetails_obj.getValueWithoutVAT()));

            //valueWithVat
            TextView textView_valueWithVat = view.findViewById(R.id.valueWithVat);
            if(invoiceDetails_obj.getValueWithVat() == 0)
                textView_valueWithVat.setText("-");
            else textView_valueWithVat.setText(String.valueOf(invoiceDetails_obj.getValueWithVat()));
        }
    }

    public  void sendInvoiceByMail()
    {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<Void> call = invoiceAPI.sentInvoiceByEmail("Bearer " + account.getToken(), invoice_obj.getInvoiceId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), getResources().getString(R.string.invoice_detail_email_send), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.invoice_detail_server_error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}