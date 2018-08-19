package ru.portal_systems.tasknote;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.portal_systems.tasknote.model.Task;

/**
 * Created by 111 on 18.08.2017.
 */

public class TaskArrayAdapter extends ArrayAdapter {

    Context context;
    List<Task> taskList;

    public TaskArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        taskList = (List<Task>)objects;
    }

    //// TODO: 18.08.2017 оптимизировать через ViewHolder
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rView = convertView;
        if (rView == null){
            rView = ((Activity) context).getLayoutInflater().inflate(R.layout.row_layout, parent, false);
        }
        Task task = taskList.get(position);
        ((TextView) rView.findViewById(R.id.taskItem_name)).setText(task.getName());
        ((TextView) rView.findViewById(R.id.taskItem_descript)).setText(task.getDescription());
        ((TextView) rView.findViewById(R.id.taskItem_date)).setText(TaskManager.getInstance().getStringFromDate(task.getTerm()));
        ((ImageView) rView.findViewById(R.id.taskItem_img_status)).setImageResource(getResPrior(task.getPrioritet()));
        return rView;
    }

    private int getResPrior(int a){
        switch (a){
            case 0: return R.drawable.ic_signal_cellular_0_bar_black_24dp;
            case 1: return R.drawable.ic_signal_cellular_2_bar_black_24dp;
            case 2: return R.drawable.ic_signal_cellular_4_bar_black_24dp;
        }
        return -1;
    }
}
