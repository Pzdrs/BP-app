export function getSelectedOptions(select) {
    return Array.from(select.selectedOptions)
        .map(option => option.value);
}
