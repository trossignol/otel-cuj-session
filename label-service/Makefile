default: build

dev:
	mvn clean quarkus:dev

build:
	mvn clean package && docker-compose build

run:
	docker-compose up -d

stop:
	docker-compose stop

restart: stop run