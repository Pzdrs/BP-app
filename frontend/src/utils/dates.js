import dateFormat from "dateformat";

export function toDate(date) {
    return new Date(Date.parse(date));
}

export function formatDate(date) {
    return dateFormat(date, 'yyyy-mm-dd HH:MM:ss')
}
