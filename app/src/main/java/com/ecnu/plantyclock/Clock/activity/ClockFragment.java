package com.ecnu.plantyclock.Clock.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ecnu.plantyclock.Clock.AlarmService;
import com.ecnu.plantyclock.Clock.Model.AlarmModel;
import com.ecnu.plantyclock.Clock.Utils.ActivityManager;
import com.ecnu.plantyclock.Clock.Utils.DividerItemDecoration;
import com.ecnu.plantyclock.Clock.Utils.MyTimeSorter;
import com.ecnu.plantyclock.Clock.data.MyAlarmDataBase;
import com.ecnu.plantyclock.R;
import com.ecnu.plantyclock.activities.StyleActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ChenLu on 2021/5/24
 * Use for 嵌套入styleactivity
 */
public class ClockFragment  extends Fragment {
    private MyAlarmDataBase db;
    private Toolbar mToolBar;
    private FloatingActionButton mAddAlarmBtn;
    private RecyclerView mRecyclerView;
    private TextView mNoAlarmTextView;
//    private MainActivity.MyReAdapter adapter;
    private MyReAdapter adapter;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private AlarmService.MyBinder binder;
    private ServiceConnection connection = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.clock_activity_main);
        View view = View.inflate(getActivity(),R.layout.clock_activity_main,null);
        ActivityManager.addActivity(getActivity());

        db = new MyAlarmDataBase(getActivity().getApplicationContext());

        mToolBar = (Toolbar) view.findViewById(R.id.toolbar);
        mAddAlarmBtn = (FloatingActionButton) view.findViewById(R.id.add_reminder);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_list);
        mNoAlarmTextView = (TextView) view.findViewById(R.id.no_alarm_text);

        List<AlarmModel> mAlarmList = db.getAllAlarms();

        if (mAlarmList.isEmpty()) {
            mNoAlarmTextView.setVisibility(View.VISIBLE);
        }


        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
//        adapter = new MainActivity.MyReAdapter();
        adapter = new MyReAdapter();
        adapter.setItemCount();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        mToolBar.setTitle(R.string.app_name);

        mAddAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.ecnu.plantyclock.Clock.activity.AddAlarmActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }

    protected int getDefaultItemCount() {

        return 100;
    }

    public void onResume() {
        super.onResume();
        List<AlarmModel> list = db.getAllAlarms();

        if (list.isEmpty()) {
            mNoAlarmTextView.setVisibility(View.VISIBLE);
        } else {
            mNoAlarmTextView.setVisibility(View.GONE);
        }
        adapter.setItemCount();

    }

    public void cancelAlarm(final Context context, final int id, final AlarmModel alarm){

        connection = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (AlarmService.MyBinder)service;
                binder.cancelAlarm(alarm, id, context);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("MainActivity","解绑服务");
            }
        };
        Intent intent = new Intent(getActivity(),AlarmService.class);

        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Log.d("MainActivity", "取消闹钟");

        getActivity().unbindService(connection);

    }


    class MyReAdapter extends RecyclerView.Adapter<MyReAdapter.MyViewHolder> {

        private ArrayList<MyReAdapter.AlarmItem> mItems;

        public MyReAdapter() {
            mItems = new ArrayList<>();
        }

        public long getItemId(int position) {

            return position;
        }

        public void setItemCount() {
            mItems.clear();
            mItems.addAll(loadData());
            notifyDataSetChanged();
        }


        public void onDeleteItem() {
            mItems.clear();
            mItems.addAll(loadData());
        }

        public void removeItemSelected(int selected) {
            if (mItems.isEmpty()) {
                return;
            }
            mItems.remove(selected);
            notifyItemRemoved(selected);
        }

        @Override
        public MyReAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.items_layout, parent, false);

            return new MyReAdapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyReAdapter.MyViewHolder holder, final int position) {
            MyReAdapter.AlarmItem item = mItems.get(position);
            holder.setAlarmTitle(item.mTitle);
            holder.setAlarmTime(item.mTime);
            holder.setRepeatType(item.mRepeatType);
            holder.setActiveImage(item.mActive);
            holder.getItemPosition(position);
            holder.mOverFlowImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View view = mRecyclerView.getChildAt(position);
                    int po = mRecyclerView.getChildAdapterPosition(view);
                    int id = IDmap.get(po);

                    AlarmModel alarm = db.getAlarm(id);
                    db.deleteAlarm(alarm);
                    db.deleteQuestion(alarm);

                    adapter.removeItemSelected(po);
                    adapter.onDeleteItem();
                    cancelAlarm(getActivity(), id, alarm);

                    Toast.makeText(getActivity(), "已删除闹钟", Toast.LENGTH_SHORT).show();


                    List<AlarmModel> alarmModels = db.getAllAlarms();
                    if (alarmModels.isEmpty()) {
                        mNoAlarmTextView.setVisibility(View.VISIBLE);
                    } else {
                        mNoAlarmTextView.setVisibility(View.INVISIBLE);
                    }

                }
            });
        }

//        private void showPopupMenu(View v, final int position) {
//            PopupMenu menu = new PopupMenu(MainActivity.this, v);
//            menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
//            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//
//                    View view = mRecyclerView.getChildAt(position);
//                    int po = mRecyclerView.getChildAdapterPosition(view);
//                    int id = IDmap.get(po);
//                    switch (item.getItemId()) {
//                        case R.id.popup_delete:
//                            AlarmModel alarm = db.getAlarm(id);
//                            db.deleteAlarm(alarm);
//                            db.deleteQuestion(alarm);
//                            adapter.removeItemSelected(po);
//                            adapter.onDeleteItem(getDefaultItemCount());
//
//                            cancelAlarm(MainActivity.this,id,alarm);
//
//                            List<AlarmModel> alarmModels = db.getAllAlarms();
//                            if (alarmModels.isEmpty()) {
//                                mNoAlarmTextView.setVisibility(View.VISIBLE);
//                            } else {
//                                mNoAlarmTextView.setVisibility(View.INVISIBLE);
//                            }
//                            break;
//                        case R.id.popup_edit:
//                            selectAlarm(id);
//                            break;
//                        default:
//                            break;
//                    }
//                    return false;
//                }
//            });
//
//            menu.show();
//        }


        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {

            private TextView mTitleText, mTimeText, mRepeatText;
            private ImageView mActiveImage, mThumbnailImage, mOverFlowImage;
            private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
            private TextDrawable mDrawableBuilder;
            private int itemPosition;


            public MyViewHolder(final View itemView) {
                super(itemView);

                itemView.setOnClickListener(this);
                itemView.setLongClickable(true);

                mTitleText = (TextView) itemView.findViewById(R.id.re_tittle);
                mTimeText = (TextView) itemView.findViewById(R.id.re_time);
                mRepeatText = (TextView) itemView.findViewById(R.id.re_repeatType);
                mActiveImage = (ImageView) itemView.findViewById(R.id.active_image);
                mThumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
                mOverFlowImage = (ImageView) itemView.findViewById(R.id.delete_image);

                mActiveImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int po = getItemPosition(itemPosition);

                        View view = mRecyclerView.getChildAt(po);
                        int id = mRecyclerView.getChildAdapterPosition(view);
                        int CheckedAlarmID = IDmap.get(id);
                        MyReAdapter.AlarmItem item = mItems.get(po);
                        AlarmModel alarmModel = db.getAlarm(CheckedAlarmID);

                        if (item.mActive.equals("true")) {

                            item.mActive = "false";
                            setActiveImage(item.mActive);
                            alarmModel.setActive("false");
                            setActiveImage(item.mActive);
                            db.updateAlarm(alarmModel);
                            cancelAlarm(getActivity(), CheckedAlarmID, alarmModel);
                            Toast.makeText(getActivity(), "已关闭闹钟", Toast.LENGTH_SHORT).show();

                        } else if (item.mActive.equals("false")) {

                            item.mActive = "true";
                            setActiveImage(item.mActive);
                            alarmModel.setActive("true");
                            db.updateAlarm(alarmModel);
                            restartAlarm(item.mTime, item.mRepeatType, CheckedAlarmID);
                            Toast.makeText(getActivity(), "已开启闹钟", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @Override
            public void onClick(View v) {
                int id = mRecyclerView.getChildAdapterPosition(v);
                int CheckedAlarmID = IDmap.get(id);
                selectAlarm(CheckedAlarmID);
            }



            public void setAlarmTitle(String title) {
                mTitleText.setText(title);
                String letter = "a";
                if (title != null && !title.isEmpty()) {
                    letter = title.substring(0, 1);
                }

                int color = mColorGenerator.getRandomColor();

                mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);
                mThumbnailImage.setImageDrawable(mDrawableBuilder);
            }

            public void setAlarmTime(String time) {
                mTimeText.setText(time);
            }

            public void setRepeatType(String type) {
                mRepeatText.setText(type);

            }

            public void setActiveImage(String active) {
                if (active.equals("true")) {
                    mActiveImage.setImageResource(R.drawable.ic_alarm_on_grey_600_24dp);
                } else if (active.equals("false")) {
                    mActiveImage.setImageResource(R.drawable.ic_alarm_off_grey_600_24dp);
                }
            }

            public int getItemPosition(int position) {
                this.itemPosition = position;
                return position;
            }
        }


        class AlarmItem {
            public String mTitle;
            public String mTime;
            public String mRepeatType;
            public String mRepeatCode;
            public String mActive;

            public AlarmItem(String title, String time, String repeatNormal, String repeatDefine, String active) {
                this.mTitle = title;
                this.mTime = time;
                this.mRepeatType = repeatNormal;
                this.mRepeatCode = repeatDefine;
                this.mActive = active;

            }
        }

        public List<MyReAdapter.AlarmItem> loadData() {

            ArrayList<MyReAdapter.AlarmItem> items = new ArrayList<>();
            List<AlarmModel> am = db.getAllAlarms();
            List<String> Titles = new ArrayList<>();
            List<String> Repeat = new ArrayList<>();
            List<String> RepeatCode = new ArrayList<>();
            List<String> Actives = new ArrayList<>();
            List<String> Time = new ArrayList<>();
            List<Integer> IDList = new ArrayList<>();
            List<MyTimeSorter> TimeSortList = new ArrayList<>();

            for (AlarmModel a : am) {
                Titles.add(a.getTitle());
                Time.add(a.getTime());
                Repeat.add(a.getRepeatType());
                RepeatCode.add(a.getRepeatCode());
                Actives.add(a.getActive());
                IDList.add(a.getID());
            }

            int key = 0;
            for (int k = 0; k < Titles.size(); k++) {
                TimeSortList.add(new MyTimeSorter(key, Time.get(k)));
                key++;
            }

            Collections.sort(TimeSortList, new MyReAdapter.TimeComparator());

            int k = 0;
            for (MyTimeSorter item : TimeSortList) {

                int i = item.getIndex();
                items.add(new MyReAdapter.AlarmItem(Titles.get(i), Time.get(i), Repeat.get(i), RepeatCode.get(i), Actives.get(i)));
                IDmap.put(k, IDList.get(i));
                k++;
            }
            return items;
        }

        public class TimeComparator implements Comparator {
            DateFormat f = new SimpleDateFormat("hh:mm");

            public int compare(Object a, Object b) {
                String o1 = ((MyTimeSorter) a).getTime();
                String o2 = ((MyTimeSorter) b).getTime();

                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

    }

    private void restartAlarm(final String time, final String repeat, final int id) {

        connection = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (AlarmService.MyBinder)service;
                switch (repeat){
                    case "只响一次":
                        binder.setSingleAlarm(getActivity().getApplicationContext(),time,id);
                        break;
                    case "每天":
                        binder.setEverydayAlarm(getActivity().getApplicationContext(),time,id);
                        break;
                    default:
                        AlarmModel model = db.getAlarm(id);
                        String repeatCode = model.getRepeatCode();
                        binder.setDiyAlarm(getActivity().getApplicationContext(),repeat,time,id, repeatCode);
                }

                Log.d("MainActivity","重启闹钟");
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("MainActivity","解绑服务");
            }
        };
        Intent intent = new Intent(getActivity(),AlarmService.class);
        getActivity().bindService(intent,connection,Context.BIND_AUTO_CREATE);


        getActivity().unbindService(connection);

    }

    private void selectAlarm(int id) {
        String CheckedAlarm = Integer.toString(id);
        Intent intent = new Intent(getActivity(), com.ecnu.plantyclock.Clock.activity.EditAlarmActivity.class);
        intent.putExtra(com.ecnu.plantyclock.Clock.activity.EditAlarmActivity.ALARM_ID, CheckedAlarm);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.setItemCount();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_finish:
//                finish();
//                break;
//            case R.id.action_about:
//                Intent i = new Intent(this, com.ecnu.plantyclock.Clock.activity.AboutActivity.class);
//                startActivity(i);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
    public void onDestroy()
    {
        super.onDestroy();
    }


//    @Override
//    public void onBackPressed() {
//        finish();
//        super.onBackPressed();
//    }

}