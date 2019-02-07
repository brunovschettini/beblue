package beblue.io.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Weeks implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false, columnDefinition = "varchar default ''")
    private String name;

    @Column(nullable = false)
    private Integer number_day;

    public Weeks() {
    }

    public Weeks(Long id, String name, Integer number_day) {
        this.id = id;
        this.name = name;
        this.number_day = number_day;
    }

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

    public Integer getNumber_day() {
        return number_day;
    }

    public void setNumber_day(Integer number_day) {
        this.number_day = number_day;
    }

}
