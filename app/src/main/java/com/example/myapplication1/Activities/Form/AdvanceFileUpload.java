package com.example.myapplication1.Activities.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

import com.example.myapplication1.Model.AccountModel;
import com.example.myapplication1.Model.DocumentDownloadModel;
import com.example.myapplication1.Model.UserUploadModel;
import com.example.myapplication1.R;
import com.example.myapplication1.Remote.APIInterfaces;
import com.example.myapplication1.Remote.RetrofitClientLogIn;
import com.google.gson.Gson;

public class  AdvanceFileUpload extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int ALL_FILE_REQUEST = 102;
    Button select_all_file;

    TextView all_file_name;
    Button submit;
    ProgressBar progressBar;
    int method = 0;
    String  all_file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_file_upload);

        select_all_file = findViewById(R.id.select_from_all_files);
        all_file_name = findViewById(R.id.all_file_name);
        submit = findViewById(R.id.upload);
        progressBar = findViewById(R.id.progressbar);

        select_all_file.setOnClickListener(AdvanceFileUpload.this);
        submit.setOnClickListener(AdvanceFileUpload.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (all_file_path == null) {
                    Toast.makeText(AdvanceFileUpload.this, "File Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                UploadTask uploadTask = new UploadTask();
                uploadTask.execute(new String[]{ all_file_path});
            }
        });
    }

    public  void sendDocument(UserUploadModel userUploadModel)
    {
        APIInterfaces invoiceAPI = RetrofitClientLogIn.getInstance().create(APIInterfaces.class);
        Call<Void> call = invoiceAPI.uploadDoc(userUploadModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Log.d(response.message(), String.valueOf(response.code()));
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_from_all_files) {
            method = 0;
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    filePicker(0);
                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else {
                filePicker(0);
            }

        }  else {
                filePicker(0);
            }
    }

    private void filePicker(int i) {
        if (i == 0) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), ALL_FILE_REQUEST);
        }
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(AdvanceFileUpload.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AdvanceFileUpload.this, permission)) {
            Toast.makeText(AdvanceFileUpload.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(AdvanceFileUpload.this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AdvanceFileUpload.this, "Permission Successfull", Toast.LENGTH_SHORT).show();
                    filePicker(method);
                } else {
                    Toast.makeText(AdvanceFileUpload.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ALL_FILE_REQUEST) {
                if (data == null) {
                    return;
                }

                Uri uri = data.getData();
                String paths = FilePath.getFilePath(AdvanceFileUpload.this, uri);
                Log.d("File Path : ", "" + paths);
                if (paths != null) {
                    all_file_name.setText("" + new File(paths).getName());
                }
                all_file_path = paths;
            }
        }
    }

    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null){
                Toast.makeText(AdvanceFileUpload.this, "File Uploaded", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(AdvanceFileUpload.this, "File Upload Failed", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {

            File file = new File(strings[0]);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();

                for(int i=0; i<bytes.length;i++)
                System.out.println(bytes[i]);

                DocumentDownloadModel documentDownload = (DocumentDownloadModel) getIntent().getSerializableExtra("DocumentDownloadModel");
                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AdvanceFileUpload.this);
                Gson gson = new Gson();
                String json = mPrefs.getString("AccountInfo",null);
                AccountModel account = gson.fromJson(json, AccountModel.class);


               // sendDocument(new UserUploadModel(account.getAccountId(),documentDownload.getDocumentType()
                //        ,documentDownload.getDocumentName(), buf));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}