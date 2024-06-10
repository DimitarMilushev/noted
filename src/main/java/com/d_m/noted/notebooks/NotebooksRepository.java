package com.d_m.noted.notebooks;

import com.d_m.noted.notebooks.entities.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotebooksRepository extends JpaRepository<Notebook, Long> {
    Optional<Notebook> findByTitleAndUserId(String title, Long userId);
}
