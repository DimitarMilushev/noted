package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByTitleAndNotebookId(String title, Long notebookId);

    /**
     * @return Returns the first 5 notes, filtered by userId and ordered in descending order by updated_at date.
     *
     * @param id userId
     */
    List<Note> findTop5ByNotebook_User_IdOrderByUpdatedAtDesc(Long id);
}
