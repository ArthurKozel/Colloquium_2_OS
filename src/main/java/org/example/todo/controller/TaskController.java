package org.example.todo.controller;

import org.example.todo.model.Task;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController
{
    private List<Task> tasks = new ArrayList<>();
    private long idCounter = 1;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);


    @GetMapping
    public List<Task> getAllTasks()
    {
        logger.info("Запрос на получение всех задач");
        return tasks;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task)
    {
        logger.info("Получен запрос на создание задачи: {}", task.getTitle());

        task.setId(idCounter++);

        if (task.getStatus() == null)
        {
            task.setStatus("todo");
        }

        tasks.add(task);
        logger.info("Задача успешно создана с ID: {}", task.getId());
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id)
    {
        logger.info("Запрос на получение задачи с ID: {}", id);
        for (Task t : tasks)
        {
            if (t.getId().equals(id))
            {
                logger.warn("Задача с ID {} не найдена в системе", id);
                return new ResponseEntity<>(t, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask)
    {
        logger.info("Запрос на обновление задачи с ID: {}", id);
        for (Task t : tasks)
        {
            if (t.getId().equals(id))
            {
                t.setTitle(updatedTask.getTitle());
                t.setDescription(updatedTask.getDescription());
                t.setStatus(updatedTask.getStatus());
                return new ResponseEntity<>(t, HttpStatus.OK);
            }
        }
        logger.warn("Задача с ID {} не найдена в системе", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable Long id, @RequestBody Task partialTask)
    {
        logger.info("Запрос на изменение задачи с ID: {}", id);
        for (Task t : tasks)
        {
            if (t.getId().equals(id))
            {
                if (partialTask.getTitle() != null)
                {
                    t.setTitle(partialTask.getTitle());
                }
                if (partialTask.getDescription() != null)
                {
                    t.setDescription(partialTask.getDescription());
                }
                if (partialTask.getStatus() != null)
                {
                    t.setStatus(partialTask.getStatus());
                }
                return new ResponseEntity<>(t, HttpStatus.OK);
            }
        }
        logger.warn("Задача с ID {} не найдена в системе", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id)
    {
        logger.info("Запрос на удаление задачи с ID: {}", id);
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));

        if (removed)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            logger.warn("Задача с ID {} не найдена в системе", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}