package com.d_m.noted.notes.entities;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.enums.NoteSharedAccessLevel;
import com.d_m.noted.shared.entities.BaseDatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
public class Note extends BaseDatedEntity {
    @Column(nullable = false)
    @NotNull
    String title;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notebook_id")
    Notebook notebook;

    @Column(length = 20000, nullable = false)
    @NotNull
    String content;

    @Builder.Default
    @Column(nullable = false)
    @NotNull
    boolean isShared = false;

    @Builder.Default
    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    NoteSharedAccessLevel accessLevel = NoteSharedAccessLevel.READ;
}
