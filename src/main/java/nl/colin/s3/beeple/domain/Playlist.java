package nl.colin.s3.beeple.domain;

import java.util.Set;

public class Playlist {
    private final Long id;
    private String name;
    private Set<Song> songs;

    public Playlist(Long id, String name, Set<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }

    public Long getId() {return this.id;}
    public String getName() {return this.name;}
    public Set<Song> getSongs() {return this.songs;}

    public void setName(String name) {this.name = name;}
    public void setSongs(Set<Song> songs) {this.songs = songs;}
}
