package nl.colin.s3.beeple.domain;

public class Artist {
    private final Long id;
    private String name;

    public Artist(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {return this.id;}
    public String getName() {return this.name;}

    public void setName(String name) {this.name = name;}
}
