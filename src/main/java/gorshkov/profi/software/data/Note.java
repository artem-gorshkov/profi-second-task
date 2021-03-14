package gorshkov.profi.software.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Note {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String content;
}
