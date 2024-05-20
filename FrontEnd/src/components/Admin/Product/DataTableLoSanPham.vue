<script setup>

import { ref, defineProps, onMounted } from 'vue';

import Index from '@/components/Admin/LoSanpham/Index.vue';

import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';
import { useToast } from 'primevue/usetoast';

const emit = defineEmits(['update-sanPham'])
const toast = useToast();
const loSanPhamService = useLoSanPhamService();
const dataMauSac = ref([]);
const props = defineProps({
    tableId: String,
    rightGhId: String,
    tableClass: String,
    rightGhClass: String
});

const display = ref(false);
const isRightGhLoaiVisible = ref(true);

const tableClass = ref(props.tableClass);
const rightGhClass = ref(props.rightGhClass);


const formatCurrency = (value) => {
    return value.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
};

const loadDataMauSac = async () => {
    await loSanPhamService.fetchData();
    dataMauSac.value = loSanPhamService.data;
};

onMounted(() => {
    loadDataMauSac();
});


const chuyenPhanTus = (id) => {
    isRightGhLoaiVisible.value = false;
    const element = document.getElementById(id);
    window.scroll({
        top: element.offsetTop,
        behavior: 'smooth'
    });
};

const vePhanTus = (id) => {
    isRightGhLoaiVisible.value = true;
    const element = document.getElementById(id);
    if (element) {
        window.scroll({
            top: element.offsetTop,
            behavior: 'smooth'
        });
    }
};

const open = () => {
    display.value = true;
};

const close = () => {
    display.value = false;
};

const selectedProduct = ref(null);
const products = ref(null);
const updateLoSanPham = () => {
    console.log("da chay emit o Datatable lo");
    emit("update-updateLoSanpHam-sanPham")
}
</script>

<template>
    <div>
        <Dialog header="Lô sản phẩm" v-model:visible="display" :breakpoints="{ '960px': '75vw' }" :style="{ width: '1000px' }"
            :modal="true">
            <index @update-index-sanPham = "updateLoSanPham"></index>

            <template #footer>

            </template>
        </Dialog>
        <Button type="button" icon="pi pi-pencil" @click="open" class="p-button-success"
            style="width: 50px; height: 40px; margin-left: 20px" />
    </div>
</template>
