<script setup>
import { FilterMatchMode } from 'primevue/api';
import { ref, onMounted, onBeforeMount, watch } from 'vue';
// import Them from './ThemMauSac.vue';
// import Detail from './DetailMauSac.vue';
// import Update from './UpdateMauSac.vue';
import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';
import { useToast } from 'primevue/usetoast';
import ThemLoSanPham from './ThemLoSanPham.vue';

const loSanPhamService = useLoSanPhamService();
const toast = useToast();
const deleteProductDialog = ref(false);
const product = ref({});
const selectedProducts = ref(null);
const dt = ref(null);
const filters = ref({});
const dataTrangThai = ref([
    { label: 'Tất cả', value: 'Tất cả' },
    { label: 'Đang sử dụng', value: '1' },
    { label: 'Ngưng sử dụng', value: '0' }
]);
const trangThai = ref();
const idDelete = ref();
const dataTrongLuong = ref([]);

//load data màu sắc tất cả
// const loaddataTrongLuong = () => {
//     await loSanPhamService.fetchData();
//     dataTrongLuong.value = loSanPhamService.data;
// };

//load data màu sắc theo trạng thái
const loaddataTrongLuong = async () => {
    await loSanPhamService.fetchData();
    dataTrongLuong.value = loSanPhamService.data;
};

const loaddataTrongLuongByTrangThai = async () => {
    await loSanPhamService.fetchDataByStatus(trangThai.value.value);
    dataTrongLuong.value = loSanPhamService.data;
};
//thay đổi cbb
watch(trangThai, (newVal) => {
    // if (trangThai.value.value != 'Tất cả') {
    //     loaddataTrongLuongByTrangThai();
    // } else {
        loaddataTrongLuong();
    // }
});

onBeforeMount(() => {
    initFilters();
});

onMounted(() => {
    loaddataTrongLuong();
});

//hiện confirm
// const confirmDeleteProduct = (id) => {
//     idDelete.value = id;
//     deleteProductDialog.value = true;
// };

//xoá
// const deleteTrongLuong = (id) => {
//     // axios.put('http://localhost:8080/api/mau-sac/delete/' + idDelete.value).then((response) => {
//     //     alert(response.data.data);
//     //     deleteProductDialog.value = false;
//     //     window.location.reload();
//     // });
//     const remove = loSanPhamService.deleteMauSac(idDelete.value);
//     toast.add({ severity: 'success', summary: 'Thông báo', detail: 'Xoá thành công', life: 3000 });
//     deleteProductDialog.value = false;
// };

const exportCSV = () => {
    dt.value.exportCSV();
};

const initFilters = () => {
    filters.value = {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS }
    };
};
</script>
<template>
    <div class="grid">
        <div class="col-12">
            <div class="card">
                <Toast />
                <Toolbar class="mb-4">
                    <template v-slot:start>
                        <div class="my-2">
                            <ThemLoSanPham></ThemLoSanPham>
                        </div>
                    </template>

                </Toolbar>
                <DataTable
                    ref="dt"
                    :value="dataTrongLuong"
                    v-model:selection="selectedProducts"
                    dataKey="id"
                    :paginator="true"
                    :rows="10"
                    :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                    :rowsPerPageOptions="[5, 10, 25]"
                    currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
                    responsiveLayout="scroll"
                >
                    <template #header>
                        <div class="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
                            <h5 class="m-0">Quản lý lô sản phẩm</h5>
                            <span class="block mt-2 md:mt-0 p-input-icon-left">
                                <i class="pi pi-search" />
                                <InputText v-model="filters['global'].value" placeholder="Search..." />
                                <Dropdown v-model="trangThai" :options="dataTrangThai" optionLabel="label" placeholder="Tất cả" class="w-full md:w-14rem" style="margin-left: 20px" />
                            </span>
                        </div>
                    </template>
                    <Column field="maLo" header="Mã" :sortable="true" headerStyle="width:14%; min-width:10rem;">
                        <template #body="slotProps">
                            <span class="p-column-title">ma</span>
                            {{ slotProps.data.maLo }}
                        </template>
                    </Column>
                    <Column field="tenLo" header="Tên lô" :sortable="true" headerStyle="width:14%; min-width:10rem;">
                        <template #body="slotProps">
                            <span class="p-column-title">ten lo</span>
                            {{ slotProps.data.tenLo }}
                        </template>
                    </Column>
                    <Column field="ngayHetHan" header="Ngày Hết hạn" :sortable="true" headerStyle="width:14%; min-width:8rem;">
                        <template #body="slotProps">
                            <span class="p-column-title">Ngày Hết hạn</span>
                            {{ slotProps.data.ngayHetHan }}
                        </template>
                    </Column>
                    <Column field="trangThai" header="Trạng thái" :sortable="true" headerStyle="width:14%; min-width:10rem;">
                        <template #body="slotProps">
                            <span class="p-column-title">Category</span>
                            {{ slotProps.data.trangThai == 1 ? 'Đang sử dụng' : 'Ngưng sử dụng' }}
                        </template>
                    </Column>

                    <Column header="Hành động" headerStyle="min-width:10rem;">
                        <template #body="slotProps">
                            <Detail :my-prop="slotProps.data"></Detail>
                            <Update :my-prop="slotProps.data"></Update>
                            <!-- <Button icon="pi pi-pencil" class="p-button-rounded p-button-success mr-2" @click="editProduct(slotProps.data)" /> -->
                            <Button icon="pi pi-trash" class="p-button-rounded p-button-warning mt-2" @click="confirmDeleteProduct(slotProps.data.id)" />
                        </template>
                    </Column>
                </DataTable>
                <Dialog v-model:visible="deleteProductDialog" :style="{ width: '450px' }" header="Confirm" :modal="true">
                    <div class="flex align-items-center justify-content-center">
                        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                        <span v-if="product"
                            >Bạn có chắc chắn muốn xoá lô sản phẩm <b>{{ product.tenLo }}</b> không ?</span
                        >
                    </div>
                    <template #footer>
                        <Button label="No" icon="pi pi-times" class="p-button-text" @click="deleteProductDialog = false" />
                        <Button label="Yes" icon="pi pi-check" class="p-button-text" @click="deleteProduct(product.id)" />
                    </template>
                </Dialog>
            </div>
        </div>
    </div>
</template>
