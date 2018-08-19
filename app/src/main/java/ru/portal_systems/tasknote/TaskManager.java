package ru.portal_systems.tasknote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.portal_systems.tasknote.model.Task;

import static ru.portal_systems.tasknote.CodeHelper.TL_ACTUAL;
import static ru.portal_systems.tasknote.CodeHelper.TL_ALL;
import static ru.portal_systems.tasknote.CodeHelper.TL_TERMOVER;

/**
 * Created by 111 on 19.08.2017.
 */

public class TaskManager {

    private static TaskManager taskManager;

    public List<Task> allTaskList;
    public List<Task> termOverTaskList;
    public List<Task> actualTaskList;

    private TaskManager(List<Task> allTaskList) {
        this.allTaskList = allTaskList;
        termOverTaskList = new ArrayList<>();
        actualTaskList = new ArrayList<>();
        createOtherLists();
    }

    //singleton
    public static TaskManager getInstance(List<Task> allTaskList){
        if (taskManager == null){
            taskManager = new TaskManager(allTaskList);
        }
        return taskManager;
    }

    //// TODO: 22.08.2017 исправить костыль
    public static TaskManager getInstance(){
        return taskManager;
    }

        /*
        * добавляет задание в списки и возвращает список, в который было добавленно задание:
        * TL_ALL - добавленно только в список allTaskList;
        * TL_TERMOVER - добавлено в allTaksList и в termOverTaskList;
        * TL_ACTUAL - добавленно в allTaskList и в actualTaskList;
        *
        * в allTaskList добавляется всегда
        */

    public int addTaskInLists(Task task){
        Date today = Calendar.getInstance().getTime();
        allTaskList.add(task);
        if (!task.isComplete()){
            if (task.getTerm().getTime() > today.getTime()){
                actualTaskList.add(task);
                return TL_ACTUAL;
            } else {
                termOverTaskList.add(task);
                return TL_TERMOVER;
            }
        }
        return TL_ALL;
    }

    public void editTaskInLists(int id, Task task){
        allTaskList.set(id, task);
        updateOtherList();
    }

    //// TODO: 22.08.2017 переписать под обновление элемента, а не под замену списка
    public void updateOtherList(){
        actualTaskList.clear();
        termOverTaskList.clear();
        createOtherLists();
    }

    public void createOtherLists(){
        Date today = DataModel.changeDays(-1);
        for (Task task : allTaskList) {
            if (!task.isComplete()) {
                if (task.getTerm().getTime() > today.getTime()) {
                    actualTaskList.add(task);
                } else {
                    termOverTaskList.add(task);
                }
            }
        }
    }

    public static Date getDateFromString(String string){
        Date rDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            rDate = dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rDate;
    }

    public static String getStringFromDate(Date date){
        String rString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        rString = dateFormat.format(date);
        return rString;
    }

/*
    public int getIdFromListAll(Task task){
        int idBig = taskAAall.getPosition(task);
        startEditTask((Task) taskAAall.getItem(idBig));
        return 0;
    }
*/
}
