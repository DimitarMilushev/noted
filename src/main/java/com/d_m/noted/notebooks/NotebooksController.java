package com.d_m.noted.notebooks;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.shared.dtos.notebooks.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notebooks")
public class NotebooksController {
    private final NotebooksService notebooksService;
    private final NotebooksMapper mapper;

    @PostMapping
    public ResponseEntity<CreateNotebookResponseDto> createNotebook(
            @RequestBody CreateNotebookDto payload,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final Notebook notebook = this.notebooksService.createNotebook(payload.title(), user.getId(), user);
        final CreateNotebookResponseDto response = this.mapper.notebookToCreateNotebookResponseDto(notebook);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotebook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        this.notebooksService.deleteById(id, user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateNotebookTitleResponseDto> updateNotebookTitle(
            @PathVariable Long id,
            @RequestBody UpdateNotebookTitleDto payload,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final Notebook notebook = this.notebooksService.updateTitle(id, payload.title(), user);
        final UpdateNotebookTitleResponseDto response = this.mapper.notebookToUpdateNotebookTitleResponseDto(notebook);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<GetNotebookDetailsResponseDto> getNotebookDetails(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final Notebook notebook = this.notebooksService.getById(id, user);
        final GetNotebookDetailsResponseDto response = this.mapper.notebookToGetNotebookDetailsResponseDto(notebook);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<GetNotebookByUserIdResponseDto>> getAllNotebooksForUser(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final List<Notebook> notebooks = this.notebooksService.findAllByUserId(user.getId());
        final List<GetNotebookByUserIdResponseDto> response = notebooks
                .stream()
                .map(this.mapper::notebookToGetNotebookByUserIdResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}
