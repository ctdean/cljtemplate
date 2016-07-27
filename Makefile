#
# App Makefile
# 

DATABASE ?= mydb

all: run

run:
	lein trampoline run

# Create the tables
init:
	createuser -s postgres -h localhost || exit 0
	createdb -Upostgres -h localhost $(DATABASE)
	createdb -Upostgres -h localhost $(DATABASE)_test

# Drop the tables
drop:
	dropdb -Upostgres -h localhost $(DATABASE) || exit 0
	dropdb -Upostgres -h localhost $(DATABASE)_test || exit 0

migrate:
	DATABASE_URL="jdbc:postgresql://localhost:5432/$(DATABASE)?user=postgres" \
	    lein run -m clams.migrate migrate
	DATABASE_URL="jdbc:postgresql://localhost:5432/$(DATABASE)_test?user=postgres" \
	    lein run -m clams.migrate migrate

# Nuke the existing databases and recreate
rebuild: drop init migrate

test:
	lein test

# Run test refresh with the correct profile and injections.
test_refresh:
	lein with-profile +test trampoline test-refresh

clean:
	rm -rf target

.PHONY: test clean

# Run in prod
heroku:
	lein uberjar
	lein heroku deploy

heroku_migrate:
	heroku run java -cp target/app.jar clams.migrate migrate

.PHONY: heroku heroku_migrate
