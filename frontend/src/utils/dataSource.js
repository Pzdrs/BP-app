export function getDisplayName(dataSource) {
    return dataSource.name || 'Unnamed data source';
}

export function getRandomHexColor() {
    return '#' + Math.floor(Math.random() * 16777215).toString(16);
}
