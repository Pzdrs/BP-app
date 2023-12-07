<script setup>

</script>

<template>
  <v-navigation-drawer
      permanent
      expand-on-hover
      rail
  >
    <v-list>
      <v-list-item
          prepend-avatar="https://randomuser.me/api/portraits/women/85.jpg"
          title="Sandra Adams"
          subtitle="sandra_a88@gmailcom"
      ></v-list-item>
    </v-list>

    <v-divider></v-divider>

    <v-list density="compact" nav>
      <v-list-item to="/settings" prepend-icon="mdi-account-settings" title="Settings" value="myfiles"></v-list-item>
    </v-list>
    <template v-slot:append>
      <v-list density="compact" nav>
        <v-list-item prepend-icon="mdi-logout-variant" title="Log out" value="myfiles"></v-list-item>
      </v-list>
    </template>
  </v-navigation-drawer>
</template>

<style scoped>

</style>
