package beblue.io.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="weeks")
public class Weeks implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer number_day;

    public Weeks() {
        this.id = null;
        this.name = "";
        this.number_day = null;
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
