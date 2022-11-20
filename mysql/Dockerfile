#  spin up a mysql container
FROM mysql
# Add a database
ENV MYSQL_DATABASE usermanagerdb
# Add the content of the sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
COPY sql-scripts/ /docker-entrypoint-initdb.d/