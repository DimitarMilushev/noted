package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import com.d_m.noted.shared.dtos.notes.GetNoteDataResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
public class NotesController {
    private final NotesService service;
    private final NotesMapper mapper;

    @Autowired
    public NotesController(
            NotesService service,
            NotesMapper mapper
            ) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNote(@RequestBody CreateNoteDto payload) {
        //TODO: needs to be logged in
        final Note note = this.service.createNote(payload);
        return ResponseEntity.ok(note.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetNoteDataResponseDto> getNoteData(
            @PathVariable Long id
    ) {
        //TODO: needs to be logged in
        // note.notebooks.first.user.id == userId
        final Note note = this.service.getById(id);
        final GetNoteDataResponseDto response = mapper.noteToGetNoteDataResponseDto(note);

        return ResponseEntity.ok(response);
    }
}
