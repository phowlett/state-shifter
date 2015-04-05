package stateshifter.db;

import org.mongeez.Mongeez;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

@Component
public class MongoInitializer {
	
	private final MongoDbFactory mongoDbFactory;

	@Autowired
	public MongoInitializer(MongoDbFactory mongoDbFactory) {
		this.mongoDbFactory = mongoDbFactory;
		runMongeez();
	}

	private void runMongeez() {
		Mongeez mongeez = new Mongeez();
		mongeez.setFile(new ClassPathResource("/stateshifter/db/mongeez.xml"));
		mongeez.setMongo(mongoDbFactory.getDb().getMongo());
		mongeez.setDbName(mongoDbFactory.getDb().getName());
		mongeez.process();	
	}
	
}
