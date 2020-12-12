bayes-dota
==========

This is the [task](TASK.md).

Any additional information about your solution goes here.


##MatchId creation/Retrieval 
1.  System.currentTimeMillis() is used as a seed to create a long MatchId <br/>
2.  The HeroKills data is saved in H2 database as three columns : HERO, KILLS and MATCHID  (Used to fetch the data by matchId in endpoint `GET /api/match/$match_id` )<br/>

