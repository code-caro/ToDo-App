package com.cw.todocheck.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cw.todocheck.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM task_table WHERE task_table.task_ID == :id")
    LiveData<Task> get(long id);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);


    // Allgemeine Methode für sortierte Aufgaben
    @Query("SELECT * FROM task_table ORDER BY :orderBy")
    LiveData<List<Task>> getTasksSorted(String orderBy);

    @Query("SELECT * FROM task_table ORDER BY task ASC")
    LiveData<List<Task>> getTasksSortedByTaskAscending();

    @Query("SELECT * FROM task_table ORDER BY task DESC")
    LiveData<List<Task>> getTasksSortedByTaskDescending();

    @Query("SELECT * FROM task_table ORDER BY " +
            "CASE WHEN priority = 'HIGH' THEN 1 " +
            "WHEN priority = 'MEDIUM' THEN 2 " +
            "WHEN priority = 'LOW' THEN 3 " +
            "ELSE 4 END")
    LiveData<List<Task>> getTasksSortedByPriority();

    @Query("SELECT * FROM task_table ORDER BY task_table.due_date ASC")
    LiveData<List<Task>> getTasksSortedByDueDate();

    @Query("SELECT * FROM task_table ORDER BY task_table.keywords ASC")
    LiveData<List<Task>> getTasksSortedByKeywords();

    @Query("SELECT * FROM task_table WHERE task_table.task LIKE '%' || :query || '%' OR task_table.keywords LIKE '%' || :query || '%' OR task_table.due_date LIKE '%' || :query || '%'")
    LiveData<List<Task>> searchTasks(String query);

}
