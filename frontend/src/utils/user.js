export function getFullName(user) {
    if (user.firstName === '' && user.lastName === '') return 'Unnamed user';
    return `${user.firstName} ${user.lastName}`;
}
