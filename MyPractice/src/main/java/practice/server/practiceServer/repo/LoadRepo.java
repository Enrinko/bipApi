package practice.server.practiceServer.repo;

import org.springframework.data.repository.CrudRepository;
import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.entity.LoadEntity;

import java.util.List;
import java.util.Optional;

public interface LoadRepo extends CrudRepository<LoadEntity, Long> {

    @Override
    <S extends LoadEntity> List<S> saveAll(Iterable<S> entities);

    @Override
    List<LoadEntity> findAll();

    List<LoadEntity> findByGroupInLoad_GroupNameOrTeacherInLoad_InitialsOrSubjectInLoad_SubjectName(String groupname, String initials, String subjectname);
}

class LoadRepoReworked implements LoadRepo {
    @Override
    public <S extends LoadEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends LoadEntity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<LoadEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<LoadEntity> findAll() {
        return null;
    }

    @Override
    public List<LoadEntity> findByGroupInLoad_GroupNameOrTeacherInLoad_InitialsOrSubjectInLoad_SubjectName(String groupname, String initials, String subjectname) {
        return null;
    }

    @Override
    public Iterable<LoadEntity> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(LoadEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends LoadEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

}