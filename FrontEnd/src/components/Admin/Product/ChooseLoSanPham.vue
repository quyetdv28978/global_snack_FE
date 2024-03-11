<script setup>
import { FilterMatchMode } from 'primevue/api';
import { useForm, useField, defineRule } from 'vee-validate';
import * as yup from 'yup';
import { reactive, ref, computed, onMounted, onBeforeMount } from 'vue';
import { ProductStore } from '@/service/Admin/product/product.api';
import { useToast } from 'primevue/usetoast';
import DataTableLoSanPham from './DataTableLoSanPham.vue';
import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';
import Index from '../LoSanpham/Index.vue';

const loSanPhamStore = useLoSanPhamService();
const toast = useToast();
const productStore = ProductStore();
const props = defineProps({
    myProp: {}
});

const { value: tenLo, errorMessage: loSanPhamError } = useField('tenLo');
const { value: name, errorMessage: nameError } = useField('ten');

const isOpen = ref(true);

const productDialog = ref(false);
const product = ref({});
// mở form


//đóng form
const hideDialog = () => {
    productDialog.value = false;
};



const selectedLoSanPham = ref(null);
const dataLoSanPham = ref([]);
const loadDataLoSanPham = async () => {
    await loSanPhamStore.fetchDataBySPCTNotNull(props.myProp.id);
    dataLoSanPham.value = loSanPhamStore.dataByStatus1;
};

const onLoSanPhamChange = () => {
    if (selectedLoSanPham.value) {
        tenLo.value = selectedLoSanPham.value.id;
        //    console.log(TrongLuong.value)
    } else {
        tenLo.value = null;
    }
};


onBeforeMount(() => {
    loadDataLoSanPham();
});

const arrayImage = ref([]);
const lstChiTietSP = ref([]);
const editProduct = () => {
    productDialog.value = true;
};

const formatCurrency = (value) => {
    return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
};

const filters = ref({});

onBeforeMount(() => {
    initFilters();
});


const initFilters = () => {
    filters.value = {
        global: { value: null, matchMode: FilterMatchMode.CONTAINS }
    };
};


const getStatusLabel = (trangThai) => {
    switch (trangThai) {
        case 1:
            return '- Đang sử dụng';

        case 0:
            return '- Chưa sử dụng';

        case 2:
            return '- Tồn kho';

        case 3:
            return '- Hết hạn  ';

        default:
            return '';
    }
};


const columns = ref([
    { field: 'giaSauGiam', header: 'Giá giảm giá' },
    { field: 'tenKM', header: 'Tên Khuyến Mãi' },
    { field: 'thoiGianBatDau', header: 'Thời gian bắt đầu' },
    { field: 'thoiGianKetThuc', header: 'Thời gian kết thúc' },
    { field: 'giaTriGiam', header: 'Giá Trị (%)' },
]);

// hàm để tắt/mở cột
const selectedColumns = ref(columns.value.soLuongTon);

const onToggle = (val) => {
    selectedColumns.value = columns.value.filter(col => val.includes(col));
};

const trangThaiselect = ref(null);
const dataTrangThai = ref([
    { label: 'Tất cả', value: -1 },
    { label: 'Hết hàng', value: 0 },
    { label: 'Còn hàng', value: 1 },
    { label: 'khuyễn mại', value: 2 },
    { label: 'Tồn kho', value: 3 },
]);
const { handleSubmit, resetForm } = useForm({
    // validationSchema: schema
});
const onSubmit = handleSubmit(async (values) => {
console.log(tenLo.value);
console.log(props.myProp.id);
try {
            await loSanPhamStore.updateLoSanPham(tenLo.value, props.myProp.id);
            toast.add({ severity: 'success', summary: 'Success Message', detail: 'update thành công', life: 3000 });
            productDialog.value = false;
            // dataLoSanPham.value = loSanPhamStore.dataByStatus1;
                        // reset();
    } catch (error) {
        console.error('Lỗi xử lý dữ liệu:', error);
    }
})

</script>


<template>
    <Button icon="pi pi-eye" severity="secondary" class="p-button-rounded  mr-2" @click="editProduct()" />
    <Dialog v-model:visible="productDialog" :style="{ width: '1050px' }" header="Xem Chi Tiết Sản Phẩm" :modal="true"
        class="p-fluid">
        <form @submit="onSubmit" style="margin-top: 30px;">
            <div class="p-fluid formgrid grid">

                <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                    <div class="Field col-12 md:col-12" style="margin-bottom: 30px">
                        <span class="p-float-label">
                            <InputText id="name" name="name" type="text" v-model="name" :class="{ 'p-invalid': nameError }"
                                disabled>
                            </InputText>
                            <label for="username">Tên sản phẩm</label>
                        </span>
                        <small class="p-error">{{ nameError }}</small>
                    </div>
<div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                <div style="display: flex">
                                    <span class="p-float-label" style="width: 239px">
                                        <Dropdown id="dropdown" :options="dataLoSanPham" v-model="selectedLoSanPham"
                                        :optionLabel="(option) => `${option.tenLo} - ${option.ngayHetHan}  ${getStatusLabel(option.trangThai)}`"
                                        @change="onLoSanPhamChange" 
                                        >
                                        </Dropdown>
                                        <label for="dropdown">Lô sản phẩm</label>
                                    </span>
                                    <DataTableLoSanPham :tableId="'TableLoSanPham'" :rightGhId="'right_ghLoSanPham'"
                                        :tableClass="'TableLoSanPham'" :rightGhClass="'right_ghLoSanPham'" />
                                </div>

                            </div>
                </div>
                <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                    <div class="p-fluid formgrid grid">
                        <div class="Field col-12 md:col-6"
                            style="margin-bottom: 30px; height: 300px;margin-top: -30px; margin-left: 100px;display: inline-flex; justify-content: center; align-items: center;">
                            <Galleria :value="arrayImage" :responsiveOptions="responsiveOptions" :numVisible="5"
                                containerStyle="max-width: 340px" style="">
                                <template #item="slotProps">
                                    <img :src="imagesChinh" :alt="slotProps.item.alt" style="width: 70%; height: ;" />
                                </template>
                                <template #thumbnail="slotProps">
                                    <img :src="slotProps.item.anh" :alt="slotProps.item.alt"
                                        style="width: 80px; height:80px ;" />
                                </template>
                            </Galleria>
                        </div>
                    </div>
                </div>

                <!-- <div class="Field col-12 md:col-12" style="margin-bottom: 30px">
                  <Index/>
                </div> -->
                <div style="width: 1000px; text-align: center;">
                    <Button class="p-button-outlined" outlined severity="secondary"
                        style="width: 200px; height: auto; margin: 10px;" @click="hideDialog()" label="Đóng">
                    </Button>
                    <Button type="submit" class="p-button-outlined"
                                    style="width: 200px; height: auto; margin: 10px" label="Lưu"></Button>
                </div>

            </div>
        </form>
    </Dialog>
</template>