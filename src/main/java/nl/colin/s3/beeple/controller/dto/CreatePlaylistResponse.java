package nl.colin.s3.beeple.controller.dto;

public class CreatePlaylistResponse {
    public CreatePlaylistResponse() {}

    private Long id;
    private String name;
    public Long getId() {return this.id;}
    public String getName() {return this.name;}

    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}
