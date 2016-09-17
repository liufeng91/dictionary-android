package com.example.administrator.biying2;

/**
 * Created by Administrator on 2016/9/2.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.String;


import android.view.View.OnClickListener;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MainActivity extends Activity {
    private static String request_path =
            "http://fanyi.youdao.com/openapi.do?keyfrom=hui-application&key=1176244735&type=data&doctype=json&version=1.1&q=";
    EditText et_word;
    Button btn_query;
    Button clear;
    TextView tv_result;
    String new_request_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final RequestQueue mQueue = Volley.newRequestQueue(this);
        et_word = (EditText) findViewById(R.id.city);
        tv_result = (TextView) findViewById(R.id.result);
        btn_query = (Button) findViewById(R.id.query);
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_word.setText("");
            }
        });
        btn_query.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                tv_result.setText("");
                String word;
                word = et_word.getText().toString();
                if (word.length() < 1) {
                    Toast.makeText(MainActivity.this, "请输入翻译内容",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String send_word;
                try{
                    send_word = java.net.URLEncoder.encode(word,"utf-8");
                    StringBuilder sb = new StringBuilder(request_path);
                    sb.append(send_word);
                    new_request_path = sb.toString();
                }catch(Exception e){

                }
                StringRequest stringRequest = new StringRequest(new_request_path,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String explain;
                                JSONObject is_result;
                                try{
                                    JSONObject jso = new JSONObject(response);
                                    if(!jso.has("basic")){
                                        tv_result.setText("无查询结果，请确认查询内容");
                                    }else{
                                        is_result = jso.getJSONObject("basic");
                                        explain = is_result.getString("explains");
                                        org.codehaus.jettison.json.JSONArray jsa = new org.codehaus.jettison.json.JSONArray(explain);
                                        String showresult="";
                                        StringBuilder sbshowresult = new StringBuilder(showresult);
                                        for(int i=0;i<jsa.length();i++)
                                        {
                                            sbshowresult.append("  ");
                                            sbshowresult.append(jsa.getString(i));
                                            sbshowresult.append("\n");
                                        }
                                        showresult = sbshowresult.toString();
                                        tv_result.setText(showresult);
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                mQueue.add(stringRequest);
            }
        });
    }
}
