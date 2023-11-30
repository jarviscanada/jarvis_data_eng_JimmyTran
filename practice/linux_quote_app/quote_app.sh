#!/bin/bash

# Setup and validate arguments
api_key=$1
#bf054731femsh35fa8d48cc951a8p11d1d6jsnb77c1a86a057
psql_host=$2
psql_port=$3
psql_database=$4
psql_username=$5
psql_password=$6
symbol=$7

 #Check # of args
if [ "$#" -ne 7 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Retrieve quote info from API and parse json with jq
# API returns Global Quote "object" with information needed inside, jq formats it so we only have contents.
quote_info=$(curl --request GET \
       	--url "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=${symbol}&datatype=json" \
       	--header "X-RapidAPI-Host: alpha-vantage.p.rapidapi.com" \
       	--header "X-RapidAPI-Key: ${api_key}" | jq '."Global Quote"')

# Retrieve values from quote info and set them into variables.
open=$(echo "$quote_info" | grep "02. open" | awk '{print $3}' | sed 's/,//g' | xargs)
high=$(echo "$quote_info" | grep "03. high" | awk '{print $3}' | sed 's/,//g' | xargs)
low=$(echo "$quote_info" | grep "04. low" | awk '{print $3}' | sed 's/,//g' | xargs)
price=$(echo "$quote_info" | grep "05. price" | awk '{print $3}' | sed 's/,//g' | xargs)
volume=$(echo "$quote_info" | grep "06. volume" | awk '{print $3}' | sed 's/,//g' | xargs)

# Testing
#echo "$quote_info"
#echo "$open"
#echo "$high"
#echo "$low"
#echo "$price"
#echo "$volume"

# Inserting values into database table
insert_stmt="INSERT INTO quotes
(
symbol,
open,
high,
low,
price,
volume
)
VALUES
(
'$symbol',
$open,
$high,
$low,
$price,
$volume
)"

# Set up env var for pql cmd
export PGPASSWORD=$psql_password

# Insert data into the database
psql -h $psql_host -p $psql_port -d $psql_database -U $psql_username -c "$insert_stmt"
exit $?