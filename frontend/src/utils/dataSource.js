export function getDisplayName(dataSource) {
    return dataSource.name || 'Unnamed data source';
}

export function getNameOrId(dataSource) {
    return dataSource.name || dataSource.id;
}

export function getRandomHexColor() {
    return '#' + Math.floor(Math.random() * 16777215).toString(16);
}
