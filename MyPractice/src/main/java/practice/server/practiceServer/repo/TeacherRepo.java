package practice.server.practiceServer.repo;

import org.springframework.data.repository.CrudRepository;
import practice.server.practiceServer.entity.GroupEntity;
import practice.server.practiceServer.entity.TeacherEntity;

import java.util.List;
import java.util.Optional;

public interface TeacherRepo extends CrudRepository<TeacherEntity, Long> {
    @Override
    <S extends TeacherEntity> List<S> saveAll(Iterable<S> entities);

    @Override
    List<TeacherEntity> findAll();

    TeacherEntity findByInitials(String initials);
}
class TeacherRepoReworked implements TeacherRepo {
    @Override
    public <S extends TeacherEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends TeacherEntity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<TeacherEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<TeacherEntity> findAll() {
        return null;
    }

    @Override
    public TeacherEntity findByInitials(String initials) {
        return null;
    }

    @Override
    public Iterable<TeacherEntity> findAllById(Iterable<Long> longs) {
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
    public void delete(TeacherEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends TeacherEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
