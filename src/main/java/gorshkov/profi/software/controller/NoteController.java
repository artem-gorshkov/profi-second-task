package gorshkov.profi.software.controller;

import gorshkov.profi.software.config.TitleProperty;
import gorshkov.profi.software.data.Note;
import gorshkov.profi.software.data.NoteDto;
import gorshkov.profi.software.data.NoteRepository;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class NoteController {
    private final NoteRepository noteRepository;
    private final TitleProperty titleProperty;

    @PostMapping
    @ApiOperation("Создать заметку")
    public Note createNote(NoteDto noteDto) {
        return noteRepository.save(convert(noteDto));
    }

    @GetMapping
    @ApiOperation("Получить все заметки или найти заметки по запросу")
    public Iterable<Note> getNotesOrSearch(String query) {
            var notes = noteRepository.findAll();
        if (query == null) {
            notes.forEach(this::setTitle);
            return notes;
        } else {
            return StreamSupport.stream(notes.spliterator(), false)
                    .filter(note -> note.getTitle().contains(query) || note.getContent().contains(query))
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("Получить заметку по id")
    public Note getNote(@PathVariable int id) {
        var note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        setTitle(note);
        return note;
    }

    @PutMapping("/{id}")
    @ApiOperation("Изменить заметку")
    public Note editNote(@PathVariable int id, NoteDto noteDto) {
        var note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        noteRepository.save(note);
        return note;
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удалить заметку")
    public void deleteNote(@PathVariable int id) {
        var note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        noteRepository.delete(note);
    }

    private Note convert(NoteDto noteDto) {
        Note note = new Note();
        note.setContent(noteDto.getContent());
        note.setTitle(noteDto.getTitle());
        return note;
    }

    private void setTitle(Note note) {
        if (note.getTitle() == null) {
            note.setTitle(note.getContent().substring(0, titleProperty.getLength()));
        }
    }
}

