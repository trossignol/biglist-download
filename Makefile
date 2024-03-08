default: run

clean:
	mvn clean

run-db:
	docker-compose up -d postgres

stop-db:
	docker-compose stop postgres

log-db:
	docker-compose logs -f postgres

clean-db: stop-db
	docker-compose rm -f postgres

dev: run-db
	mvn clean quarkus:dev

build:
	mvn clean package

run: run-db build
	java -jar ./target/quarkus-app/quarkus-run.jar

test-stream:
	curl http://localhost:8080/api/stream

test-client:
	curl http://localhost:8080/api/client

web:
	open http://localhost:8080/