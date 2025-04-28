package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class SearchRequest {
    @NotBlank
    private String input;

    public SearchRequest() {}

    public @NotBlank String getInput() {return this.input;}
    public void setInput(@NotBlank String input) {this.input = input;}
}
