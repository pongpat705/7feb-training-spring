@startuml

entity client
entity api
database database

client -> api : POST /update with requestBody
activate api
api -> database : findById(pk)
database -> api : return result
alt notnull case
    api -> api : replace newvalue from requestBody to entity
    api -> database : save
    database -> api : return result
    alt success case
        api -> api : set ok code+message to ResponseModel
    else failed case
        api -> api : set error code+message to ResponseModel
    end
else null case
    api -> api : set error code+message to ResponseModel
end
api -> client : response
deactivate api


@enduml