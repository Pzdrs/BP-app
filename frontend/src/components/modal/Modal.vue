<script setup>
defineProps({
  id: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div :id="id" class="modal">
    <div class="modal-background"></div>
    <div class="modal-card">
      <header class="modal-card-head">
        <p class="modal-card-title">
          <slot name="title"></slot>
        </p>
        <button class="delete" aria-label="close"></button>
      </header>
      <section class="modal-card-body">
        <div class="content">
          <slot name="content"></slot>
        </div>
      </section>
      <footer class="modal-card-foot">
        <slot name="footer">
          <button class="button is-info">Submit</button>
        </slot>
        <button id="cancel-button" class="button">Cancel</button>
      </footer>
    </div>
  </div>
</template>

<style scoped>

</style>
