package practice.server.practiceServer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.server.practiceServer.entity.SubjectEntity;
import practice.server.practiceServer.exception.SubjectExceptionValidation;
import practice.server.practiceServer.response.BaseResponse;
import practice.server.practiceServer.response.SubjectListResponse;
import practice.server.practiceServer.response.SubjectResponse;
import practice.server.practiceServer.service.SubjectService;
import practice.server.practiceServer.utils.SubjectValidation;

import java.util.List;

@RestController
@RequestMapping("bipapi/subjects")
@AllArgsConstructor
public class SubjectController {
    public SubjectService service;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAll() {
        return ResponseEntity.ok(new SubjectListResponse("Список всех предметов:", true, service.getAll()));
    }

    @GetMapping("/byId")
    public ResponseEntity<BaseResponse> getById(@RequestParam Long id) {
        return ResponseEntity.ok(new SubjectResponse("Предмет по id", true, service.getById(id)));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> add(@RequestBody SubjectEntity subject) {
        try {
            SubjectValidation.validation(subject);
            service.save(subject);
            return ResponseEntity.ok(new BaseResponse("Предмет добавлена", true));
        } catch (SubjectExceptionValidation exc) {
            return ResponseEntity.badRequest().body(new BaseResponse(exc.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/addAll")
    public ResponseEntity<BaseResponse> addAll(@RequestBody List<SubjectEntity> subject) {
        try {
            for (int i = 0; i < subject.size(); i++){
                SubjectValidation.validation(subject.get(i));
            }
            service.saveAll(subject);
            return ResponseEntity.ok(new BaseResponse("Предмет добавлена", true));
        } catch (SubjectExceptionValidation exc) {
            return ResponseEntity.badRequest().body(new BaseResponse(exc.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<BaseResponse> edit(@RequestBody SubjectEntity subject) {
        try {
            SubjectValidation.validation(subject);
            service.save(subject);
            return ResponseEntity.ok(new BaseResponse("Предмет отредактирован", true));
        } catch (SubjectExceptionValidation e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new BaseResponse("Предмет удалён", true));
        }   catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }
}
