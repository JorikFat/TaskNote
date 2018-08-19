package ru.portal_systems.tasknote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.portal_systems.tasknote.model.Task;

/**
 * Created by 111 on 18.08.2017.
 */

public class DataModel {
    public static List<Task> taskListAll = new ArrayList<>();
    public static List<Task> taskListActual = new ArrayList<>();
    public static List<Task> taskListTermOver = new ArrayList<>();

    static{
        String name = "Task";
        String description = "description of task";

        for (int i=0; i<10; i++){
            int num = 1+i;
            taskListAll.add(new Task("all "+ name + num, description + num, changeDays(i - 5), i % 3));
        }
    }

    public static Date changeDays(int days){
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH)+days;

        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
