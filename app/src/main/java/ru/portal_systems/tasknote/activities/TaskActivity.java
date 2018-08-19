package ru.portal_systems.tasknote.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import ru.portal_systems.tasknote.R;
import ru.portal_systems.tasknote.TaskManager;
import ru.portal_systems.tasknote.model.Task;

import static ru.portal_systems.tasknote.CodeHelper.*;



public class TaskActivity extends AppCompatActivity {

    private boolean isNew;
    Task task;

    //temp
    int numberEditable;

    //Views
    EditText nameTask;
    EditText descriptionTask;
    DatePicker datePicker;
    SeekBar seekBar;
    ConstraintLayout layoutEdit;
    Switch cSwitch;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                editTask();
                likeFinish();
//                changeLists(task);
//                finish();//
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initView();

        //TODO ДОБАВИТЬ КНОПКУ BACK

        Bundle extras = getIntent().getExtras();
        if (extras != null){//редактирование
            isNew = false;
            int index = extras.getInt(TL_NUM_EDIT);

            //todo удалить этот костыль
            numberEditable = index;

            task = TaskManager.getInstance().allTaskList.get(index);
            layoutEdit.setVisibility(View.VISIBLE);
            ((Switch)findViewById(R.id.task_sw_complete)).setChecked(task.isComplete());
        } else {//создание
            isNew = true;
            task = new Task();
            layoutEdit.setVisibility(View.GONE);
        }
        setViewValue(task);
    }

    private void initView(){
        nameTask = (EditText) findViewById(R.id.task_et_name);
        descriptionTask = (EditText) findViewById(R.id.task_et_descript);
        datePicker = (DatePicker) findViewById(R.id.task_datePicker);
        seekBar = (SeekBar) findViewById(R.id.task_sb_prioritet);
        cSwitch = (Switch) findViewById(R.id.task_sw_complete);
        layoutEdit = (ConstraintLayout) findViewById(R.id.layout_edit);
    }

    //todo рефакторить!!!
    private void setViewValue(Task task){
        int[] a = getNumbersDate(task.getTerm());
        nameTask.setText(task.getName());
        descriptionTask.setText(task.getDescription());
        datePicker.updateDate(a[0], a[1], a[2]);
        seekBar.setProgress(task.getPrioritet());
        setTextBold(task.getPrioritet());
        //// TODO: 18.08.2017 вынести в отдельный класс
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("MyLog", "SeekBar progressChanged");
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
                    changeTextBold(id);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //// TODO: 23.08.2017 рефакторить!!!
    private void setTextBold(int numTextView){
        TextView changeTextView = (TextView)findViewById(R.id.task_tv_priorLow);
        switch (numTextView){
            case 0:
                changeTextView = (TextView) findViewById(R.id.task_tv_priorLow);
                break;
            case 1:
                changeTextView = (TextView) findViewById(R.id.task_tv_priorMedium);
                break;
            case 2:
                changeTextView = (TextView) findViewById(R.id.task_tv_priorHeight);
                break;
        }
        changeTextView.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void changeTextBold(int id){
        //todo установить bold в текущее положение
        TextView tvLow = (TextView) findViewById(R.id.task_tv_priorLow);
        TextView tvMedium = (TextView) findViewById(R.id.task_tv_priorMedium);
        TextView tvHeight = (TextView) findViewById(R.id.task_tv_priorHeight);

        tvLow.setTypeface(null, Typeface.NORMAL);
        tvMedium.setTypeface(null, Typeface.NORMAL);
        tvHeight.setTypeface(null, Typeface.NORMAL);

        ((TextView)findViewById(id)).setTypeface(Typeface.DEFAULT_BOLD);
    }

    private int[] getNumbersDate(Date date){
        int[] rInt = new int[3];
        Calendar calendar = Calendar.getInstance();
        if (date != null){
            calendar.setTime(date);
        }
        rInt[0] = calendar.get(Calendar.YEAR);
        rInt[1] = calendar.get(Calendar.MONTH);
        rInt[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return rInt;
    }

    private void editTask(){
        task.setName(nameTask.getText().toString());
        task.setDescription(descriptionTask.getText().toString());
        task.setTerm(dateFromDatePicker());
        task.setPrioritet(seekBar.getProgress());
        task.setComplete(cSwitch.isChecked());
    }

    private void likeFinish(){
        Intent intent = new Intent();
        if (!isNew){
            intent.putExtra(TL_NUM_EDIT, numberEditable);
        }
        intent.putExtra(T_NAME, task.getName());
        intent.putExtra(T_DESCRIPTION, task.getDescription());
        intent.putExtra(T_PRIOR, task.getPrioritet());
        intent.putExtra(T_TERM, TaskManager.getInstance().getStringFromDate(task.getTerm()));
        setResult(RESULT_OK, intent);
        finish();
    }

    private Date dateFromDatePicker(){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return c.getTime();
    }
}
