# README.md file
cat <<EOF > README.md
# Quote App

A bash app that gets data from the [Alpha Vantage API](https://rapidapi.com/alphavantage/api/alpha-vantage) and saves it into a PostgreSQL Database.

## Usage

1. Make sure you have the following dependencies installed:
    - bash
    - curl
    - jq
    - psql

2. Set up your Alpha Vantage API key and PostgreSQL database. Modify the script accordingly.

3. Run the script with the following command:

   bash quote_app.sh API_KEY PSQL_HOST PSQL_PORT PSQL_DATABASE PSQL_USERNAME PSQL_PASSWORD SYMBOLS

# After assessment, thing I want to do:
1. Try to incorporate jq into the Retrieving value and setting them into variables
2. Functionality to use multiple symbol arguments
3. Complete checking if response is valid 

EOF