<script setup>


import axios, {post} from "@/axios";

function login() {
  post('/auth/login', {
    username: 'admin',
    password: 'admin',
  })
}

function logout() {
  post('/auth/logout')
}

function hello() {
  axios.get('/')
      .then(res => {
        console.log(res)
      }).catch(err => {
    console.log(err)
  })
}

function secure() {
  axios.get('/secure')
      .then(res => {
        console.log(res)
      }).catch(err => {
    console.log(err)
  })
}

function state() {
  axios.get('/auth/state')
      .then(res => {
        console.log(res)
      }).catch(err => {
    console.log(err)
  })
}

</script>

<template>
  <button @click="login">login</button>
  <button @click="logout">logout</button>
  <button @click="hello">hello</button>
  <button @click="secure">secure</button>
  <button @click="state">state</button>
</template>
