package com.d_m.noted.notebooks;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.auth.models.UserSessionDetails;
import com.d_m.noted.shared.dtos.notebooks.CreateNotebookDto;
import com.d_m.noted.shared.dtos.notebooks.GetNotebookByUserIdResponseDto;
import com.d_m.noted.shared.dtos.notebooks.GetNotebookDetailsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/notebooks")
public class NotebooksController {
    private final NotebooksService notebooksService;
    private final NotebooksMapper mapper;

    @Autowired
    public NotebooksController(
            NotebooksService notebooksService,
            NotebooksMapper mapper
            ) {
        this.notebooksService = notebooksService;
        this.mapper = mapper;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNotebook(
            @RequestBody CreateNotebookDto payload,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Notebook notebook = this.notebooksService.createNotebook(payload.title(), user.getId());
        return ResponseEntity.ok("Saved notebook " + notebook.getTitle());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<GetNotebookDetailsResponseDto> getNotebookDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Notebook notebook = this.notebooksService.findByIdAndUserId(id, user.getId());
        final var response = this.mapper.notebookToGetNotebookDetailsResponseDto(notebook);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<GetNotebookByUserIdResponseDto>> getAllNotebooksForUser(
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final var notebooks = this.notebooksService.findAllByUserId(user.getId());

        final var response = new ArrayList<GetNotebookByUserIdResponseDto>();
        notebooks.forEach(nb -> response.add(this.mapper.notebookToGetNotebookByUserIdResponseDto(nb)));

        return ResponseEntity.ok(response);
    }
}
