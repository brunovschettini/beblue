# Beblue

* [Considerações](#considerações)
* [Desenvolvimento](#desenvolvimento)
* [Pedidos & Respostas](#pedidos--respostas)
* [HTTP Verbs](#http-verbs)
* [Responses](#responses)
* [Error handling](#error-handling)
* [Versions](#versions)
* [Record limits](#record-limits)
* [Request & Response Examples](#request--response-examples)
* [Mock Responses](#mock-responses)
* [JSONP](#jsonp)

## Considerações

Este documento fornece informações e exemplos para as API do desafio da Beblue.

* [Url da API](http://localhost:80/api/)

## Desenvolvimento

Para construir esse projeto utilizei as dependênicias disponibilizadas no site oficial do Spring, o desevolvimento foi feito usando as IDEs Netbeans e Spring Tools Suite

* [Spring](https://start.spring.io)

* JPA, REST, H2, mysql (não configurada como default), SpotifyAPI, Jackson, JSON e WEB

A base de dados foi usada [H2](http://www.h2database.com) (banco de dados em memória) para facilitar a construção (building) e permitir um teste mais rápido com menos configurações.

* [H2 Console](http://localhost/api/h2-console) ou [8080](http://localhost:8080/api/h2-console/)

O Tomcat (Versão 8.5) foi usado como o container da aplicação.

Depois da primeira execução as entidades serão criadas no banco de dados.

Há um CLIENT_ID e CLIENT_SECRET do spotify fixado no arquivo beblue.io.auth.SpotifyAuthorization.java, assim que executar pela primeira vez esse irá reaizar uma conexão com a Api do Spotify e popular as tabelas criadas.

### Na sequência: 

* 1: Weeks
* 2: Genres
* 3: WeeksSales (É a entidade que contêm os valores dos cashbacks + dias da semana + gênero musical)
* 4: Users (Um usuário admin, criei de forma mas estrutural do que de fato usual)
* 5: Albums (Cadastra os artistas para serem vínculados aos albúms, tem id único + o id do spotify para caso sejá necessário uma consulta mais detalhada)

## Pedidos & Respostas

### API Recursos

  - [GET /status](#get-status)
  - [GET /genres/](#get-genres) 
  - [GET /album/genre/[genre]/[offset]](#get-albumgenregenreoffset)
  - [GET /album/id/[id]](#get-albumsidid)
  - [GET /order/find/](#get-orderfind)
  - [GET /order/find/[query]](#get-orderfindquery)
  - [GET /order/id/[id]](#get-orderidid)
  - [POST /order/add](#get-orderadd)
  - [POST /order/add2](#get-orderadd2)
  - [DELETE /order/delete](#get-orderdelete)
  - [DELETE /order/item/delete](#get-orderitemdelete)

### GET /status

Exemplo: http://localhost/api/status

Response: HTTP 200 (OK)

### GET /genres

 - Consulta os gêneros musicais

Exemplo: http://localhost/api/genres

Response body:
    [
        {"id":1,"name":"classic"},
        {"id":2,"name":"acoustic"},
        {"id":3,"name":"afrobeat"},
        {"id":4,"name":"alt-rock"}
    ]
 

### GET /album/genre/[genre]/[offset]

 - Consultar o catálogo de discos de forma paginada, filtrando por gênero e ordenando de forma crescente pelo nome do disco

Exemplo: http://localhost/api/album/genre/[genre]/[offset]

Response body:
    [
        {
            "id": 1,
            "spotify_id": "id do album no spotify",
            "name": "Nome do album",
            "artist": {
                "id": null,
                "spotify_id": "id do artista no spotify",
                "name": "Nome do artista"
            },
            "genre": {
                "id": null,
                "name": "Nome do gênero"
            },
            "price": 0.00,
            "created_at": now()
        },
        {
            "id": 2,
            "spotify_id": "id do album no spotify",
            "name": "Nome do album",
            "artist": {
                "id": null,
                "spotify_id": "id do artista no spotify",
                "name": "Nome do artista"
            },
            "genre": {
                "id": null,
                "name": "Nome do gênero"
            },
            "price": 0.00,
            "created_at": now()
        }
    ]


### GET /album/genre/[genre]/[offset]

 - Consultar o disco pelo seu identificador

Exemplo: http://localhost/api/album/id/[id]

Response body:

    {
        "id": null,
        "spotify_id": "id do album no spotify",
        "name": "Nome do album",
        "artist": {
            "id": null,
            "spotify_id": "id do artista no spotify",
            "name": "Nome do artista"
        },
        "genre": {
            "id": null,
            "name": "Nome do gênero"
        },
        "price": 0.00,
        "created_at": now()
    } 

### GET /order/find/

 - Consultar todas as vendas efetuadas

Exemplo: http://localhost/api/order/find/

Response body:

    {
        "status_code": 1,
        "status": "info: list orders by range date",
        "result": {
            "total": 0,
            "total_cashback": 0,
            "ordersItems": []
        }
    }

Com resultados

Response body:

    {
        "status_code": 1,
        "status": "info: list orders by range date",
        "result": {
            "total": 234.31,
            "total_cashback": 0,
            "ordersItems": [
                {
                    "id": 3,
                    "order": {
                        "id": 1,
                        "user": {
                            "id": 1,
                            "name": "admin",
                            "login": "admin",
                            "created_at": 1549721442702
                        },
                        "created_at": 1549677600000
                    },
                    "album": {
                        "id": 3,
                        "spotify_id": "3wvoawKuMJw5ROGw92BS4X",
                        "name": "Grandes mestres da MPB",
                        "artist": {
                            "id": 3,
                            "spotify_id": "5JYtpnUKxAzXfHEYpOeeit",
                            "name": "Jorge Ben Jor"
                        },
                        "genre": {
                            "id": 80,
                            "name": "mpb"
                        },
                        "price": 85.15,
                        "created_at": 1549721443468
                    },
                    "cashback_percent_log": 30,
                    "cashback": 0,
                    "cost": 85.15,
                    "created_at": 1549677600000
                }
        }
    }

### GET /order/find/

 - Consultar todas as vendas efetuadas de forma paginada, filtrando pelo range de datas (inicial e final) da venda e ordenando de forma decrescente pela data da venda;

Example: http://localhost/api/order/find/{"start_date":"01-01-1900","end_date":"01-01-1900"}

Response body:

    {
        "status_code": 1,
        "status": "info: list orders by range date",
        "result": {
            "total": 234.31,
            "total_cashback": 0,
            "ordersItems": [
                {
                    "id": 3,
                    "order": {
                        "id": 1,
                        "user": {
                            "id": 1,
                            "name": "admin",
                            "login": "admin",
                            "created_at": 1549721442702
                        },
                        "created_at": 1549677600000
                    },
                    "album": {
                        "id": 3,
                        "spotify_id": "3wvoawKuMJw5ROGw92BS4X",
                        "name": "Grandes mestres da MPB",
                        "artist": {
                            "id": 3,
                            "spotify_id": "5JYtpnUKxAzXfHEYpOeeit",
                            "name": "Jorge Ben Jor"
                        },
                        "genre": {
                            "id": 80,
                            "name": "mpb"
                        },
                        "price": 85.15,
                        "created_at": 1549721443468
                    },
                    "cashback_percent_log": 30,
                    "cashback": 0,
                    "cost": 85.15,
                    "created_at": 1549677600000
                }
        }
    }

Method: GET
http://localhost:8080/api/order/find/{"start_date":"08-02-2019","end_date":"08-02-2019"}

Method: GET
http://localhost:8080/api/order/find/


{
    "status_code": 1,
    "status": "info: list orders by range date",
    "result": {
        "total": 309.08,
        "total_cashback": 0,
        "ordersItems": [
            {
                "id": 4,
                "order": {
                    "id": 1,
                    "user": {
                        "id": 1,
                        "name": "admin",
                        "login": "admin",
                        "created_at": 1549660551939
                    },
                    "created_at": 1549591200000
                },
                "album": {
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
                    "price": 83.77,
                    "created_at": 1549660552340
                },
                "cashback_percent_log": 25,
                "cashback": 0,
                "cost": 83.77,
                "created_at": 1549591200000
            },
            {
                "id": 3,
                "order": {
                    "id": 1,
                    "user": {
                        "id": 1,
                        "name": "admin",
                        "login": "admin",
                        "created_at": 1549660551939
                    },
                    "created_at": 1549591200000
                },
                "album": {
                    "id": 3,
                    "spotify_id": "3wvoawKuMJw5ROGw92BS4X",
                    "name": "Grandes mestres da MPB",
                    "artist": {
                        "id": 3,
                        "spotify_id": "5JYtpnUKxAzXfHEYpOeeit",
                        "name": "Jorge Ben Jor"
                    },
                    "genre": {
                        "id": 80,
                        "name": "mpb"
                    },
                    "price": 32.88,
                    "created_at": 1549660552336
                },
                "cashback_percent_log": 25,
                "cashback": 0,
                "cost": 32.88,
                "created_at": 1549591200000
            },
            {
                "id": 1,
                "order": {
                    "id": 1,
                    "user": {
                        "id": 1,
                        "name": "admin",
                        "login": "admin",
                        "created_at": 1549660551939
                    },
                    "created_at": 1549591200000
                },
                "album": {
                    "id": 1,
                    "spotify_id": "4gjq4aTa0Y4rbxCG5J4bSy",
                    "name": "O Sonho, a Vida, a Roda Viva! 50 Anos (Ao Vivo)",
                    "artist": {
                        "id": 1,
                        "spotify_id": "3SKTkAUNa3oUa2rkd8DAyM",
                        "name": "MPB4"
                    },
                    "genre": {
                        "id": 80,
                        "name": "mpb"
                    },
                    "price": 70.42,
                    "created_at": 1549660552318
                },
                "cashback_percent_log": 25,
                "cashback": 0,
                "cost": 70.42,
                "created_at": 1549591200000
            },
            {
                "id": 2,
                "order": {
                    "id": 1,
                    "user": {
                        "id": 1,
                        "name": "admin",
                        "login": "admin",
                        "created_at": 1549660551939
                    },
                    "created_at": 1549591200000
                },
                "album": {
                    "id": 2,
                    "spotify_id": "26Vd2zx3iCZVRHoCalDqXF",
                    "name": "The Best of Brazilian MPB",
                    "artist": {
                        "id": 2,
                        "spotify_id": "0LyfQWJT6nXafLPZqxe9Of",
                        "name": "Various Artists"
                    },
                    "genre": {
                        "id": 80,
                        "name": "mpb"
                    },
                    "price": 48.76,
                    "created_at": 1549660552318
                },
                "cashback_percent_log": 25,
                "cashback": 0,
                "cost": 48.76,
                "created_at": 1549591200000
            },
            {
                "id": 5,
                "order": {
                    "id": 1,
                    "user": {
                        "id": 1,
                        "name": "admin",
                        "login": "admin",
                        "created_at": 1549660551939
                    },
                    "created_at": 1549591200000
                },
                "album": {
                    "id": 5,
                    "spotify_id": "6mk3ZqOD5nHn3zYo1TzPmg",
                    "name": "Vozes Do Magro",
                    "artist": {
                        "id": 1,
                        "spotify_id": "3SKTkAUNa3oUa2rkd8DAyM",
                        "name": "MPB4"
                    },
                    "genre": {
                        "id": 80,
                        "name": "mpb"
                    },
                    "price": 73.25,
                    "created_at": 1549660552342
                },
                "cashback_percent_log": 25,
                "cashback": 0,
                "cost": 73.25,
                "created_at": 1549591200000
            }
        ]
    }
}


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

{
    "status_code": 1,
    "status": "success: order nº 1 removed",
    "result": null
}

# Extras - Excluir item de uma ordem de venda
Method: DELETE
http://localhost:8080/api/order/item/delete/
Content-Type: application/x-www-form-urlencoded
Param: id
Values: 1

{
    "status_code": 1,
    "status": "success: item nº 6 removed",
    "result": null
}


// add

[
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


http://localhost:8080/api/h2-console/


http://localhost:8080/api/album/genre/mpb/0

returno 

[{"id":4,"spotify_id":"1X42b0NEC8OPNnPKcJJgIY","name":"10 Anos Depois","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":60.32,"created_at":1549657735497},{"id":35,"spotify_id":"2NftEgtE2KfcaG26kGxAoZ","name":"40 Anos Ao Vivo (Ao Vivo; Teatro SESC Vila Mariana, São Paulo, May 17th, 2006)","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":20.00,"created_at":1549657735621},{"id":8,"spotify_id":"7p2JZV8u3EjXw5gQbyIiFf","name":"A um passo da MPB","artist":{"id":5,"spotify_id":"6arRXoJBfebp5a9VKlEATa","name":"Falcão"},"genre":{"id":80,"name":"mpb"},"price":54.97,"created_at":1549657735511},{"id":29,"spotify_id":"3mq22mmdE8xzU3ijOsVwrO","name":"Adivinha O Que É","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":47.63,"created_at":1549657735595},{"id":18,"spotify_id":"2UPtwS8Drk03niJyyAlxOy","name":"Amigo É Pra Essas Coisas - Ao Vivo","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":76.60,"created_at":1549657735549},{"id":30,"spotify_id":"0Wc3ec9fCBWbiZA5309Bq1","name":"Bate Boca - As Músicas De Tom Jobim & Chico Buarque","artist":{"id":7,"spotify_id":"177N2fB1WvNBCQ4epjMQ3O","name":"Quarteto Em Cy"},"genre":{"id":80,"name":"mpb"},"price":26.59,"created_at":1549657735599},{"id":50,"spotify_id":"6VgNfSfvtOIbULq33TNymb","name":"Bons Tempos, Hein?!","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":16.36,"created_at":1549657735666},{"id":7,"spotify_id":"49xT37CJ1ZuSsfkwVU0Yne","name":"Brazil - Mpb And Bossa Nova","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":81.58,"created_at":1549657735506},{"id":49,"spotify_id":"2WBXlD3F3LEkZ2Zddn2dF0","name":"Brazilian Modern Popular Music (MPB)","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":71.04,"created_at":1549657735663},{"id":23,"spotify_id":"0ISoCzFnAtQHvuikDCUM82","name":"Canto Dos Homens","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":27.40,"created_at":1549657735574},{"id":40,"spotify_id":"6wm0tLbPO4Gm2xgJfNsJvj","name":"Clássicos da Mpb","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":25.14,"created_at":1549657735638},{"id":12,"spotify_id":"7dwdTa8CwJhmv0IDKshjJo","name":"Clássicos na Voz do MPB4 (Ao Vivo)","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":68.51,"created_at":1549657735526},{"id":16,"spotify_id":"4a4iOtxCn80OOIw534ta7C","name":"Deixa Estar","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":41.46,"created_at":1549657735540},{"id":28,"spotify_id":"67PvnpqvgcmfYOrnG0XKYp","name":"Escape","artist":{"id":15,"spotify_id":"5nfxuTM0r56z5avS9JUrmu","name":"MPB Music"},"genre":{"id":80,"name":"mpb"},"price":2.89,"created_at":1549657735592},{"id":44,"spotify_id":"1v27xeOURRuqC1hWG0ijy3","name":"Fundamental - Mpb","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":28.60,"created_at":1549657735648},{"id":3,"spotify_id":"3wvoawKuMJw5ROGw92BS4X","name":"Grandes mestres da MPB","artist":{"id":3,"spotify_id":"5JYtpnUKxAzXfHEYpOeeit","name":"Jorge Ben Jor"},"genre":{"id":80,"name":"mpb"},"price":41.98,"created_at":1549657735494},{"id":45,"spotify_id":"1LFP9vHdwkUEFQmV398f5B","name":"Grandes mestres da MPB, Vol. 1","artist":{"id":20,"spotify_id":"0yFvXd36g5sNKYDi0Kkvl8","name":"Elis Regina"},"genre":{"id":80,"name":"mpb"},"price":89.31,"created_at":1549657735651},{"id":6,"spotify_id":"6Qk0MFoJADNJKGdDHNdgmi","name":"I Love MPB - Pra Gente Se Amar","artist":{"id":4,"spotify_id":"3qZ2n5keOAat1SoF6bHwmb","name":"Zeca Pagodinho"},"genre":{"id":80,"name":"mpb"},"price":1.74,"created_at":1549657735504},{"id":34,"spotify_id":"4Ltz1JlhfCVHrEj0B7xRSW","name":"Lost","artist":{"id":15,"spotify_id":"5nfxuTM0r56z5avS9JUrmu","name":"MPB Music"},"genre":{"id":80,"name":"mpb"},"price":42.41,"created_at":1549657735617},{"id":36,"spotify_id":"10ASXD1eHOkkIlEWkOu84Q","name":"MPB","artist":{"id":17,"spotify_id":"5Cz2cGmme7lTrQFTQ0y9Qg","name":"Group Som Brasil"},"genre":{"id":80,"name":"mpb"},"price":35.07,"created_at":1549657735625},{"id":46,"spotify_id":"5xYK9AjKgMVcrEHJNV2e3J","name":"MPB - O Melhor de Todos os Tempos","artist":{"id":21,"spotify_id":"1eUyabxMT9QaUwV8OjzdQA","name":"Os Cantores da Noite"},"genre":{"id":80,"name":"mpb"},"price":83.60,"created_at":1549657735654},{"id":42,"spotify_id":"02CiSA6VJG2jzh2dwbgzbw","name":"MPB Por Eles","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":22.44,"created_at":1549657735643},{"id":11,"spotify_id":"6xppV4dr2y6bcr2x8BJMmZ","name":"MPB em Cy","artist":{"id":7,"spotify_id":"177N2fB1WvNBCQ4epjMQ3O","name":"Quarteto Em Cy"},"genre":{"id":80,"name":"mpb"},"price":22.80,"created_at":1549657735523},{"id":47,"spotify_id":"52ynG0aWMJmplTkuPUA3z9","name":"MPB no Samba","artist":{"id":22,"spotify_id":"5nTsYDrBg6eP5NisjBZ06A","name":"Sambaxé"},"genre":{"id":80,"name":"mpb"},"price":36.02,"created_at":1549657735657},{"id":25,"spotify_id":"6fJTYEjoGtEqZK1xf3oagH","name":"MPB4 & Quarteto Em Cy Sem Limite","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":55.43,"created_at":1549657735581},{"id":19,"spotify_id":"0EVIshl2FVOMG6s0GT2z3E","name":"Maxximum - Mpb","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":23.00,"created_at":1549657735553},{"id":32,"spotify_id":"3kDmNvqWEKIjzappbDjW51","name":"Memories","artist":{"id":15,"spotify_id":"5nfxuTM0r56z5avS9JUrmu","name":"MPB Music"},"genre":{"id":80,"name":"mpb"},"price":38.70,"created_at":1549657735608},{"id":14,"spotify_id":"5TIkEblxSTBckjd4oMk27D","name":"Mestres da MPB","artist":{"id":8,"spotify_id":"26ZBNvIa4trB98ayVyhg7J","name":"Conjunto Época de Ouro"},"genre":{"id":80,"name":"mpb"},"price":38.06,"created_at":1549657735533},{"id":20,"spotify_id":"5fjui0DqhLYcqggOHLQwzQ","name":"Mestres da MPB","artist":{"id":10,"spotify_id":"5yrpqAQx2OwR62pNE7d5bm","name":"Dick Farney"},"genre":{"id":80,"name":"mpb"},"price":31.51,"created_at":1549657735559},{"id":21,"spotify_id":"4SOl0TdVO1PJSWfizvVI4l","name":"Mestres da MPB","artist":{"id":11,"spotify_id":"3pXm8H2lDUNFkmQMiJbx6F","name":"Candeia"},"genre":{"id":80,"name":"mpb"},"price":88.42,"created_at":1549657735565},{"id":24,"spotify_id":"7eegFYYyimU9sP7JKu8Uw2","name":"Mestres da MPB","artist":{"id":13,"spotify_id":"4PirWL5JYf8HerMVvmwuD0","name":"Jamelão"},"genre":{"id":80,"name":"mpb"},"price":57.62,"created_at":1549657735578},{"id":27,"spotify_id":"6iKX9ZwZjnRvvYj6LPc2Vl","name":"Mestres da MPB","artist":{"id":14,"spotify_id":"2CmCNGXVgGJh0gVarYTfzq","name":"Carmélia Alves e Gordurinha"},"genre":{"id":80,"name":"mpb"},"price":4.23,"created_at":1549657735588},{"id":33,"spotify_id":"7mdqEbTpJV56IDoL26BUDE","name":"Mestres da MPB","artist":{"id":16,"spotify_id":"0WuhMCeJWbIxhQmd7GlcVI","name":"Carlos Cachaca"},"genre":{"id":80,"name":"mpb"},"price":53.31,"created_at":1549657735613},{"id":38,"spotify_id":"0sAVZBhTARlrKIYP4uFI0s","name":"Mestres da MPB","artist":{"id":19,"spotify_id":"6mPswHpL1PqahOi0u1sHt5","name":"Emilinha Borba"},"genre":{"id":80,"name":"mpb"},"price":1.59,"created_at":1549657735633},{"id":9,"spotify_id":"5uQedNNUCIxVKbSQ7CMNX2","name":"Mestres da MPB - Nora Ney","artist":{"id":6,"spotify_id":"6n3vQEpNNzmYXd1pxeifl6","name":"Nora Ney"},"genre":{"id":80,"name":"mpb"},"price":53.33,"created_at":1549657735515},{"id":43,"spotify_id":"6IUSqMLpTX6zXBDXghbxTR","name":"Mestres da MPB - Vol. 2","artist":{"id":13,"spotify_id":"4PirWL5JYf8HerMVvmwuD0","name":"Jamelão"},"genre":{"id":80,"name":"mpb"},"price":62.67,"created_at":1549657735646},{"id":22,"spotify_id":"1BhPF9HsYN397PY48XIuRt","name":"Mestres da Mpb","artist":{"id":12,"spotify_id":"77ZUbcdoU5KCPHNUl8bgQy","name":"João Gilberto"},"genre":{"id":80,"name":"mpb"},"price":0.20,"created_at":1549657735569},{"id":13,"spotify_id":"03UEPwKpRNcuhuE34WeNH1","name":"Mpb Brazil: Brazil, Samba, Bossa Nova And Beyond / A Nova Canção Brasileira","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":59.24,"created_at":1549657735529},{"id":48,"spotify_id":"092EOmoBrX4WxBteFCBxp5","name":"O Carnaval Pra Valer da MPB","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":36.93,"created_at":1549657735660},{"id":15,"spotify_id":"2ZMQvTxST9tO8Tdf5EQ5Bt","name":"O Melhor da Música (MPB)","artist":{"id":9,"spotify_id":"0Ziniqmk3nwm9QnKH41xna","name":"Orquestra Albatroz"},"genre":{"id":80,"name":"mpb"},"price":57.40,"created_at":1549657735537},{"id":26,"spotify_id":"4PPaRDnOPxnvNjyzpoLc2t","name":"O Sonho, a Vida, a Roda Viva!","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":30.29,"created_at":1549657735584},{"id":1,"spotify_id":"4gjq4aTa0Y4rbxCG5J4bSy","name":"O Sonho, a Vida, a Roda Viva! 50 Anos (Ao Vivo)","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":69.43,"created_at":1549657735480},{"id":31,"spotify_id":"2H6k51biFLrlAsC984Jyjp","name":"Obras-Primas","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":64.14,"created_at":1549657735604},{"id":37,"spotify_id":"4pAMwBF9dqg5l4VhOiTKVk","name":"Recomeçar (feat. Pop & Mpb)","artist":{"id":18,"spotify_id":"4kAbl5IYvvDFKccxCcNBT4","name":"Marcella Bártholo"},"genre":{"id":80,"name":"mpb"},"price":45.82,"created_at":1549657735629},{"id":39,"spotify_id":"5YsF25eXyTRkfDn3kNDWTL","name":"Riquezas da MPB","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":30.22,"created_at":1549657735635},{"id":10,"spotify_id":"1dAiOtNpeTJOp1xpf9iB9A","name":"Sambando Na Mpb - Grandes Nomes do Pagode Interpretando o Melhor da Mpb","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":78.41,"created_at":1549657735519},{"id":2,"spotify_id":"26Vd2zx3iCZVRHoCalDqXF","name":"The Best of Brazilian MPB","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":69.48,"created_at":1549657735489},{"id":41,"spotify_id":"25hQ3BtosSEEAEg4mf5wTd","name":"Versões Mpb","artist":{"id":2,"spotify_id":"0LyfQWJT6nXafLPZqxe9Of","name":"Various Artists"},"genre":{"id":80,"name":"mpb"},"price":34.10,"created_at":1549657735640},{"id":17,"spotify_id":"72wGhrjvW5Mt6kFzI1todY","name":"Vira Virou","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":48.24,"created_at":1549657735545},{"id":5,"spotify_id":"6mk3ZqOD5nHn3zYo1TzPmg","name":"Vozes Do Magro","artist":{"id":1,"spotify_id":"3SKTkAUNa3oUa2rkd8DAyM","name":"MPB4"},"genre":{"id":80,"name":"mpb"},"price":26.12,"created_at":1549657735500}]


rettorn

http://localhost:8080/api/genres/

