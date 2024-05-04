import datetime
import json
from pymongo import MongoClient
from bson import ObjectId


def import_json_to_mongodb(json_file):
    client = MongoClient('10.0.0.15', 27017)

    with open(json_file, 'r') as f:
        data = json.load(f)

    for i, dp in enumerate(data):
        client['es-gps'].datapoints.insert_one({
            'lat': dp['lat'], 'lng': dp['lng'], 'alt': dp['alt'],
            'source': ObjectId(dp['source']['$oid']),
            'timestamp': datetime.datetime.strptime(dp['timestamp']['$date'], '%Y-%m-%dT%H:%M:%S.%fZ'),
            'course': dp['course'], 'speed': dp['course']
        })
        print(f'Inserted {i}')


if __name__ == "__main__":
    import_json_to_mongodb('data/mseno-chorusice-5s.json')
