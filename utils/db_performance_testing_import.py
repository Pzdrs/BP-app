import datetime
import random

import pymongo
from bson import ObjectId

SOURCE = '66139d36e23a4f2817e35ae6'

if __name__ == '__main__':
    MONGO_CLIENT = pymongo.MongoClient("mongodb://10.0.0.15:27017/")
    DATABASE = MONGO_CLIENT["es-gps"]

    for i in range(9000):
        DATABASE.datapoints.insert_one({
            'lat': 56.3215, 'lng': 14.26456, 'alt': 0,
            'source': ObjectId(SOURCE),
            'timestamp': datetime.datetime.now(datetime.UTC),
            'course': random.randint(0, 360), 'speed': random.randint(0, 150)
        })
        print(f'Inserted {i}')
