package practice.server.practiceServer.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.repo.GroupRepo;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class GroupService {
    public GroupRepo repo;

    public void saveAll(List<GroupEntity> groupList) {
        repo.saveAll(groupList);
    }

    public void save(GroupEntity group) {
        repo.save(group);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<GroupEntity> getAll() {
        return repo.findAll();
    }

    public GroupEntity findByName(String name) {
        return repo.findByGroupName(name);
    }

    public GroupEntity getById(Long id) {
        return repo.findById(id).isPresent() ? repo.findById(id).get() : null;
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}