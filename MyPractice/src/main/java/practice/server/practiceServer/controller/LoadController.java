package practice.server.practiceServer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.entity.LoadEntity;
import practice.server.practiceServer.entity.SubjectEntity;
import practice.server.practiceServer.entity.TeacherEntity;
import practice.server.practiceServer.exception.LoadExceptionValidation;
import practice.server.practiceServer.model.LoadModel;
import practice.server.practiceServer.response.BaseResponse;
import practice.server.practiceServer.response.FinalLoadResponse;
import practice.server.practiceServer.response.LoadListResponse;
import practice.server.practiceServer.response.LoadResponse;
import practice.server.practiceServer.service.GroupService;
import practice.server.practiceServer.service.LoadService;
import practice.server.practiceServer.service.SubjectService;
import practice.server.practiceServer.service.TeacherService;
import practice.server.practiceServer.utils.LoadValidation;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("bipapi/loads")
@AllArgsConstructor
public class LoadController {
    public LoadService service;
    public GroupService groupService;
    public TeacherService teacherService;
    public SubjectService subjectService;

    public List<LoadModel> toModel (List<LoadEntity> toModelList) {
        List<LoadModel> model = new ArrayList<>();
        for (int i = 0; i < toModelList.size(); i++) {
            LoadEntity toModel = toModelList.get(i);
            model.add(new LoadModel(toModel.getId(),
                    toModel.getSemesterFirstHours(),
                    toModel.getSemesterSecondHours(),
                    toModel.getExamHours(),
                    toModel.getCourseworkHours(),
                    toModel.getDiplomaHours(),
                    toModel.getConsultationHours(),
                    toModel.getTeacherInLoad().getInitials(),
                    toModel.getSubjectInLoad().getSubjectName(),
                    toModel.getGroupInLoad().getGroupName()));
        }
        return model;
    }

    public LoadModel toModel (LoadEntity toModel) {
        return new LoadModel(toModel.getId(),
                toModel.getSemesterFirstHours(),
                toModel.getSemesterSecondHours(),
                toModel.getExamHours(),
                toModel.getCourseworkHours(),
                toModel.getDiplomaHours(),
                toModel.getConsultationHours(),
                toModel.getTeacherInLoad().getInitials(),
                toModel.getSubjectInLoad().getSubjectName(),
                toModel.getGroupInLoad().getGroupName());
    }

    public List<LoadEntity> fromModel (List<LoadModel> modelList) {
        List<LoadEntity> entity = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            LoadModel model = modelList.get(i);
            entity.add(new LoadEntity(model.getSemesterFirstHours(), model.getSemesterSecondHours(),
                    model.getExamHours(),model.getCourseworkHours(),model.getDiplomaHours(),model.getConsultationHours(),
                    teacherService.findByName(model.getTeacherInLoad()),
                    subjectService.findByName(model.getSubjectInLoad()),
                    groupService.findByName(model.getGroupInLoad())));
        }
        return entity;
    }

    public LoadEntity fromModel (LoadModel model) {
        return new LoadEntity(model.getSemesterFirstHours(), model.getSemesterSecondHours(), model.getExamHours(),
                model.getCourseworkHours(), model.getDiplomaHours(), model.getConsultationHours(),
                teacherService.findByName(model.getTeacherInLoad()),
                subjectService.findByName(model.getSubjectInLoad()),
                groupService.findByName(model.getGroupInLoad()));
    }
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAll() {
        return ResponseEntity.ok(new LoadListResponse("Список всей нагрузки:", true, toModel(service.getAll())));
    }

    @GetMapping("/byId")
    public ResponseEntity<BaseResponse> getById(@RequestParam Long id) {
        return ResponseEntity.ok(new LoadResponse("Нагрузка по id", true, toModel(service.getById(id))));
    }

    @GetMapping("/finalLoadBy")
    public ResponseEntity<BaseResponse> getFinalLoadByTeacher(@RequestParam String groupname, @RequestParam String initials, @RequestParam String subjectname) {
        return ResponseEntity.ok(new FinalLoadResponse("Группа по id", true, service.finalLoadOfTeacher(service.getLoadBy(groupname, initials, subjectname))));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> add(@RequestBody LoadModel loadModel) {
        try {
            LoadEntity load = fromModel(loadModel);
            LoadValidation.validation(load);
            if (service.getAll().contains(load)) {
                throw new LoadExceptionValidation("Такая нагрузка уже существует");
            }
            service.save(load);
            return ResponseEntity.ok(new BaseResponse("Нагрузка добавлена", true));
        } catch (LoadExceptionValidation exc) {
            return ResponseEntity.badRequest().body(new BaseResponse(exc.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/addAll")
    public ResponseEntity<BaseResponse> addAll(@RequestBody List<LoadModel> loadModels) {
        try {
            List<LoadEntity> loads = fromModel(loadModels);
            for (int i = 0; i < loads.size(); i++) {
                LoadValidation.validation(loads.get(i));
            }
            if (service.getAll().contains(loads)) {
                throw new LoadExceptionValidation("Такая нагрузка уже существует");
            }
            service.saveAll(loads);
            return ResponseEntity.ok(new BaseResponse("Нагрузка добавлена", true));
        } catch (LoadExceptionValidation exc) {
            return ResponseEntity.badRequest().body(new BaseResponse(exc.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<BaseResponse> edit(@RequestBody LoadModel loadModel) {
        try {
            LoadEntity load = fromModel(loadModel);
            LoadValidation.validation(load);
            service.save(load);
            return ResponseEntity.ok(new BaseResponse("Нагрузка отредактирована", true));
        } catch (LoadExceptionValidation e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new BaseResponse("Нагрузка удалена", true));
        }   catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage(), false));
        }
    }
}
