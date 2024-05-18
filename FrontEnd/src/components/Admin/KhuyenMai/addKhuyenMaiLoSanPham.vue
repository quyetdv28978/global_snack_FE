<script setup>
import { ref, onMounted, watch } from 'vue';
import { useLoaiService } from '@/service/Admin/Loai/LoaiService';
import { useCounterStore } from '@/service/Admin/ThuongHieu/ThuongHieuService.js';
import { ProductStore } from '@/service/Admin/product/product.api';
import SelectCTSP from './SelectCTSP.vue';
import { ctspStore } from '@/service/Admin/SanPhamChiTiet/sanPhamCTService';
import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';
import { useToast } from 'primevue/usetoast';
import { log } from 'pdfmake/build/pdfmake';

const toast = useToast();
const ctspService = ctspStore();
const loSanPhamService = useLoSanPhamService();
const productStore = ProductStore();
const thuonghieuService = useCounterStore();
const loaiStore = useLoaiService();
const products = ref([]);
const showSpinner = ref(false);
// const productService = productStore();
const selectedProductDialog = ref(false);
const dataLoai = ref([]);
const dataThuongHieu = ref([]);
const selectedDialog = ref(false);
const selectedProduct = ref([]);
const listSPCT = ref(null);
const showProduct = (id) => {
    console.log(selectedProduct);
    // localStorage.setItem('idkm', props.myProp.id);
    //    loadDataProduct(id);
    //     selectedDialog.value = true;
};

const hide = () => {
    selectedDialog.value = false;
};
const props = defineProps({
    myProp: {}
});


onMounted(() => {
    loadProducts();
    loadDataLoai();
    loadDataThuongHieu();
});

const idSP = ref(null);

const loadDataProduct = async (idsp) => {
    idSP.value = idsp;
    await loSanPhamService.showLoSanPhamSapHethan(idsp)
    listSPCT.value = loSanPhamService.data;
};

const applyKhuyenMai = () => {
    // console.log("applyKhuyenMai");
    // đây là số lượng SPCT được chọn để áp dụng khuyến mại
    const sl = selectedProduct.value.length;
        localStorage.setItem('idkm', props.myProp.id);
    // đây là IdKM dùng để áp dụng được truyền từ component cha
    const idkm = localStorage.getItem('idkm');
        console.log(props.myProp.id);
    // duyệt qua mảng danh sách các CTSP được chọn
    selectedProduct.value.forEach((product) => {
        const tenLsp = product.tenLo;
        // cập nhật lại giá tiền và id khuyến mại
        ctspService.addKhuyenMaiLoSanphamSapHethan(tenLsp, idkm);
    });

    selectedProduct.value = [];
    selectedDialog.value = false;
    loadDataProduct(idSP.value);
    toast.add({ severity: 'success', summary: 'Successful', detail: 'Áp dụng khuyến mại thành công', life: 3000 });
};

const showProducts = () => {
    selectedProductDialog.value = true;
};


const hideDialog = () => {
    selectedProductDialog.value = false;
};



const loadProducts = async () => {
    showSpinner.value = true;
    await loSanPhamService.showLoSanPhamSapHethan(); // Gọi hàm fetchAll từ Store
    products.value = loSanPhamService.data;

};

const selectedLoai = ref(null);
const loadDataLoai = async () => {
    await loaiStore.fetchDataByStatus(1);
    dataLoai.value = loaiStore.dataByStatus1;

};
watch(selectedLoai, (newVal, oldVal) => {
    // Kiểm tra nếu giá trị mới khác giá trị cũ
    if (newVal !== oldVal) {
        // Gọi hàm để cập nhật dataTable dựa trên giá trị mới của selectedLoai
        updateDataTableLoai();
    }
});


const selectedThuongHieu = ref(null);
const loadDataThuongHieu = async () => {
    await thuonghieuService.fetchData();
    dataThuongHieu.value = thuonghieuService.data;

};

watch(selectedThuongHieu, (newVal, oldVal) => {
    // Kiểm tra nếu giá trị mới khác giá trị cũ
    if (newVal !== oldVal) {
        // Gọi hàm để cập nhật dataTable dựa trên giá trị mới của selectedLoai
        updateDataTableThuongHieu();
    }
});


const updateDataTableLoai = async () => {
    await productStore.fetchDataByLoai(selectedLoai.value.id);
    products.value = productStore.products;
};


const updateDataTableThuongHieu = async () => {
    await productStore.fetchDataByThuongHieu(selectedThuongHieu.value.id);
    products.value = productStore.products;
};


const getStatusLabel = (soLuong, trangThai) => {
    if (soLuong > 0 && trangThai == 1) {
        return { text: 'Còn Hàng', severity: 'success' };
    } else if (soLuong <= 0 || trangThai == 0) {
        return { text: 'Hết Hàng', severity: 'danger' };
    } else {
        return { text: 'Tồn Kho', severity: 'info' };
    }
};
</script>

<template>
    <!-- không thể chọn CTSP áp dụng khuyến mại nếu trạng thái khuyến mại là hết hạn ( = 1) -->
    <Button icon="pi pi-inbox" class="p-button-rounded p-button-info ml-2" @click="showProducts()"
        :disabled="props.myProp.trangThai === 1" />
    <Dialog v-model:visible="selectedProductDialog" header="Flex Scroll" :style="{ width: '50vw' }" maximizable modal
        :contentStyle="{ height: '850px' }" class="p-fluid">
        <template #header>
            <div class="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
                <h5 class="m-0 ">Lô sản phẩm sắp hết hạn</h5>
            </div>

            <!-- <Dropdown id="dropdown" :options="dataLoai" v-model="selectedLoai" optionLabel="ten"  placeholder="Loại" :class="{ 'p-invalid': loaiError }" style="height: 40px"> </Dropdown> -->
            <!-- <label for="dropdown" style="margin-left: 10px;">Loại</label> -->
            <!-- <Dropdown v-model="thuongHieu" :options="dataThuongHieu" optionLabel="label" placeholder="Tất cả" class="w-full md:w-14rem" style="margin-left: 20px" /> -->
            <!-- <Dropdown id="dropdown" :options="dataThuongHieu" v-model="selectedThuongHieu" optionLabel="ten"  placeholder="Thương hiệu" :class="{ 'p-invalid': loaiError }" style="height: 40px"> </Dropdown> -->

        </template>
        <div class="card p-fluid">
            <DataTable :value="products" v-model:selection="selectedProduct" paginator :rows="5"
                :rowsPerPageOptions="[5, 10, 20, 50]" tableStyle="min-width: 50rem"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
                responsiveLayout="scroll">
                <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>

                <Column field="code" header="STT" :sortable="true" style="width: 1px; padding: 5px">
                    <template #body="slotProps">
                        <span class="p-column-title">STT</span>
                        {{ products.indexOf(slotProps.data) + 1 }}
                    </template>
                </Column>
                <Column field="ma" header="Mã lô" :sortable="true" headerStyle="width:8%; min-width:3rem;">
                    <template #body="slotProps">
                        <span class="p-column-title">mã lô</span>
                        {{ slotProps.data.maLo }}
                    </template>
                </Column>
                <Column field="ten" header="Tên lô" :sortable="true" headerStyle="width:14%; min-width:10rem;">
                    <template #body="slotProps">
                        <span class="p-column-title">Tên</span>
                        {{ slotProps.data.tenLo }}
                    </template>
                </Column>
                <Column field="loai" header="ngày hết hạn" :sortable="true" headerStyle="width:8%; min-width:5rem;">
                    <template #body="slotProps">
                        <span class="p-column-title">Tên</span>
                        {{ slotProps.data.ngayHetHan }}
                    </template>
                </Column>
                <Column field="thuongHieu" header="trang thái" :sortable="true"
                    headerStyle="width:14%; min-width:5rem;">
                    <template #body="slotProps">
                        <span class="p-column-title">Tên</span>
                        Sắp hết hạn sử dụng
                    </template>
                </Column>
                <Column field="vatLieu" header="Tên sản phẩm" :sortable="true" headerStyle="width:14%; min-width:5rem;">
                    <template #body="slotProps">
                        <span class="p-column-title">Tên</span>
                        {{ slotProps.data.tenSanPham }}
                    </template>
                </Column>

                <Column field="vatLieu" header="Trọng lượng" :sortable="true" headerStyle="width:14%; min-width:5rem;">
                    <template #body="slotProps">
                        <span class="p-column-title">Tên</span>
                        {{ slotProps.data.trongLuong }}
                    </template>
                </Column>
            </DataTable>
        </div>

        <template #footer>
            <Button label="Huỷ" icon="pi pi-times" class="p-button-text" @click="hideDialog" />
            <Button label="Save" icon="pi pi-check" @click="applyKhuyenMai" />
        </template>
    </Dialog>

    <Dialog v-model:visible="selectedDialog" header="Flex Scroll" :style="{ width: '50vw' }" maximizable modal
        :contentStyle="{ height: '500px' }" class="p-fluid">
        <template #header>
            <div class="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
                <h5 class="m-0">Khuyến Mại</h5>
            </div>
        </template>
        <div class="card p-fluid">
            <DataTable :value="listSPCT" v-model:selection="selectedProduct" paginator :rows="5"
                :rowsPerPageOptions="[5, 10, 20, 50]" tableStyle="min-width: 50rem"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                currentPageReportTemplate="Showing {first} to {last} of {totalRecords} listSPCT"
                responsiveLayout="scroll">
                <template #empty>
                    <div class="flex flex-column justify-content-center align-items-center" style="height: 300px">
                        <svg width="100px" height="100px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg"
                            fill="#000000" class="bi bi-file-earmark-x">
                            <path
                                d="M6.854 7.146a.5.5 0 1 0-.708.708L7.293 9l-1.147 1.146a.5.5 0 0 0 .708.708L8 9.707l1.146 1.147a.5.5 0 0 0 .708-.708L8.707 9l1.147-1.146a.5.5 0 0 0-.708-.708L8 8.293 6.854 7.146z" />
                            <path
                                d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2zM9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5v2z" />
                        </svg>
                        <h6>Không có dữ liệu.</h6>
                    </div>
                </template>
                <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
                <Column field="tenLo" header="Sản Phẩm"></Column>
                <Column field="trongLuong" header="Trọng lượng 2"></Column>
                <Column field="giaBan" header="Giá Bán"></Column>
            </DataTable>
        </div>

        <template #footer>
            <Button label="Huỷ" icon="pi pi-times" class="p-button-text" @click="hide" />
            <Button label="Áp dụng" icon="pi pi-check" @click="applyKhuyenMai" />
        </template>
    </Dialog>
</template>
