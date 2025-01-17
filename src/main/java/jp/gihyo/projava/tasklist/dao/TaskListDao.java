package jp.gihyo.projava.tasklist.dao;

import jp.gihyo.projava.tasklist.controller.HomeController.TaskItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskListDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(TaskItem taskItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("tasklist");
        insert.execute(param);
    }

    public List<TaskItem> findAll() {
        String query = "SELECT * FROM TASKLIST";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = result.stream()
                .map((Map<String, Object> row) -> new TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("deadline").toString(),
                        (Boolean)row.get("done")
                        )).toList();
        return taskItems;
    }

    public int update(TaskItem taskItem) {
        return jdbcTemplate.update(
                "UPDATE TASKLIST SET task = ?, deadline = ?, done = ? WHERE id = ?",
                        taskItem.task(),
                        taskItem.deadline(),
                        taskItem.done(),
                        taskItem.id());
    }

    public int delete(String id) {
        return jdbcTemplate.update("DELETE FROM TASKLIST WHERE id = ?", id);
    }
}
