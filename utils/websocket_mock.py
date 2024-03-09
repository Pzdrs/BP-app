import asyncio
import json
import random

import websockets

from kml_extract import get_coords

if __name__ == '__main__':

    async def send_data(websocket, path):
        coords = get_coords('data/activa.kml')
        for i, (lat, lng) in enumerate(coords):
            await websocket.send(json.dumps({
                'data': {
                    'lat': lng,
                    'lng': lat,
                    'alt': random.randint(0, 1000),
                    'speed': random.randint(0, 150),
                    'course': random.randint(0, 360),
                    'fix': random.choice(['3d', '2d', '']),
                }
            }))
            await asyncio.sleep(1)


    start_server = websockets.serve(send_data, 'localhost', 80)

    asyncio.get_event_loop().run_until_complete(start_server)
    asyncio.get_event_loop().run_forever()
