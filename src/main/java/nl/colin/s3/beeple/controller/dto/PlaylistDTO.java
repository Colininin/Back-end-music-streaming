package nl.colin.s3.beeple.controller.dto;


import jakarta.validation.constraints.NotNull;

public class PlaylistDTO {
    private Long id;
    @NotNull(message = "Name cannot be null")
    private String name;

    public PlaylistDTO() {}

    public Long getId(){return this.id;}
    public String getName(){return this.name;}

    public void setId(Long id){this.id = id;}
    public void setName(String name){this.name = name;}
}
