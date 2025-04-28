package nl.colin.s3.beeple.domain;

public class User {
    private final Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    public User(Long id, String name, String email, String password, String role)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String name){
        this.id = id;
        this.name = name;

    }

    public Long getId(){return this.id;}
    public String getName(){return this.name;}
    public String getEmail(){return this.email;}
    public String getPassword(){return this.password;}
    public String getRole(){return this.role;}

    public void setName(String name){this.name = name;}
    public void setEmail(String email){this.email = email;}
    public void setPassword(String password){this.password = password;}
    public void setRole(String role){this.role = role;}
}
