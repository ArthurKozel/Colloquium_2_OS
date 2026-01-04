package org.example.todo.controller;

import org.example.todo.model.Task;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController
{
    private List<Task> tasks = new ArrayList<>();
    private long idCounter = 1;

    @GetMapping
    public List<Task> getAllTasks()
    {
        return tasks;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task)
    {
        task.setId(idCounter++);

        if (task.getStatus() == null)
        {
            task.setStatus("todo");
        }

        tasks.add(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id)
    {
        for (Task t : tasks)
        {
            if (t.getId().equals(id))
            {
                return new ResponseEntity<>(t, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask)
    {
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
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable Long id, @RequestBody Task partialTask)
    {
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
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id)
    {
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));

        if (removed)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}