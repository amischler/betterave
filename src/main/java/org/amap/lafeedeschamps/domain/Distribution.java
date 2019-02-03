package org.amap.lafeedeschamps.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Distribution.
 */
@Entity
@Table(name = "distribution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Distribution implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy = "distribution")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("distributions")
    private DistributionPlace place;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "distribution_users",
               joinColumns = @JoinColumn(name = "distribution_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Distribution date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public Distribution text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Distribution comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Distribution addComments(Comment comment) {
        this.comments.add(comment);
        comment.setDistribution(this);
        return this;
    }

    public Distribution removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setDistribution(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public DistributionPlace getPlace() {
        return place;
    }

    public Distribution place(DistributionPlace distributionPlace) {
        this.place = distributionPlace;
        return this;
    }

    public void setPlace(DistributionPlace distributionPlace) {
        this.place = distributionPlace;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Distribution users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Distribution addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public Distribution removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distribution distribution = (Distribution) o;
        if (distribution.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), distribution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Distribution{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
