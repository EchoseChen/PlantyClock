package com.ecnu.plantyclock.Weather;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.LocationClient;
import com.ecnu.plantyclock.R;
import com.ecnu.plantyclock.Weather.Bean.WeatherBean;
import com.ecnu.plantyclock.Weather.adapter.ReAdapter;
import com.ecnu.plantyclock.Weather.adapter.ReAdapter2;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by ChenLu on 2021/5/23
 * Use for 天气的功能界面
 */
public class WeatherFragment extends Fragment {
    private TextView tv_zhunagtai;
    String cityName = "上海";
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv11, tv22, tv33, tv44, tv55, tv66;
    private TextView sheshidu;
    private TextView fengxiang;
    private ImageView img_zhungkuang;
    private SwipeRefreshLayout refreshLayout;
    private WeatherBean weatherBean;
    private TextView tv_cityName;
    private EditText editText;
    private Button btn_chaxun;
    private TextView qiehuan;
    private String weatherCon;

    private SharedPreferences sharedPreferences;





    private CallBackInterface callBackInterface;
    public interface CallBackInterface{
        public void gettValues(String str);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackInterface = (CallBackInterface) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.weather_activity_main,null);

        tv_zhunagtai = view.findViewById(R.id.tv_zhuangtai);
        sheshidu = view.findViewById(R.id.tv_sheshidu);
        fengxiang = view.findViewById(R.id.tv_fengxiang);
        img_zhungkuang = view.findViewById(R.id.img_now_zhuangkuang);
        recyclerView = view.findViewById(R.id.ry_recycleView);
        recyclerView2 = view.findViewById(R.id.ry_recycleView2);
        refreshLayout = view.findViewById(R.id.srl_swipe);
        tv_cityName = view.findViewById(R.id.tv_cityName);
        qiehuan = view.findViewById(R.id.tv_qiehuan);






        tv_cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherFragment.MyDialog myDialog = new WeatherFragment.MyDialog(getActivity());
                myDialog.show();
            }
        });
        qiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherFragment.MyDialog myDialog = new WeatherFragment.MyDialog(getActivity());
                myDialog.show();
            }
        });

        //下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectWeather(tv_cityName.getText().toString());
                    }
                }, 1000);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager1);

        tv1 = view.findViewById(R.id.foot_tv1);
        tv2 = view.findViewById(R.id.foot_tv2);
        tv3 = view.findViewById(R.id.foot_tv3);
        tv4 = view.findViewById(R.id.foot_tv4);
        tv5 = view.findViewById(R.id.foot_tv5);
        tv6 = view.findViewById(R.id.foot_tv6);
        tv11 = view.findViewById(R.id.foot_tv11);
        tv22 = view.findViewById(R.id.foot_tv22);
        tv33 = view.findViewById(R.id.foot_tv33);
        tv44 = view.findViewById(R.id.foot_tv44);
        tv55 = view.findViewById(R.id.foot_tv55);
        tv66 = view.findViewById(R.id.foot_tv66);

        //得到sp存储的值
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String s_chengshi = sharedPreferences.getString("chengshi", "");
        //判断sp的值时候为空 为空就执行默认城市
        if (s_chengshi.equals("")) {
            selectWeather(cityName);
        } else {//否则查询sp存储的值
            selectWeather(s_chengshi);
        }
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView test1=(TextView)getActivity().findViewById(R.id.foot_tv1);
        Typeface typeface1= Typeface.createFromAsset(getActivity().getAssets(),"fonts/wawati.TTF");
        test1.setTypeface(typeface1);
        TextView test2=(TextView)getActivity().findViewById(R.id.foot_tv2);
        Typeface typeface2= Typeface.createFromAsset(getActivity().getAssets(),"fonts/wawati.TTF");
        test2.setTypeface(typeface2);
        TextView test3=(TextView)getActivity().findViewById(R.id.foot_tv3);
        Typeface typeface3= Typeface.createFromAsset(getActivity().getAssets(),"fonts/wawati.TTF");
        test3.setTypeface(typeface3);
        TextView test4=(TextView)getActivity().findViewById(R.id.foot_tv4);
        Typeface typeface4= Typeface.createFromAsset(getActivity().getAssets(),"fonts/wawati.TTF");
        test4.setTypeface(typeface4);
        TextView test5=(TextView)getActivity().findViewById(R.id.foot_tv5);
        Typeface typeface5= Typeface.createFromAsset(getActivity().getAssets(),"fonts/wawati.TTF");
        test5.setTypeface(typeface5);
        TextView test6=(TextView)getActivity().findViewById(R.id.foot_tv6);
        Typeface typeface6= Typeface.createFromAsset(getActivity().getAssets(),"fonts/wawati.TTF");
        test6.setTypeface(typeface6);
    }



    /**
     * @param cityName 城市名称
     */
    private void selectWeather(String cityName) {
        JSONObject jsonObject = new JSONObject();
        String url = "https://way.jd.com/he/freeweather?city=" + cityName + "&appkey=1fea054bfd7426b48e50b2863fdc2bd2";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson = new Gson();
                weatherBean = gson.fromJson(jsonObject.toString(), WeatherBean.class);
                if(weatherBean.getResult().getHeWeather5().get(0).getStatus().equals("unknown location")){
                    Toast.makeText(getActivity(), "输入城市有误", Toast.LENGTH_SHORT).show();
                }else {
                    weatherCon = weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt();
                    callBackInterface.gettValues(weatherCon);
                    tv_zhunagtai.setText(weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt());
                    sheshidu.setText(weatherBean.getResult().getHeWeather5().get(0).getNow().getTmp() + "℃");
                    fengxiang.setText(weatherBean.getResult().getHeWeather5().get(0).getNow().getWind().getDir());
                    //根据天气状况 来判断图片
                    if (weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("多云") || weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("阴")) {
                        img_zhungkuang.setImageResource(R.mipmap.yin);
                    } else if (weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("晴")) {
                        img_zhungkuang.setImageResource(R.mipmap.qinglang);
                    } else if (weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("雨夹雪") || weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("小雪")) {
                        img_zhungkuang.setImageResource(R.mipmap.xiaoxue);
                    } else if (weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("小雨")) {
                        img_zhungkuang.setImageResource(R.mipmap.xiaoyu);
                    } else if (weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("中雨") || weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("大雨")) {
                        img_zhungkuang.setImageResource(R.mipmap.dayu);
                    } else if (weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("雷阵雨")) {
                        img_zhungkuang.setImageResource(R.mipmap.leizhenyu);
                    }else if (weatherBean.getResult().getHeWeather5().get(0).getNow().getCond().getTxt().equals("雾")) {
                        img_zhungkuang.setImageResource(R.mipmap.wu);
                    }
                    //3小时 recyclerView滑动适配
                    ReAdapter reAdapter = new ReAdapter(weatherBean.getResult().getHeWeather5().get(0).getHourly_forecast());
                    recyclerView.setAdapter(reAdapter);
                    //未来七天  recyclerView滑动适配
                    ReAdapter2 reAdapter2 = new ReAdapter2(weatherBean.getResult().getHeWeather5().get(0).getDaily_forecast());
                    recyclerView2.setAdapter(reAdapter2);
                    tv_cityName.setText(weatherBean.getResult().getHeWeather5().get(0).getBasic().getCity());
                    //生活指数部分

                    tv1.setText("舒适度指数：" + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getComf().getBrf());
                    tv2.setText("洗车指数：" + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getCw().getBrf());
                    tv3.setText("穿衣指数：" + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getDrsg().getBrf());
                    tv4.setText("感冒指数：" + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getFlu().getBrf());
                    tv5.setText("运动指数：" + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getSport().getBrf());
                    tv6.setText("旅游指数：" + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getTrav().getBrf());

                    tv11.setText("      " + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getComf().getTxt());
                    tv22.setText("      " + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getCw().getTxt());
                    tv33.setText("      " + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getDrsg().getTxt());
                    tv44.setText("      " + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getFlu().getTxt());
                    tv55.setText("      " + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getSport().getTxt());
                    tv66.setText("      " + weatherBean.getResult().getHeWeather5().get(0).getSuggestion().getTrav().getTxt());
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setColorSchemeResources(R.color.colorPrimary);
                    Toast.makeText(getActivity(), "更新时间" + weatherBean.getResult().getHeWeather5().get(0).getBasic().getUpdate().getLoc(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "网络有误", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Dialog对话框
     */
    class MyDialog extends Dialog {

        public MyDialog(Context context) {
            super(context);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog);
            editText = findViewById(R.id.edit_chengshi);
            btn_chaxun = findViewById(R.id.btn_quding);
            btn_chaxun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("dddddddd", "dfgagfagdfg");
                    if (editText.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "输入为空,重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        selectWeather(editText.getText().toString());
                        //sp存储
                        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("chengshi", editText.getText().toString().trim());
                        editor.commit();
                        dismiss();
                    }
                }
            });
        }
    }
}