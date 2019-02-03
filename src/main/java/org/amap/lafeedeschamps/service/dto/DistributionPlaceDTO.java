package org.amap.lafeedeschamps.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DistributionPlace entity.
 */
public class DistributionPlaceDTO implements Serializable {

    private Long id;

    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DistributionPlaceDTO distributionPlaceDTO = (DistributionPlaceDTO) o;
        if (distributionPlaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), distributionPlaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DistributionPlaceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
