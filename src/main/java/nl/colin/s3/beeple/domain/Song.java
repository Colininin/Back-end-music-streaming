package nl.colin.s3.beeple.domain;

public class Song {
    private final Long id;
    private Artist artist;
    private String title;
    private String songPath;

    public Song(Long id, Artist artist, String title, String songPath) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.songPath = songPath;
    }

    public Long getId() {return this.id;}
    public Artist getArtist() {return this.artist;}
    public String getTitle() {return this.title;}
    public String getSongPath() {return this.songPath;}

    public void setArtist(Artist artist) { this.artist = artist; }
    public void setTitle(String title) { this.title = title; }
    public void setSongPath(String songPath) { this.songPath = songPath; }
}
