package stateshifter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import stateshifter.model.Bug;

@RepositoryRestResource(path="bug")
public interface BugRepository extends MongoRepository<Bug, String> {

}
