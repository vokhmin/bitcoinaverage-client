package net.vokhmin.ba.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder(builderClassName = "Builder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BATicketRes {

    @JsonCreator
    public BATicketRes(String ticket) {
        this.ticket = ticket;
    }

    private String ticket;

}
