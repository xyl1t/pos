### New endpoints in this version

    GET localhost:8080/FinalProject/petrolstations?sortBy=location
    GET localhost:8080/FinalProject/cars?type=names
    GET localhost:8080/FinalProject/cars/{object_id}?listPositions=true

# Initialize

Generate initial cars and petrol stations with 

    GET localhost:8080/FinalProject/generateInstances?cars=10&petrolstations=3

Set 5 new positions for all or speicifc car

    GET localhost:8080/FinalProject/simulateMovement?numMovements=5&carId=65d8ffa03bb89733e2952bd3

# Car

Get all cars

    GET localhost:8080/FinalProject/cars

Get specific car

    GET localhost:8080/FinalProject/cars/{object_id}

Get car without positions

    GET localhost:8080/FinalProject/cars?type=names

Get car positions

    GET localhost:8080/FinalProject/cars/{object_id}?listPositions=true

Post car

    POST http://localhost:8080/FinalProject/cars
    Content-Type: application/json

    {
      "carName": "CarX",
      "positions": [
        {
          "location": {
            "type": "Point",
            "coordinates": [
              46.62435448533561,
              14.008668384034245
            ]
          },
          "datetime": {
            "$date": 1708723632000
          }
        }
      ]
    }

Delete car

    DELETE localhost:8080/FinalProject/cars/{object_id}

# Petrol Station

Get all petrol stations

    GET localhost:8080/FinalProject/petrolstations
    GET localhost:8080/FinalProject/petrolstations?sortBy=location
    GET localhost:8080/FinalProject/petrolstations?sortBy=name

Get specific petrol station

    GET localhost:8080/FinalProject/petrolstations/65d8ffa03bb89733e2952bd3

Post petrol station

    POST http://localhost:8080/FinalProject/petrolstations
    Content-Type: application/json

    {
      "stationName": "PetrolX",
      "positions": [
        {
          "type": "Point",
          "coordinates": [
            47.14389626431493,
            14.378986950406418
          ]
        }
      ]
    }

DELETE petrol station

    DELETE localhost:8080/FinalProject/petrolstations/65d8ffa03bb89733e2952bd3
