export function openModal($el) {
    document.querySelector($el).classList.add('is-active');
}
export function closeModalByQuery($el) {
    document.querySelector($el).classList.remove('is-active');
}
export function closeModal($el) {
    $el.classList.remove('is-active');
}

export function closeAllModals() {
    (document.querySelectorAll('.modal') || []).forEach(($modal) => {
        closeModal($modal);
    });
}

export function setupModals() {
    // Add a click event on various child elements to close the parent modal
    (document.querySelectorAll('.modal-background, .modal-close, .modal-card-head .delete, .modal-card-foot #cancel-button') || []).forEach(($close) => {
        const $target = $close.closest('.modal');

        $close.addEventListener('click', () => {
            closeModal($target);
        });
    });

    // Add a keyboard event to close all modals
    document.addEventListener('keydown', (event) => {
        if (event.code === 'Escape') {
            closeAllModals();
        }
    });
}
