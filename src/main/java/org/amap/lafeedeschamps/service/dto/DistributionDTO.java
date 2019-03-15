package org.amap.lafeedeschamps.service.dto;
import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Distribution entity.
 */
public class DistributionDTO implements Serializable {

    private Long id;

    private LocalDate date;

    @Lob
    private String text;

    private Instant endDate;

    private Instant startDate;

    private Integer minUsers;


    private Long placeId;

    private String placeName;

    private Set<UserDTO> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Integer getMinUsers() {
        return minUsers;
    }

    public void setMinUsers(Integer minUsers) {
        this.minUsers = minUsers;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long distributionPlaceId) {
        this.placeId = distributionPlaceId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String distributionPlaceName) {
        this.placeName = distributionPlaceName;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DistributionDTO distributionDTO = (DistributionDTO) o;
        if (distributionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), distributionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DistributionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", text='" + getText() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", minUsers=" + getMinUsers() +
            ", place=" + getPlaceId() +
            ", place='" + getPlaceName() + "'" +
            "}";
    }
}
