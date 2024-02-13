import datetime

import pymongo
from bson import ObjectId

from kml_extract import get_coords

if __name__ == '__main__':
    MONGO_CLIENT = pymongo.MongoClient("mongodb://admin:admin@localhost:27017/")
    DATABASE = MONGO_CLIENT["es-gps"]

    coords = get_coords('data/activa.kml')
    for i, (lat, lng) in enumerate(coords):
        DATABASE.datapoints.insert_one({
            'lat': lat, 'lng': lng, 'alt': 0,
            'source': ObjectId('65c16c4b61bfc0617755e82a'),
            'timestamp': datetime.datetime.now(datetime.UTC) + datetime.timedelta(minutes=i)
        })
        print(f'Inserted {lat}, {lng}')
