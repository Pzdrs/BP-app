export function getDisplayName(dataSource) {
    return dataSource.name || 'Unnamed data source';
}

export function getNameOrId(dataSource) {
    return dataSource.name || dataSource.id;
}

export function getRandomHexColor() {
    return '#' + (Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0');
}
