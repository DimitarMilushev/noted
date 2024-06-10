package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
public class NotesController {
    private final NotesService service;

    @Autowired
    public NotesController(NotesService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNote(@RequestBody CreateNoteDto payload) {
        final Note note = this.service.createNote(payload);
        return ResponseEntity.ok(note.toString());
    }

}
