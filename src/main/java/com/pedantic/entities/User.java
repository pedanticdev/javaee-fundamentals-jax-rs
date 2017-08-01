package com.pedantic.entities;

import com.pedantic.services.SecurityUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "login_user")
@NamedQueries({@NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u ORDER BY u.name DESC"),
        @NamedQuery(name = User.FIND_BY_EMAIL, query = "SELECT u FROM User  u where u.email = :email"),
        @NamedQuery(name = User.FIND_BY_CREDENTIALS, query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password"),
})
@XmlRootElement
public class User {

    public static final String FIND_BY_EMAIL = "User.findByEmail";
    public static final String FIND_BY_CREDENTIALS = "User.findByEmailAndPassword";
    public static final String FIND_ALL = "User.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @NotNull
    @Column(nullable = false, length = 40)
    private String email;

    @NotNull
    @Column(nullable = false, length = 256)
    private String password;

    private String name;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
