# che
Messaging and Game Framework

## modules - schema
Standardised message formats to be built and deployed as stand alone jar / dependency for mobile clients

## modules - netty
Communication point for mobile clients with SSL layer and JSON message parsing

## modules - hazelcast
RMI server hosting hazelcast instance

## modules - cheController
Internal Netty server connected to netty module, can manage objects and interact with Hazelcast server

## modules - erlang 
To do, likely to setup custom erlang_otp servers / actors as part of game processing

## modules - camel
Camel will manage all communications betwen applications as end point control 
