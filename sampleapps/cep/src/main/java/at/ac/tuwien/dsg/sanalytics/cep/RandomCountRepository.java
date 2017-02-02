package at.ac.tuwien.dsg.sanalytics.cep;

import org.springframework.data.mongodb.repository.MongoRepository;

import at.ac.tuwien.dsg.sanalytics.events.RandomCount;

public interface RandomCountRepository extends MongoRepository<RandomCount, String> {

}
