package ru.portal_systems.tasknote;

import java.util.List;

import ru.portal_systems.tasknote.model.Task;

public interface InterfaceTaskDB {
    void createItem(Task task);
    Task readItem(long id);
    int updateItem(long id, Task task);
    void deleteItem(long id);
    Task readLastItem();

    List<Task> readAllItems();
}
