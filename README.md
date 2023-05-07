# rate-a-place app with mysql as database


The mysql connection strings are configured in application.conf file. 

Install mysql 8.0* version or above and also use workbench for easy access to database through UI.

## Running backend application 
Run the application just by "./gradlew run" in the root folder of the project. And then access localhost:9000 from browser to access UI. 

## Running frontend application
Change directory to the 'ui' folder.
Run 'npm install' to download all the node modules.
Run 'npm start' to run the application on localhost:3000.

**NOTE: Do not use sbt run as it is not fixed for this project**
