package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePlaylistRequest {
    @NotBlank
    private String name;

    public CreatePlaylistRequest() {}

    public @NotBlank String getName() {return this.name;}
    public void SetName(@NotBlank String name) {this.name = name;}
}
