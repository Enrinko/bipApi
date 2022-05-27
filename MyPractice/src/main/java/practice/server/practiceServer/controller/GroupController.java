package practice.server.practiceServer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.exception.GroupExceptionValidation;
import practice.server.practiceServer.response.BaseResponse;
import practice.server.practiceServer.response.GroupListResponse;
import practice.server.practiceServer.response.GroupResponse;
import practice.server.practiceServer.service.GroupService;
import practice.server.practiceServer.utils.GroupValidation;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("bipapi/groups")
@AllArgsConstructor
public class GroupController {
    public GroupService service;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAll() {
        return ResponseEntity.ok(new GroupListResponse("Список всех групп:", true, service.getAll()));
    }

    @GetMapping("/byId")
    public ResponseEntity<BaseResponse> getById(@RequestParam Long id) {
        return ResponseEntity.ok(new GroupResponse("Группа по id", true, service.getById(id)));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> add(@RequestBody GroupEntity group) {
        try {
            GroupValidation.validation(group);
            if (service.getAll().contains(group)) {
                throw new GroupExceptionValidation("Такая группа уже существует");
            }
            service.save(group);
            return ResponseEntity.ok(new BaseResponse("Группа добавлена", true));
        } catch (GroupExceptionValidation exc) {
            return ResponseEntity.badRequest().body(new BaseResponse(exc.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/addAll")
    public ResponseEntity<BaseResponse> addAll(@RequestBody List<GroupEntity> group) {
        try {
            for (int i = 0; i < group.size(); i++) {
                GroupValidation.validation(group.get(i));
            }
            if (service.getAll().contains(group)) {
                throw new GroupExceptionValidation("Такая группа уже существует");
            }
            service.saveAll(group);
            return ResponseEntity.ok(new BaseResponse("Группа добавлена", true));
        } catch (GroupExceptionValidation exc) {
            return ResponseEntity.badRequest().body(new BaseResponse(exc.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<BaseResponse> edit(@RequestBody GroupEntity group) {
        try {
            GroupValidation.validation(group);
            service.save(group);
            return ResponseEntity.ok(new BaseResponse("Группа отредактирована", true));
        } catch (GroupExceptionValidation e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new BaseResponse("Группа удалена", true));
        }   catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }
}
