package com.d_m.noted.notebooks.entities;

import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.entities.BaseDatedEntity;
import com.d_m.noted.users.entities.UserData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Data
@Entity
public class Notebook extends BaseDatedEntity {
    @Column(nullable = false)
    @NotNull
    String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserData user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "notebook")
    List<Note> notes;
}
