# leaderboard-example

A leaderboard backend example in kotlin

# Run

- Run api service and dependencies
    - `make run`
    - prerequisite:
        - `docker`
        - Port `8080` for API, `6379` for Redis and `27017` for MongoDB

- Run dependencies only
    - `make dep`
    - For setting up dependencies to run API service on for example, IntelliJ

# APIs

- Add score
    - POST `/scores`
    - Header:
        - Authorization: `Bearer {userId}`
        - In a production backend service, an access token should be provided.
        - For simplicity, the `userId` is provided here to mimic both authorization and to identify the user.
    - Example request body:
      ```json
        {
          "score": 123.45
        }
      ```
    - cURL:

        - Command:
            - `curl --location 'localhost:8080/scores' --header 'Authorization: Bearer ${userId}' --header 'Content-Type: application/json' --data '{"score": ${score}}'`

        - Example:
            - `curl --location 'localhost:8080/scores' --header 'Authorization: Bearer user1' --header 'Content-Type: application/json' --data '{"score": 123.45}'`
- Get leaderboard
    - GET `/leaderboards/{name}`
    - `{name}` is either `all_time` for all time leaderboard or `YYYYMD` for monthly leaderboard
    - cURL:
        - Command: `curl --location 'localhost:8080/leaderboards/${name}'`
        - All time example: `curl --location 'localhost:8080/leaderboards/all_time'`
        - Monthly example: `curl --location 'localhost:8080/leaderboards/202303'`
    - Example response:
      ```json
        {
          "leaderboard": {
            "users": [
              {
                "id": "user8",
                "score": 3456789.0
              },
              {
                "id": "user1",
                "score": 123.45
              }
            ]
          }
        }
      ``` 

# Setup (Optional)

- By default, the leaderboards are empty for all time and current month while past leaderboards are not found.
- To generate data to leaderboard, use the script `sh ./scripts/add_redis.sh $MONTH` (
  e.g. `sh ./scripts/add_redis.sh 202302`) to generate redis data for a specific month.