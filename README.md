bayes-dota
==========

This is the [task](TASK.md).

Any additional information about your solution goes here.


###MatchId creation
1.  System.currentTimeMillis() is used for creating matchId <br/> Whenever POST `/api/match` is called with body text as a combat log
2.  The game data is aggregated to be saved in H2/Postgres DB


### DB Schema

 TABLE                 
------------
                                          
| HEROITEMS Table   | HEROKILLS Table   | HEROSPELLS Table  | HERODAMAGE Table
| ----------------  | ---------------   | ----------------  | -------------
| ITEM_ID(PK)       | KILL_ID(PK)       | SPELL_ID(PK)      | 
| HERO              | HERO              | CASTS             | 
| ITEM              | KILLS             | HERO              | 
| MATCH_ID          | MATCH_ID          | MATCH_ID          | 
| TIMESTAMP         |                   | SPELL             | 
|                   |                   | TARGET            | 
                        
Rather than keepin foreign key relationships (for matchID, since this is query param for all endpoints), I kept this in all table because multiple rounds can be present
     
     
## Flexibility
1) All prefixes are configurable in application.yml (in future hero prefix 'npc_dota_' could be changed to any other prefix easily)
2) Multiple prefixes are supported in application.yml
3) Entity class is used as model as well (with @JsonIgnore the RestApi response is limited to expected response)
4) Unexpected event log format is skipped and logged as debug
5) All other events are put into UNKNOWN category
6) Swagger UI can be used under `http://localhost:8080/swagger-ui.html` for easy UI access
7) POSTMAN collection json can also be imported to test API

### Missing Features
1) Due to 2 hour time limit, the fourth endpoint is not implemented<br/>
2) Testing could not be covered under time limit<br/>
3) We could also dockerize the application<br/>
                   
                   



