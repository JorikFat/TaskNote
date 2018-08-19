package ru.portal_systems.tasknote.listeners;

import android.widget.SeekBar;

import ru.portal_systems.tasknote.R;

/**
 * Created by 111 on 18.08.2017.
 */

public class MySeekBarCL implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id;
        switch (progress){
            case 0:
                id = R.id.task_tv_priorLow;
                break;
            case 1:
                id = R.id.task_tv_priorMedium;
                break;
            case 2:
                id = R.id.task_tv_priorHeight;
                break;
            default:
                id=-1;
        }
        if (id != -1) {
//            changeTextBold(id);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
