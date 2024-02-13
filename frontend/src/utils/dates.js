
export function toDate(date) {
    return new Date(date);
}

export function localizeDateTime(date) {
    return toDate(date).toLocaleString("cs-CZ");
}
