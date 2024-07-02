package com.d_m.noted.notebooks;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.auth.models.UserSessionDetails;
import com.d_m.noted.shared.dtos.notebooks.CreateNotebookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notebooks")
public class NotebooksController {
    private final NotebooksService notebooksService;

    @Autowired
    public NotebooksController(NotebooksService notebooksService) {
        this.notebooksService = notebooksService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNotebook(
            @RequestBody CreateNotebookDto payload,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Notebook notebook = this.notebooksService.createNotebook(payload.title(), user.getId());
        return ResponseEntity.ok("Saved notebook " + notebook.getTitle());
    }

}
