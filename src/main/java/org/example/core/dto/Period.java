package org.example.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Period {
    @JsonProperty(value = "date_from")
    private LocalDate dateFrom;
    @JsonProperty(value = "date_to")
    private LocalDate dateTo;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return dateFrom.format(formatter) + " - " + dateTo.format(formatter);
    }
}
