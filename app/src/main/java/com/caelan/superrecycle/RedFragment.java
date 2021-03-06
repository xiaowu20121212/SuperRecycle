package com.caelan.superrecycle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caelan.superadapter.DefaultDataSource;
import com.caelan.superadapter.ItemBinder;
import com.caelan.superadapter.SuperAdapter;
import com.caelan.superadapter.SuperViewHolder;

import java.util.ArrayList;

/**
 * Created by yangjiacheng on 2018/4/18.
 * ...
 */
public class RedFragment extends Fragment implements View.OnClickListener {

    private SuperAdapter mSuperAdapter;
    private DefaultDataSource dataSource;

    public RedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_red, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSuperAdapter = new SuperAdapter(getContext());

        mSuperAdapter.with(new ItemBinder<TextBean>(R.layout.item_simple_text) {
            @Override
            public void onBindViewHolder(SuperViewHolder holder, TextBean textBean) {
                TextView textView = holder.get(R.id.simple_text);
                TextView remove = holder.get(R.id.remove);
                //registerClickListener(holder, remove);
                textView.setText(textBean.getText());
            }

            @Override
            public void onClick(View v, int position, TextBean textBean) {
                if (v.getId() == R.id.remove) {
                    dataSource.removeData(position);
                } else {
                    Toast.makeText(getActivity(), textBean.getText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(View v, int position, TextBean textBean) {
                Toast.makeText(getActivity(), textBean.getText() + "long click", Toast.LENGTH_SHORT).show();
            }

            @NonNull
            @Override
            public int[] getViewsIdRegisterClickListener() {
                return new int[]{R.id.remove};
            }

        }, new ItemBinder<ImageBean>(R.layout.item_simple_image) {
            @Override
            public void onBindViewHolder(SuperViewHolder holder, ImageBean imageBean) {
                ImageView imageView = holder.get(R.id.simple_image);
                TextView remove = holder.get(R.id.remove);
                //registerClickListener(holder, remove);
                imageView.setImageResource(imageBean.getImageRes());
            }

            @Override
            public void onClick(View v, int position, ImageBean imageBean) {
                if (v.getId() == R.id.remove) {
                    dataSource.removeData(position);
                } else {
                    Toast.makeText(getActivity(), imageBean.getImageRes(), Toast.LENGTH_SHORT).show();
                }
            }

            @NonNull
            @Override
            public int[] getViewsIdRegisterClickListener() {
                return new int[]{R.id.remove};
            }

            @Override
            public boolean getItemClickListenerEnable() {
                return false;
            }
        });

        dataSource = new DefaultDataSource<>(obtainData(20), new DefaultDataSource.Intercept() {
            @Override
            public int onIntercept(int position, Object data) {
                if (data instanceof TextBean) {
                    return 0;
                } else if (data instanceof ImageBean) {
                    return 1;
                }
                return 0;
            }
        });
        mSuperAdapter.setDataSource(dataSource);
        recyclerView.setAdapter(mSuperAdapter);
        mSuperAdapter.notifyDataSetChanged();
    }

    ArrayList<Object> obtainData(int range) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < range; i++) {
            TextBean textBean = new TextBean("I am " + i);
            Log.d("hasCode", String.valueOf(textBean.hashCode()));
            arrayList.add(textBean);
            ImageBean imageBean = new ImageBean(R.mipmap.ic_launcher_round);
            Log.d("hasCode", String.valueOf(imageBean.hashCode()));
            arrayList.add(imageBean);
        }
        return arrayList;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.head_insert:
                dataSource.insertData(new TextBean("I am head insert"), 0);
                break;
            case R.id.middle_insert:
                dataSource.moveData(1, 3);
                break;
            case R.id.end_insert:
                dataSource.setNewDataList(obtainData(2));
                break;
            default:
                break;
        }
    }

}
