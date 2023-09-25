package org.example.core.dto.docs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.core.dto.Bank;
import org.example.core.dto.Currency;
import org.example.core.dto.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(setterPrefix = "set")
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Check {
    private UUID number;
    @JsonProperty(value = "transaction_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime localDateTime;
    @JsonProperty(value = "transaction_type")
    private TransactionType transactionType;
    @JsonProperty(value = "bank_from")
    private Bank bankFrom;
    @JsonProperty(value = "bank_to")
    private Bank bankTo;
    @JsonProperty(value = "account_from")
    private UUID accountFrom;
    @JsonProperty(value = "account_to")
    private UUID accountTo;
    private double sum;
    private Currency currency;


}
