package com.example.entity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter;

public class Adapter<RowTitle, ColTitle, Cell> extends BaseExcelPanelAdapter<RowTitle, ColTitle, Cell> {

    public Adapter(Context context, View.OnClickListener blockListener) {
        super(context);
    }

    //=========================================normal cell=========================================
    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindCellViewHolder(RecyclerView.ViewHolder holder, int verticalPosition, int horizontalPosition) {

    }

    //=========================================top cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindTopViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    //=========================================left cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindLeftViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    //=========================================top left cell=======================================
    @Override
    public View onCreateTopLeftView() {
        return null;
    }
}