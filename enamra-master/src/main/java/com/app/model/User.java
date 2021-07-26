package com.app.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "active")
    private int active;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comments> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Blog> blogspost= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Quiz> listQuiz;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Result> listResult;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Mark> markList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile_pic profile_pic;

    public User() {
    }

    @java.beans.ConstructorProperties({"id", "email", "firstname", "lastname", "username", "password", "confirmPassword", "active", "roles"})
    public User(Long id, String email, String firstname, String lastname, String username, String password, String confirmPassword, int active, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.active = active;
        this.roles = roles;
    }

    public void addQuiz(Quiz quiz) {
        if (listQuiz == null) {
            listQuiz = new ArrayList<>();
        }

        listQuiz.add(quiz);
        quiz.setUser(this);
    }
    
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

//    public String toString() {
//        return "User(id=" + this.getId() +
//                ", email=" + this.getEmail() +
//                ", firstname=" + this.getFirstname() +
//                ", lastname=" + this.getLastname() +
//                ", username=" + this.getUsername() +
//                ", active=" + this.getActive() +  ")";
//    }

    public String toString() {
        return this.getFirstname() + " " + this.getLastname();
    }
}
