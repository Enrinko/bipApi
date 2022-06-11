package practice.server.practiceServer.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import practice.server.practiceServer.entity.SubjectEntity;

import java.util.List;
import java.util.Optional;

public interface SubjectRepo extends CrudRepository<SubjectEntity, Long> {
    @Override
    <S extends SubjectEntity> List<S> saveAll(Iterable<S> entities);

    @Override
    List<SubjectEntity> findAll();

    List<SubjectEntity> findBySubjectName(String subjectName);

    @Modifying
    @Query(value = "truncate table subject", nativeQuery = true)
    void truncateMyTable();
}

class SubjectRepoReworkd implements SubjectRepo {
    @Override
    public <S extends SubjectEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends SubjectEntity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<SubjectEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<SubjectEntity> findAll() {
        return null;
    }

    @Override
    public List<SubjectEntity> findBySubjectName(String subjectName) {
        return null;
    }

    @Override
    public void truncateMyTable() {
    }

    @Override
    public Iterable<SubjectEntity> findAllById(Iterable<Long> longs) {
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
    public void delete(SubjectEntity entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
    }

    @Override
    public void deleteAll(Iterable<? extends SubjectEntity> entities) {
    }

    @Override
    public void deleteAll() {
    }
}