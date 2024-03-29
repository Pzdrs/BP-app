import json
import random
import time

import paho.mqtt.client as mqtt

from credentials import *
from kml_extract import get_coords

FREQUENCY = 1000
SOURCE = "B0:A7:32:29:D4:60"

if __name__ == '__main__':

    client = mqtt.Client(clean_session=True, callback_api_version=mqtt.CallbackAPIVersion.VERSION2)

    client.username_pw_set(MQTT_USERNAME, MQTT_PASSWORD)
    print("Connecting...")
    client.connect("home.pycrs.cz", 1883, 60)
    client.loop_start()

    coords = get_coords('data/activa.kml')
    for i, (lat, lng) in enumerate(coords):
        client.publish('gnss', json.dumps({
            'source': SOURCE,
            'lat': lat, 'lng': lng, 'alt': 0,
            'speed': random.randint(0, 150), 'course': random.randint(0, 360),
        }))
        print(f'Published {lat}, {lng}')
        time.sleep(FREQUENCY / 1000)

    client.disconnect()
