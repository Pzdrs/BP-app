import xml.etree.ElementTree as ET


def get_coords(kml_file):
    root = ET.parse(kml_file).getroot()
    coords_text = root[0][7][2][1].text

    final_coords = []

    for coord in coords_text.replace(' ', '').strip().split('\n'):
        lng, lat, _ = coord.split(',')
        final_coords.append((float(lat), float(lng)))

    return final_coords


if __name__ == '__main__':
    print(get_coords('data/activa.kml'))
