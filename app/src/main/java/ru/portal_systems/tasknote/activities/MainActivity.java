package ru.portal_systems.tasknote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.text.SimpleDateFormat;

import ru.portal_systems.tasknote.InterfaceTaskDB;
import ru.portal_systems.tasknote.R;
import ru.portal_systems.tasknote.TaskArrayAdapter;
import ru.portal_systems.tasknote.TaskDBHandler;
import ru.portal_systems.tasknote.TaskManager;
import ru.portal_systems.tasknote.model.Task;

import static ru.portal_systems.tasknote.CodeHelper.C_EDIT;
import static ru.portal_systems.tasknote.CodeHelper.C_NEW;
import static ru.portal_systems.tasknote.CodeHelper.TL_NUM_EDIT;
import static ru.portal_systems.tasknote.CodeHelper.T_DESCRIPTION;
import static ru.portal_systems.tasknote.CodeHelper.T_NAME;
import static ru.portal_systems.tasknote.CodeHelper.T_PRIOR;
import static ru.portal_systems.tasknote.CodeHelper.T_TERM;

public class MainActivity extends AppCompatActivity {

    public TaskManager taskManager;
    InterfaceTaskDB taskDB;

    public static ArrayAdapter taskAAactual;
    public static ArrayAdapter taskAAtermOver;
    public static ArrayAdapter taskAAall;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startAddTask();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initFields();

        int layoutID = R.layout.row_layout;
        taskAAactual = new TaskArrayAdapter(this, layoutID, taskManager.actualTaskList);
        taskAAtermOver = new TaskArrayAdapter(this, layoutID, taskManager.termOverTaskList);
        taskAAall = new TaskArrayAdapter(this, layoutID, taskManager.allTaskList);

        ((ListView) findViewById(R.id.taskList_actual)).setAdapter(taskAAactual);
        ((ListView) findViewById(R.id.taskList_actual)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // TODO: 22.08.2017 написать отдельный класс
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task smallTask = (Task) taskAAactual.getItem(position);
                int idBig = taskAAall.getPosition(smallTask);
                startEditTask((Task) taskAAall.getItem(idBig));
            }
        });

        ((ListView) findViewById(R.id.taskList_termOver)).setAdapter(taskAAtermOver);
        ((ListView) findViewById(R.id.taskList_termOver)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task smallTask = (Task) taskAAtermOver.getItem(position);
                int idBig = taskAAall.getPosition(smallTask);
                startEditTask((Task) taskAAall.getItem(idBig));
            }
        });

        ((ListView) findViewById(R.id.taskList_all)).setAdapter(taskAAall);
        ((ListView) findViewById(R.id.taskList_all)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startEditTask((ru.portal_systems.tasknote.model.Task)taskAAall.getItem(position));
            }
        });

    }

//    todo Рефакторить!!!
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (resultCode == RESULT_OK) {
            if (requestCode == C_NEW) {
                Task newTask = new Task();
                newTask.setName(data.getStringExtra(T_NAME));
                newTask.setDescription(data.getStringExtra(T_DESCRIPTION));
                newTask.setPrioritet(data.getIntExtra(T_PRIOR, 0));
                newTask.setTerm(TaskManager.getDateFromString(data.getStringExtra(T_TERM)));

                // TODO: 25.08.2017 перенести в Presenter
                taskDB.createItem(newTask);
                taskManager.addTaskInLists(newTask);
                updateListViews();
            } else if (requestCode == C_EDIT) {
                Task editTask = taskManager.allTaskList.get(data.getIntExtra(TL_NUM_EDIT, -1));
                editTask.setName(data.getStringExtra(T_NAME));
                editTask.setDescription(data.getStringExtra(T_DESCRIPTION));
                editTask.setPrioritet(data.getIntExtra(T_PRIOR, 0));
                editTask.setTerm(TaskManager.getDateFromString(data.getStringExtra(T_TERM)));

                // TODO: 25.08.2017 перенести в Presenter
                taskDB.updateItem(editTask.getId(), editTask);
                taskManager.editTaskInLists(data.getIntExtra(TL_NUM_EDIT, -1), editTask);
                updateListViews();
            }
        }
    }

    private void initFields() {
        taskDB = new TaskDBHandler(this);
        taskManager = TaskManager.getInstance(taskDB.readAllItems());
    }

    //todo рефакторить
    private void initViews() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec ts1 = tabHost.newTabSpec("Актуальные");
        ts1.setIndicator("Актуальные");
        ts1.setContent(R.id.taskList_actual);
        tabHost.addTab(ts1);

        ts1 = tabHost.newTabSpec("Просроченые");
        ts1.setIndicator("Просроченные");
        ts1.setContent(R.id.taskList_termOver);
        tabHost.addTab(ts1);

        ts1 = tabHost.newTabSpec("Все");
        ts1.setIndicator("Все");
        ts1.setContent(R.id.taskList_all);
        tabHost.addTab(ts1);

        tabHost.setCurrentTab(0);
    }

    private void startEditTask(Task task){
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(TL_NUM_EDIT, taskAAall.getPosition(task));
        startActivityForResult(intent, C_EDIT);
    }

    private void startAddTask() {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivityForResult(intent, C_NEW);
    }

    //// TODO: 22.08.2017 переписать под логику, а не пушкой
    public void updateListViews(){
        taskAAall.notifyDataSetChanged();
        taskAAactual.notifyDataSetChanged();
        taskAAtermOver.notifyDataSetChanged();
    }
}
