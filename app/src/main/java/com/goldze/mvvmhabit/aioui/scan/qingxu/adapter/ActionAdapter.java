package com.goldze.mvvmhabit.aioui.scan.qingxu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.aioui.scan.qingxu.bean.ActionBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/10/6
 * Time: 3:39 下午
 */
public class ActionAdapter extends RecyclerView.Adapter<ActionViewHolder> {
    private List<ActionBean> actionList;

    public ActionAdapter(List<ActionBean> actionList) {
        this.actionList = actionList;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, null, false);
        return new ActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        holder.onBind(actionList.get(position));
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }
}

class ActionViewHolder extends RecyclerView.ViewHolder {
    TextView tv_name = itemView.findViewById(R.id.tv_name);
    TextView tv_num = itemView.findViewById(R.id.tv_num);
    ProgressBar pb = itemView.findViewById(R.id.pb);

    public ActionViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void onBind(ActionBean bean) {
        tv_name.setText(bean.name);
        tv_num.setText(bean.progress + "%");
        pb.setProgress(bean.progress);
    }
}
