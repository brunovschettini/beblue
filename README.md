# beblue

#Banco de Dados




#Banco de Dados


# Consultar o catálogo de discos de forma paginada, filtrando por gênero e ordenando de forma crescente pelo nome do disco;
Method: GET

http://localhost:8080/api/album/genre/mpb/0
http://localhost:8080/api/album/genre/classic/0
http://localhost:8080/api/album/genre/rock/0
http://localhost:8080/api/album/genre/pop/0

# Consultar o disco pelo seu identificador;
Method: GET
http://localhost:8080/api/album/id/1

#Consultar todas as vendas efetuadas de forma paginada, filtrando pelo range de datas (inicial e final) da venda e ordenando de forma decrescente pela data da venda;

Method: GET
http://localhost:8080/api/order/find/{"start_date":"08-02-2019","end_date":"08-02-2019"}

Method: GET
http://localhost:8080/api/order/find/

#Consultar uma venda pelo seu identificador;
Method: GET
http://localhost:8080/api/order/id/1

#Registrar uma nova venda de discos calculando o valor total de cashback considerando a tabela.
Method: POST
http://localhost:8080/api/order/add  
Content-Type: application/json;charset=UTF-8
Param -> Albums: [
    {
        "id": 4,
        "spotify_id": "1X42b0NEC8OPNnPKcJJgIY",
        "name": "10 Anos Depois",
        "artist": {
            "id": 1,
            "spotify_id": "3SKTkAUNa3oUa2rkd8DAyM",
            "name": "MPB4"
        },
        "genre": {
            "id": 80,
            "name": "mpb"
        },
        "price": 7.23,
        "created_at": 1549645694118
    }
]
Raw: {"status_code":1,"status":"success: order nº 2 registered","result":{"total":7.23,"total_cashback":1.81,"ordersItems":[{"id":6,"order":{"id":2,"user":{"id":1,"name":"admin","login":"admin","created_at":1549657735042,"hibernateLazyInitializer":{}},"created_at":1549657865776},"album":{"id":4,"spotify_id":"1X42b0NEC8OPNnPKcJJgIY","name":"10 Anos Depois","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":7.23,"created_at":1549645694118},"cashback_percent_log":25.0,"cost":7.23,"created_at":1549657865784,"cashback":1.81}]}}

# Cria uma orde de venda com items de albums
Method: POST
http://localhost:8080/api/order/add2  
Content-Type: application/x-www-form-urlencoded
Param: albums_id
Values: [1,2,3,4,5] 

# Extras - Excluir uma ordem de venda
Method: DELETE
http://localhost:8080/api/order/delete/
Content-Type: application/x-www-form-urlencoded
Param: id
Values: 1

# Extras - Excluir item de uma ordem de venda
Method: DELETE
http://localhost:8080/api/order/item/delete/
Content-Type: application/x-www-form-urlencoded
Param: id
Values: 1
