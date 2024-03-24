<script setup>
import { useLayout } from '@/layout/composables/layout';
import { computed } from 'vue';

const { layoutConfig } = useLayout();

const logoUrl = computed(() => {
    return `layout/images/${layoutConfig.darkTheme.value ? 'logo-white' : 'logo-dark'}.svg`;
});
</script>

<template>
    <!-- Remove the container if you want to extend the Footer to full width. -->
    <div style="margin: 120px 0; padding: 0 400px">
        <div class="flex flex-column justify-center">
            <h1 class="text-center">Contact Us</h1>
            <form class="contact-container">
                <div class="flex flex-column gap-2 mt-1">
                    <label for="username" class="text-gray-600 text-xl">FirstName</label>
                    <InputText id="username" v-model="value" aria-describedby="username-help" />
                </div>
                <div class="flex flex-column gap-2">
                    <label for="username" class="text-gray-600 text-xl">LastName</label>
                    <InputText id="username" v-model="value" aria-describedby="username-help" />
                </div>
                <div class="flex flex-column gap-2">
                    <label for="username" class="text-gray-600 text-xl">Email</label>
                    <InputText id="username" v-model="value" aria-describedby="username-help" />
                </div>
                <div class="flex flex-column gap-2">
                    <label for="username" class="text-gray-600 text-xl">Subject</label>
                    <InputText id="username" v-model="value" aria-describedby="username-help" />
                </div>
                <div class="flex flex-column gap-2">
                    <label for="username" class="text-gray-600 text-xl">Message</label>
                    <Textarea v-model="value" rows="5" cols="30" />
                </div>
                <Button class="mt-6">Submit</Button>
            </form>
        </div>
    </div>
</template>
<style lang="scss" scoped>
input {
    outline: none;
}
</style>
