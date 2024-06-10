package com.d_m.noted.notebooks;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.shared.dtos.notebooks.CreateNotebookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notebooks")
public class NotebooksController {
    private final NotebooksService notebooksService;

    @Autowired
    public NotebooksController(NotebooksService notebooksService) {
        this.notebooksService = notebooksService;
    }

    @PostMapping("/create-notebook")
    public ResponseEntity<String> createNotebook(@RequestBody CreateNotebookDto payload) {
        final Notebook notebook = this.notebooksService.createNotebook(payload);
        return ResponseEntity.ok("success");
    }

}
