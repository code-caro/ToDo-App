package com.cw.todocheck.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cw.todocheck.model.Task;
import com.cw.todocheck.util.TaskRoomDatabase;

import java.util.List;

public class DocheckRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    public DocheckRepository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> taskDao.insertTask(task));
    }

    public LiveData<Task> get(long id){ return taskDao.get(id);}

    public void update(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> taskDao.update(task));
    }

    public void delete(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> taskDao.delete(task));
    }

    public LiveData<List<Task>> getTasksSorted(String orderBy) {
        return taskDao.getTasksSorted(orderBy);
    }

    public LiveData<List<Task>> getTasksSortedByTaskAscending() {
        return taskDao.getTasksSortedByTaskAscending();
    }

    public LiveData<List<Task>> getTasksSortedByTaskDescending() {
        return taskDao.getTasksSortedByTaskDescending();
    }

    public LiveData<List<Task>> getTasksSortedByPriority() {
        return taskDao.getTasksSortedByPriority();
    }

    public LiveData<List<Task>> getTasksSortedByDueDate() {
        return taskDao.getTasksSortedByDueDate();
    }

    public LiveData<List<Task>> getTasksSortedByKeywords() {
       return taskDao.getTasksSortedByKeywords();
    }

    public LiveData<List<Task>> searchTasks(String query) {
        return taskDao.searchTasks(query);
    }

}
