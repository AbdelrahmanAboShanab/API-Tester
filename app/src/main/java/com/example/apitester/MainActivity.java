package com.example.apitester;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apitester.database.PostContract;
import com.example.apitester.network.PostLoader;
import com.example.apitester.pojo.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Integer> {

    StringBuilder res = new StringBuilder();
    Toast mToast;
    Context context=this;

    EditText urlET,headers;
    TextView textView;
    RadioGroup radioGroup;
    RadioButton get,post;
    Button request;
    Button goToData;
    LinearLayout postData;
    String url;
    int headersCount , requestMethod;

    ArrayList<String> headersList;
    EditText userIdET,titleET,bodyET;
    String userId,title,body;

    TextView response;

    public static ArrayList<Post> posts = new ArrayList<Post>();
    private static final String USGS_REQUEST_URL =
            "https://jsonplaceholder.typicode.com/users/1/posts";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    LinearLayout headersLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InflatingViews();
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckEmptyValues()){
                    if(!CheckConnection()){
                        Log.i("TAG", "onClick: ddq");
                        if(mToast != null) {
                            mToast.cancel();

                        }
                        mToast = Toast.makeText(context, "No Internet connection !!!", Toast.LENGTH_LONG);
                        mToast.show();

                    }else{
                        Log.i("TAG", "aaaa: ");
                        GetData();
                        Log.i("TAG", "onClick: "+url+"  -  "+headersCount+"  - "+requestMethod);
                        StartLoader();
                    }
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.getRB:
                        if(postData.getVisibility() == View.VISIBLE){
                            postData.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.postRB:
                            if(postData.getVisibility() == View.GONE){
                                postData.setVisibility(View.VISIBLE);
                            }else{
                                postData.setVisibility(View.GONE);
                            }
                        break;
                }
            }
        });

        headers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence)){
                if(Integer.valueOf(charSequence.toString()) >= 0 && Integer.valueOf(charSequence.toString()) <= 20){
                    CreateHeaders(Integer.valueOf(charSequence.toString()));
                }else{
                    if(mToast != null) {
                        mToast.cancel();

                    }
                    mToast = Toast.makeText(context, "Enter valid number >= 0 and <= 20", Toast.LENGTH_LONG);
                    mToast.show();
                }
            }else{
                    headersLayout.removeAllViews();
                    if(mToast != null) {
                        mToast.cancel();

                    }
                    mToast = Toast.makeText(context, "Enter headers", Toast.LENGTH_LONG);
                    mToast.show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        goToData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AllPosts.class));
            }
        });


    }

    private void CreateHeaders(int c) {
        for(int i=0;i<c;i++){
            EditText editText = new EditText(MainActivity.this);
            editText.setHint("Enter header");
            editText.setId(i+1);
            editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            editText.setHintTextColor(getResources().getColor(R.color.white));
            editText.setTextColor(getResources().getColor(R.color.white));
            editText.setMarqueeRepeatLimit(20);
            headersLayout.addView(editText);
        }
    }

    private boolean CheckEmptyValues() {
        if(TextUtils.isEmpty(urlET.getText().toString())){
            if(mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, "Enter Url please... !!", Toast.LENGTH_LONG);
            mToast.show();
            return false;
        }else if(TextUtils.isEmpty(headers.getText().toString())){
            if(mToast != null) {
                mToast.cancel();

            }
            mToast = Toast.makeText(context, "Enter number of headers please... !!", Toast.LENGTH_LONG);
            mToast.show();
            return false;
        }else if(radioGroup.getCheckedRadioButtonId() == -1){
            if(mToast != null) {
                mToast.cancel();

            }
            mToast = Toast.makeText(context, "Choose request method please... !!", Toast.LENGTH_LONG);
            mToast.show();
            return false;
        }
        if(postData.getVisibility() == View.VISIBLE ){
            if(TextUtils.isEmpty(userIdET.getText().toString())){
                if(mToast != null) {
                    mToast.cancel();

                }
                mToast = Toast.makeText(context, "Enter user ID please... !!", Toast.LENGTH_LONG);
                mToast.show();
                return false;
            }else if(TextUtils.isEmpty(titleET.getText().toString())){
                if(mToast != null) {
                    mToast.cancel();

                }
                mToast = Toast.makeText(context, "Enter post title please... !!", Toast.LENGTH_LONG);
                mToast.show();
                return false;
            }else if(TextUtils.isEmpty(bodyET.getText().toString())){
                if(mToast != null) {
                    mToast.cancel();

                }
                mToast = Toast.makeText(context, "Enter post body please... !!", Toast.LENGTH_LONG);
                mToast.show();
                return false;
            }
        }
        return true;
    }

    private void InflatingViews() {
        urlET = findViewById(R.id.urlEditText);
        headers = findViewById(R.id.headersEditText);
        radioGroup = findViewById(R.id.method);
        get = findViewById(R.id.getRB);
        post = findViewById(R.id.postRB);
        request = findViewById(R.id.requestBTN);
        goToData =findViewById(R.id.getData);
        radioGroup = findViewById(R.id.method);
        postData = findViewById(R.id.postData);

        userIdET = findViewById(R.id.dataUserID);
        titleET = findViewById(R.id.dataTitle);
        bodyET = findViewById(R.id.dataBody);
        headersLayout=findViewById(R.id.headersLayout);
        response = findViewById(R.id.response);
    }

    private void GetData() {
        url = urlET.getText().toString();
        Log.i(" ", "GetData: ffff");
        headersCount = Integer.valueOf(headers.getText().toString());
        Log.i("TAG", "GetData:1 ");
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.getRB:
                requestMethod = 1;
                Log.i("TAG", "GetData:2 ");
                break;
            case R.id.postRB:
                requestMethod = 2;
                break;
        }

        headersList = new ArrayList<String>();
        for (int i=0;i<headersCount;i++){
            EditText e = findViewById(i+1);
            headersList.add(e.getText().toString());
        }
    }

    private void StartLoader() {
        LoaderManager loaderManager = getLoaderManager();
        Log.i("TAG", "m3: ");
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    private boolean CheckConnection() {
        Log.i("TAG", "m00: ");
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("TAG", "m000: ");
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Log.i("TAG", "m2: ");
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    @Override
    public Loader<Integer> onCreateLoader(int i, Bundle bundle) {
        if(requestMethod==1){
            return new PostLoader(this, url,requestMethod,headersList);
        }
        HashMap<String,String> data = new HashMap<String,String>();
        userId = userIdET.getText().toString();
        title = titleET.getText().toString();
        body = bodyET.getText().toString();
        data.put("userId",userId);
        data.put("userId",title);
        data.put("userId",body);

        return new PostLoader(this, url,requestMethod,headersList,data);
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer integer) {

        if (requestMethod == 1) {

            for(int i=0;i<posts.size();i++){
                ContentValues contentValues = new ContentValues();
                contentValues.put(PostContract.PostEntry.COLUMN_USER_ID,posts.get(i).getUserId());
                contentValues.put(PostContract.PostEntry.COLUMN_TITLE,posts.get(i).getTitle());
                contentValues.put(PostContract.PostEntry.COLUMN_BODY,posts.get(i).getBody());
                getContentResolver().insert(PostContract.PostEntry.CONTENT_URI,contentValues);
            }


            res.append("Request type : Get");
        } else {
            res.append("Request type :Post");
        }
        res.append("\n\nResponse code : "+integer);
        res.append("\n");
            for (int i=0;i<headersList.size();i++){
                res.append("\n Header "+(i+1)+" : "+headersList.get(i));
            }

        res.append("\n");
        if(requestMethod == 2){
            res.append("\n Body of post request : \nUser id : "+userId+"\nPost title : "+title+"\nPost body : "+body);
        }

        response.setText(res.toString());

        if(integer ==-1){
            if(mToast != null) {
                mToast.cancel();

            }
            mToast = Toast.makeText(context, "Enter valid url", Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }
}
