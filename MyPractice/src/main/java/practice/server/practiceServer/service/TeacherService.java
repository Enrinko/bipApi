package practice.server.practiceServer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import practice.server.practiceServer.entity.TeacherEntity;
import practice.server.practiceServer.repo.TeacherRepo;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class TeacherService {
    public TeacherRepo repo;

    public void saveAll(List<TeacherEntity> groupList) {
        repo.saveAll(groupList);
    }

    public void save(TeacherEntity group) {
        repo.save(group);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<TeacherEntity> getAll() {
        return repo.findAll();
    }

    public TeacherEntity findByName(String name) {
        return repo.findByInitials(name);
    }

    public TeacherEntity getById(Long id) {
        return repo.findById(id).isPresent() ? repo.findById(id).get() : null;
    }

}
