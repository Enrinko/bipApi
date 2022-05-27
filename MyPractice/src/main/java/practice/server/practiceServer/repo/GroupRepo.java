package practice.server.practiceServer.repo;

import org.springframework.data.repository.CrudRepository;
import practice.server.practiceServer.entity.GroupEntity;

import java.util.List;
import java.util.Optional;

public interface GroupRepo extends CrudRepository<GroupEntity, Long> {
    @Override
    <S extends GroupEntity> List<S> saveAll(Iterable<S> entities);

    @Override
    List<GroupEntity> findAll();

    GroupEntity findByGroupName(String groupName);
}

class GroupRepoReworked implements GroupRepo {
    @Override
    public <S extends GroupEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends GroupEntity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<GroupEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<GroupEntity> findAll() {
        return null;
    }

    @Override
    public GroupEntity findByGroupName(String groupname) {
        return null;
    }

    @Override
    public Iterable<GroupEntity> findAllById(Iterable<Long> longs) {
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
    public void delete(GroupEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends GroupEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }
}