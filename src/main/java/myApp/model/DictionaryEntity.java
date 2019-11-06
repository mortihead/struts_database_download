package myApp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_dictionary", schema = "test")
public class DictionaryEntity {
    private int id;
    private String name;
    private float value;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "value")
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryEntity that = (DictionaryEntity) o;
        if (id != that.id) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (Float.toString(value).hashCode());
        return result;
    }
}
