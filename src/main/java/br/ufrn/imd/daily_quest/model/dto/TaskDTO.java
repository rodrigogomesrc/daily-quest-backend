package br.ufrn.imd.daily_quest.model.dto;

import br.ufrn.imd.daily_quest.model.enums.PriorityEnum;

public record TaskDTO(String text,
                      PriorityEnum priority,
                      String dueDate,
                      String imgLink,
                      int reward){
}
