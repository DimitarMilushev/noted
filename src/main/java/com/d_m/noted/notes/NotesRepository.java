package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByTitleAndNotebookId(String title, Long notebookId);
}
