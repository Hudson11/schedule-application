package io.github.Hudson11.scheduleapi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.OffsetDateTime;

@Entity(name = "contact")
@Getter @Setter
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 155, nullable = false)
    @NotEmpty(message = "Name is Required Field")
    private String name;

    @Column(length = 155, nullable = false)
    @NotEmpty(message = "Email is Required Field")
    private String email;

    @Column
    private Boolean favorite;

    @Column
    @Lob
    private byte[] image;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime created_at;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updated_at;

    @PrePersist
    public void prePersist () {
        setCreated_at(OffsetDateTime.now());
    }

    @PreUpdate
    public void preUpdate () {
        setUpdated_at(OffsetDateTime.now());
    }
}
