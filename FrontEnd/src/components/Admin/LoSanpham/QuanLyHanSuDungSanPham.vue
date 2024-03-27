<script setup>
import { FilterMatchMode } from 'primevue/api';
import { ref, onMounted, watch, computed, onBeforeMount } from 'vue';
import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';
import { useRoute, onBeforeRouteUpdate } from 'vue-router';

const loSanPhamService = useLoSanPhamService();

const filters = ref({});
const loSsanpham = ref([]);
const route = useRoute();

const changeParams = () => {
  loadDataLoSanPham();
};
onBeforeMount(() => {
    initFilters();
});
const initFilters = () => {
    filters.value = {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS }
    };
};
const loadDataLoSanPham = async () => {
    await loSanPhamService.showLoSanPhamByMount(route.params.time);
    // await loSanPhamService.fetchData2();
  loSsanpham.value = loSanPhamService.dataLoSanPhamByMount;
console.log("a");
};
watch(
  () => route.params,
  () => {
    loadDataLoSanPham();
    console.log(loSanPhamService.dataLoSanPhamByMount);
  }
);

onMounted(() => {
  loadDataLoSanPham();
});

// onBeforeRouteUpdate(async (to, from, next) => {
//     console.log(route.params.time);
//     loadDataLoSanPham();
//   next();
// });
</script>
<template>
    <!-- <div class="card"> -->
    <DataTable :value="loSsanpham" paginator :rows="5" :rowsPerPageOptions="[5, 10, 20, 50]"
        tableStyle="min-width: 50rem">
        <Column field="tenSanPham" header="Tên sản phẩm" style="width: 25%"></Column>
        <Column field="trongLuong" header="Trọng lượng" style="width: 25%"></Column>
        <Column field="maLo" header="Mã lô" style="width: 25%"></Column>
        <Column field="tenLo" header="Tên lô" style="width: 25%"></Column>
        <Column field="soLuong" header="Số lượng" style="width: 25%"></Column>
        <Column field="ngayHetHan" header="Ngày hết hạn" style="width: 25%"></Column>
    </DataTable>
    <!-- </div> -->
</template>
