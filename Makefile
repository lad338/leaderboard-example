dep:
	docker compose -f docker-compose-dependencies.yml up

run:
	docker compose up

build:
	docker compose build

test:
	./gradlew test
