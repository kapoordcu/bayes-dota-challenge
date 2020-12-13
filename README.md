bayes-dota
==========

This is the [task](TASK.md).

Any additional information about your solution goes here.


###MatchId creation
1.  System.currentTimeMillis() is used for creating matchId <br/> Whenever `POST /api/match` is called with body text as a combat log
2.  The game data is aggregated to be saved in H2/Postgres DB


### DB Schema

 TABLE                 
------------
                                          
| HEROITEMS Table   | HEROKILLS Table   | HEROSPELLS Table  | HERODAMAGE Table
| ----------------  | ---------------   | ----------------  | -------------
| ITEM_ID(PK)       | KILL_ID(PK)       | SPELL_ID(PK)      | DAMAGE_ID(PK) 
| HERO              | HERO              | CASTS             | DAMAGE_INSTANCES
| ITEM              | KILLS             | HERO              | HERO
| MATCH_ID          | MATCH_ID          | MATCH_ID          | MATCH_ID
| TIMESTAMP         |                   | SPELL             | TARGET
|                   |                   | TARGET            | TOTAL_DAMAGE
                        
Rather than keeping foreign key relationships (for matchID, since this is query param for all endpoints), I kept this in all table because multiple rounds can be present for one match
     
     
## Flexibility
1) All prefixes are configurable in application.yml (in future hero prefix 'npc_dota_' could be changed to any other prefix easily)
2) Multiple prefixes are supported in application.yml
3) Entity class is used as model as well (with @JsonIgnore the RestApi response is limited to expected response)
4) Unexpected/Unparsable event log format is skipped and logged as debug
5) All other events are put into UNKNOWN category
6) Swagger UI can be used under `http://localhost:8080/swagger-ui.html` for easy UI access
7) POSTMAN collection json can also be imported to test API

### Missing Features
1) Due to 2 hours time limit, the fourth endpoint is not completely implemented, Saving in DB is completed, Retrieval is not complete<br/>
2) Testing could not be covered under time limit<br/>

## How to Run: DOCKER COMPOSE
The docker command to run in A or B is same, only docker file differs

i) docker-compose up
ii) docker-compose ps --> will give the IP address assigner by docker to your application
ii) Access the application on http://localhost:32768/swagger-ui.html
TWO WAYS TO DO THIS

### A (Make sure artifact exists in your local machine under target/)
a)  Use your Local machine to build the application (using mvn package)
b)  Use Docker to run the application

NOTE: "dockerfile: Dockerfile" set in docker-compose.yml BY DEFAULT 

### B (Change dockerfile name in docker-compose.yml to "dockerfile: Dockerfile-dev"
a)  Use Docker machine to build the application 
b)  Use Docker to run the application
Note: A bigger image will be created in this case since you are building the application in container
                   
                   



