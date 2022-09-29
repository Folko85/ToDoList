package main.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class TaskModel {

    private Integer id;

    @NotBlank
    private String title;

    public Integer getId() {
        return id;
    }

    public TaskModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TaskModel setTitle(String title) {  //это (возвращение себя) для красивой записи в маппере
        this.title = title;
        return this;
    }
}
