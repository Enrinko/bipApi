package practice.server.practiceServer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.server.practiceServer.entity.TeacherEntity;
import practice.server.practiceServer.exception.TeacherExceptionValidation;
import practice.server.practiceServer.response.BaseResponse;
import practice.server.practiceServer.response.TeacherListResponse;
import practice.server.practiceServer.response.TeacherResponse;
import practice.server.practiceServer.service.TeacherService;
import practice.server.practiceServer.utils.TeacherValidation;

@RestController
@RequestMapping("bipapi/teachers")
@AllArgsConstructor
public class TeacherController {
    public TeacherService service;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAll() {
        return ResponseEntity.ok(new TeacherListResponse("Список всех учителей:", true, service.getAll()));
    }

    @GetMapping("/byId")
    public ResponseEntity<BaseResponse> getById(@RequestParam Long id) {
        return ResponseEntity.ok(new TeacherResponse("Учитель по id", true, service.getById(id)));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> add(@RequestBody TeacherEntity teacher) {
        try {
            TeacherValidation.validation(teacher);
            service.save(teacher);
            return ResponseEntity.ok(new BaseResponse("Учитель добавлен", true));
        } catch (TeacherExceptionValidation exc) {
            return ResponseEntity.badRequest().body(new BaseResponse(exc.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<BaseResponse> edit(@RequestBody TeacherEntity teacher) {
        try {
            TeacherValidation.validation(teacher);
            service.save(teacher);
            return ResponseEntity.ok(new BaseResponse("Учитель отредактирован", true));
        } catch (TeacherExceptionValidation e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new BaseResponse("Учитель удалён", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }
}