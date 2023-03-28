for i in {1..15}
do
  printf "adding record to $1   \t for user%s ...  \t| response: " "$i"
  docker exec leaderboard-example-redis-1 redis-cli ZADD "leaderboard::$1" GT "$i$i.$i" "user$i"
  printf "adding record to all_time \t for user%s ...  \t| response: " "$i"
  docker exec leaderboard-example-redis-1 redis-cli ZADD leaderboard::all_time GT "$i$i.$i" "user$i"
done