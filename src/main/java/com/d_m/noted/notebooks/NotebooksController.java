package com.d_m.noted.notebooks;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.auth.models.UserSessionDetails;
import com.d_m.noted.shared.dtos.notebooks.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notebooks")
public class NotebooksController {
    private final NotebooksService notebooksService;
    private final NotebooksMapper mapper;

    @PostMapping
    public ResponseEntity<GetNotebookByUserIdResponseDto> createNotebook(
            @RequestBody CreateNotebookDto payload,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Notebook notebook = this.notebooksService.createNotebook(payload.title(), user.getId());
        final var response = this.mapper.notebookToGetNotebookByUserIdResponseDto(notebook);
        //TOdO: create separate dto
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotebook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        this.notebooksService.deleteById(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateNotebookTitleResponseDto> updateNotebookTitle(
            @PathVariable Long id,
            @RequestBody UpdateNotebookTitleDto payload,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Notebook notebook = this.notebooksService.updateTitle(id, payload.title(), user.getId());
        final UpdateNotebookTitleResponseDto response = this.mapper.notebookToUpdateNotebookTitleResponseDto(notebook);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<GetNotebookDetailsResponseDto> getNotebookDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Notebook notebook = this.notebooksService.getById(id, user.getId());
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
