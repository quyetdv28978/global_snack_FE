<script setup></script>

<template>
    <transition name="transitionName" :appear="true" enterActiveClass="fadeinleft">
        <router-view />
    </transition>
</template>

<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

html {
}
</style>
