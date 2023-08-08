package com.candyshop.dto;

import com.candyshop.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(description = "Order Create DTO")
public class OrderCreateDto {

    @Schema(description = "User id", example = "1")
    private Long userId;

    @Schema(description = "Client address",
            example = "Soligorsk, Komsomola 77-77")
    @NotNull(message = "Address must be not null")
    private String address;
    private Status status;

}
