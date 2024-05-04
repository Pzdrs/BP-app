import json


def remove_every_nth(json_file, nth):
    # Load JSON data from file
    with open(json_file, 'r') as f:
        data = json.load(f)

    # Remove every nth document
    new_data = [doc for i, doc in enumerate(data) if (i + 1) % nth != 0]

    # Write modified data back to file
    with open(json_file, 'w') as f:
        json.dump(new_data, f, indent=4)


if __name__ == "__main__":
    remove_every_nth('data/mseno-chorusice-5s.json', 5)
