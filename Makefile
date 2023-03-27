dep:
	docker compose -f docker-compose-dependencies.yml up

test:
	./gradlew test