{ackid:1,user:{uid:"B@14eefb31"},location:{utm:"",subutm:"",long:15.0, lat:-15.0, speed:0,altitude:1}}
{ackid:1,user:{uid:""},location:{utm:"",subutm:"",long:15.0, lat:-15.0, speed:0,altitude:1}, generic:{type:"1",value:"",state:"",uid:""}}


basically i need tests again to test refactor.

its going to be a lengthy process but worthwhile.


server needs messages to models.  likely need to build into schema,.

anyway im supposed to play hockey need to stop

//useful...
{"che":{"acknowledge":{"acknowledge":{"ackid":"1","state":"MESSAGE","value":""}},"player":{"player":{"playerImage":"","playerKey":"","playerName":"Tim","utmLocation":{"utmLocation":{"altitude":10,"subUtm":{"utm":{"utmLong":"","utmLat":""}},"lat":1,"long":2,"speed":12.2,"utm":{"utm":{"utmLong":"","utmLat":""}}}}}}}}

//tests alliance
{"che":{"acknowledge":{"acknowledge":{"ackid":"1","state":"MESSAGE","value":""}},"alliance":{"alliance":{"allianceMembers":[],"allianceKey":"","allianceName":"team","state":"ALLIANCE_CREATE","value":""}},"player":{"player":{"playerImage":"","playerKey":"","playerName":"Tim","utmLocation":{"utmLocation":{"altitude":10,"subUtm":{"utm":{"utmLong":"","utmLat":""}},"lat":1,"long":2,"speed":12.2,"utm":{"utm":{"utmLong":"","utmLat":""}}}}}}}}




notes

can add rest of alliance cases.....

then need missile andz game object.

also can likely build the mobile client again.  ie salvage what i need from previous project.