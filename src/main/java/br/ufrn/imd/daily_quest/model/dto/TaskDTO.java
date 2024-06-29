package br.ufrn.imd.daily_quest.model.dto;

import br.ufrn.imd.daily_quest.model.enums.PriorityEnum;
import br.ufrn.imd.daily_quest.model.enums.TaskStatusEnum;

public record TaskDTO(String text,
                      PriorityEnum priority,
                      TaskStatusEnum status,
                      String dueDate,
                      String imgLink,
                      int reward){
}
