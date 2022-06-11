package practice.server.practiceServer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import practice.server.practiceServer.entity.SubjectEntity;
import practice.server.practiceServer.repo.SubjectRepo;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class SubjectService {
    public SubjectRepo repo;

    public void saveAll(List<SubjectEntity> groupList) {
        repo.saveAll(groupList);
    }

    public void save(SubjectEntity group) {
        repo.save(group);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<SubjectEntity> getAll() {
        return repo.findAll();
    }

    public List<SubjectEntity> findByName(String name) {
        return repo.findBySubjectName(name);
    }

    public SubjectEntity getById(Long id) {
        return repo.findById(id).isPresent() ? repo.findById(id).get() : null;
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}