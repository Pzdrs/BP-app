import datetime
import random

import pymongo
from bson import ObjectId

from kml_extract import get_coords

SOURCE = '65da5ea2f0d49e189faa9097'
FILE = 'data/officeo.kml'

if __name__ == '__main__':
    MONGO_CLIENT = pymongo.MongoClient("mongodb://admin:admin@localhost:27017/")
    DATABASE = MONGO_CLIENT["es-gps"]

    coords = get_coords(FILE)
    for i, (lat, lng) in enumerate(coords):
        DATABASE.datapoints.insert_one({
            'lat': lat, 'lng': lng, 'alt': 0,
            'source': ObjectId(SOURCE),
            'timestamp': datetime.datetime.now(datetime.UTC) + datetime.timedelta(minutes=i,days=7),
            'course': random.randint(0, 360), 'speed': random.randint(0, 150)
        })
        print(f'Inserted {lat}, {lng}')
