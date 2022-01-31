# Build spring boot api
docker build -t load-testing-api . -f Dockerfile-api

# Build gatling app
docker build -t load-testing-gatling . -f Dockerfile-gatling


# Run spring boot api
docker run --name=load-testing-api -dp 8080:8080 load-testing-api

# Run gatling app
docker run --name=load-testing-gatling --network="host" -d load-testing-gatling


# Extract gatling reports
docker cp load-testing-gatling:/usr/src/app/target/gatling/ ./load-testing-gatling
