package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.responceBean.AddServicesResponce;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chiranjib on 28/12/17.
 */

public class GetServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<AddServicesResponce.DataBean> serviceList;
    private RemoveImageListioner infoClickListioner;
    private ServicesAddRemoveListioner servicesAddRemoveListioner;
    private String from;

    public GetServiceAdapter(Context context, ArrayList<AddServicesResponce.DataBean> serviceList, RemoveImageListioner infoClickListioner, ServicesAddRemoveListioner servicesAddRemoveListioner, String from) {
        this.context = context;
        this.serviceList = serviceList;
        this.infoClickListioner = infoClickListioner;
        this.servicesAddRemoveListioner = servicesAddRemoveListioner;
        this.from = from;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_service_list_group, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddServicesResponce.DataBean dataBean = serviceList.get(position);
        GetServiceAdapter.MyViewHolder h = (GetServiceAdapter.MyViewHolder) holder;
        if (!TextUtils.isEmpty(dataBean.getServiceImage()))
            Picasso.with(h.serviceItemImage.getContext()).load(dataBean.getServiceImage()).fit().into(h.serviceItemImage);
        h.serviveHeader.setText(dataBean.getServiceName());
        h.subList.setAdapter(new ArrayAdapter<AddServicesResponce.DataBean>(context, R.layout.add_service_list_item, R.id.subItemText, (ArrayList) dataBean.getSubService()));
        setListViewHeightBasedOnChildren(h.subList);
        if (dataBean.getStatus().equals("0")) {
            h.addRemoveBtn.setText("Add in service");
        } else if (dataBean.getStatus().equals("1")) {
            h.addRemoveBtn.setText("Remove");
        }
        if (dataBean.isVisible()) {
            h.subView.setVisibility(View.VISIBLE);
            h.arrowBtn.setBackgroundResource(R.drawable.up_arrow_service);
        } else if (!dataBean.isVisible()) {
            h.subView.setVisibility(View.GONE);
            h.arrowBtn.setBackgroundResource(R.drawable.down_arrow_service);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceItemImage;
        TextView serviveHeader;
        ListView subList;
        LinearLayout subView;
        LinearLayout containerLay;
        ImageButton arrowBtn;
        ImageButton infoBtn;
        Button addRemoveBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            serviceItemImage = itemView.findViewById(R.id.serviceItemImage);
            serviveHeader = itemView.findViewById(R.id.serviveHeader);
            subList = itemView.findViewById(R.id.subList);
            subView = itemView.findViewById(R.id.subView);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            infoBtn = itemView.findViewById(R.id.infoBtn);
            containerLay = itemView.findViewById(R.id.containerLay);
            addRemoveBtn = itemView.findViewById(R.id.addRemoveBtn);
            if (from.equals("profile")) {
                addRemoveBtn.setVisibility(View.GONE);
            }
            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddServicesResponce.DataBean dataBean = serviceList.get(getAdapterPosition());
                    if (dataBean.isVisible()) {
                        serviceList.get(getAdapterPosition()).setVisible(false);

                    } else {
                        serviceList.get(getAdapterPosition()).setVisible(true);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
            containerLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddServicesResponce.DataBean dataBean = serviceList.get(getAdapterPosition());
                    if (dataBean.isVisible()) {
                        serviceList.get(getAdapterPosition()).setVisible(false);

                    } else {
                        serviceList.get(getAdapterPosition()).setVisible(true);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
            infoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    infoClickListioner.onClick(getAdapterPosition());
                }
            });
            addRemoveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    servicesAddRemoveListioner.onClick(getAdapterPosition());
                }
            });
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter.getCount() % 2 == 0) {
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        } else {
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 20;
        }
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
