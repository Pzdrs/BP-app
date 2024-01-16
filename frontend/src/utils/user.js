function nullOrEmpty(value) {
    return value === null || value === '';
}

export function getFullName(user) {
    if (nullOrEmpty(user.firstName) && nullOrEmpty(user.lastName)) return 'Unnamed user';
    return `${user.firstName} ${user.lastName}`;
}
