dep:
	docker compose -f docker-compose-dependencies.yml up

run:
	docker compose up

rebuild:
	docker compose build

test:
	./gradlew test

data:
	sh ./scripts/add_redis.sh 202302