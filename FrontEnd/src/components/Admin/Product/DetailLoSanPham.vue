<script setup>
import { FilterMatchMode } from 'primevue/api';
import { useForm, useField, defineRule } from 'vee-validate';
import * as yup from 'yup';
import { reactive, ref, computed, onMounted, onBeforeMount } from 'vue';
import TableLoai from './DataTableLoai.vue';
import TableThuongHieu from './DataTableThuongHieu.vue';
import TableMauSac from './DataTableMauSac.vue';
import TablevatLieu from './DataTableVatLieu.vue';
import ChooseLoSanPham from './ChooseLoSanPham.vue';
import { ProductStore } from '@/service/Admin/product/product.api';
import { useToast } from 'primevue/usetoast';
import { useCounterStore } from '@/service/Admin/ThuongHieu/ThuongHieuService.js';
import { SizeStore } from '@/service/Admin/Size/SizeService';
import { useLoaiService } from '@/service/Admin/Loai/LoaiService';
import { useMauSacService } from '@/service/Admin/MauSac/MauSacService';
import { TrongLuongStore } from '@/service/Admin/TrongLuong/TrongLuong.api';
import { VatLieuStore } from '@/service/Admin/VatLieu/VatLieu.api';

const toast = useToast();
const productStore = ProductStore();
const thuongHieuService = useCounterStore();
const sizeStore = SizeStore();
const mauSacStore = useMauSacService();
const loaiStore = useLoaiService();
const trongLuongStore = TrongLuongStore();
const vatLieuStore = VatLieuStore();
const props = defineProps({
    myProp: {}
});


const { value: id, errorMessage: idError } = useField('id');
const { value: name, errorMessage: nameError } = useField('ten');
const { value: soluong, errorMessage: soLuongError } = useField('soLuongTon');
const { value: GiaBan, errorMessage: giaBanError } = useField('giaBan');
const { value: GiaNhap, errorMessage: giaNhapError } = useField('giaNhap');
const { value: Loai, errorMessage: loaiError } = useField('loai');
const { value: ThuongHieu, errorMessage: thuongHieuError } = useField('thuongHieu');
const { value: vatLieu, errorMessage: vatLieuError } = useField('vatLieu');
const { value: TrongLuong, errorMessage: trongLuongError } = useField('trongLuong');
const { value: TrangThai, errorMessage: TrangThaiSacError } = useField('trangThai');
const { value: MoTa, errorMessage: MoTaSacError } = useField('moTa');
const { value: imagesProduct, errorMessage: imagesProductError } = useField('imagesProduct');
const { value: imagesChinh, errorMessage: imagestError } = useField('anh');

const visibledatatable = ref(false);

const isOpen = ref(true);

const productDialog = ref(false);
const product = ref({});
// mở form


//đóng form
const hideDialog = () => {
    productDialog.value = false;
};

const selectedCity = ref(null);
const selectedLoai = ref(null);
const selectedvatLieu = ref(null);
const selectedTrongLuong = ref(null);
const ImagesProduct = ref([]);

const dataThuongHieu = ref([]);
//load data thương hiệu tất cả
const loadDataThuongHieu = async () => {
    await thuongHieuService.fetchData();
    dataThuongHieu.value = thuongHieuService.data;
    const selectedThuongHieu = dataThuongHieu.value.find((item) => item.ten === props.myProp.thuongHieu);
    selectedCity.value = selectedThuongHieu;
    if (selectedCity.value) {
        ThuongHieu.value = selectedCity.value.id;
    } else {
        ThuongHieu.value = null;
    }
};

const dataSize = ref([]);


const dataLoai = ref([]);
const loadDataLoai = async () => {
    await loaiStore.fetchData();
    dataLoai.value = loaiStore.data;

    const selectedLoais = dataLoai.value.find((item) => item.ten === props.myProp.loai);
    selectedLoai.value = selectedLoais;
    if (selectedLoai.value) {
        Loai.value = selectedLoai.value.id;
    } else {
        Loai.value = null;
    }

};

const dataTrongLuong = ref([]);
const loadDataTrongLuong = async () => {
    await trongLuongStore.fetchAll();
    dataTrongLuong.value = trongLuongStore.data;

    const selectedTrongLuongs = dataTrongLuong.value.find((item) => item.value === parseInt(props.myProp.trongLuong, 10));
    selectedTrongLuong.value = selectedTrongLuongs;
    if (selectedTrongLuong.value) {
        TrongLuong.value = selectedTrongLuong.value.id;
    } else {
        TrongLuong.value = null;
    }
};

const dataVatLieu = ref([]);
const loadDataVatLieu = async () => {
    await vatLieuStore.fetchAll();
    dataVatLieu.value = vatLieuStore.data;
    visibledatatable.value = false
    visibledatatable.value = true
    const selectedVatLieus = dataVatLieu.value.find((item) => item.ten === props.myProp.vatLieu);
    selectedvatLieu.value = selectedVatLieus;
    if (selectedvatLieu.value) {
        vatLieu.value = selectedvatLieu.value.id;
    } else {
        vatLieu.value = null;
    }
};

onBeforeMount(() => {
    visibledatatable.value = true
    loadDataThuongHieu();
    loadDataLoai();
    loadDataTrongLuong();
    loadDataVatLieu();
    console.log('list update: ', props);

});

const arrayImageMauSac = ref([]);
const arrayImage = ref([]);
const lstChiTietSP = ref([]);
const editProduct = () => {
    id.value = props.myProp.id;
    name.value = props.myProp.ten;
    soluong.value = props.myProp.soLuongTon;
    GiaBan.value = props.myProp.giaBan;
    GiaNhap.value = props.myProp.giaNhap;
    TrangThai.value = props.myProp.trangThai.toString();
    MoTa.value = props.myProp.moTa;
    imagesChinh.value = props.myProp.anh;

    arrayImage.value = props.myProp.img;
    for (const img of arrayImage.value) {
        ImagesProduct.value.push(img.anh);
        imagesProduct.value = ImagesProduct.value.join(',').replace(/^,/, '').split(',');
    }

    lstChiTietSP.value = props.myProp.sanPhamChiTiet;
    console.log(props.myProp)
    product.value = { ...editProduct };

    productDialog.value = true;
};

const getStatusLabel = (soLuong) => {
    if (soLuong == 0) {
        return { text: 'hết Hàng', severity: 'danger' };
    } else if (soLuong == 1) {
        return { text: 'Còn hàng', severity: 'success' };
    } else {
        return { text: 'Tồn kho', severity: 'war' };
    }

};

const getStatusLabelKhuyenMai = (khuyenMai) => {
    return { text: 'Khuyễn Mại', severity: 'warn' };
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


const loadDataTrangThai = () => {
    lstChiTietSP.value = props.myProp.sanPhamChiTiet;

    if (trangThaiselect.value.value == -1) {
        lstChiTietSP.value = props.myProp.sanPhamChiTiet;
        return lstChiTietSP.value;
    } else if (trangThaiselect.value.value == 0) {
        lstChiTietSP.value = lstChiTietSP.value.filter(item => item.trangThai == 0);
        return lstChiTietSP.value;
    } else if (trangThaiselect.value.value == 1) {
        lstChiTietSP.value = lstChiTietSP.value.filter(item => item.trangThai == 1);
        return lstChiTietSP.value;
    } else if (trangThaiselect.value.value == 3) {
        lstChiTietSP.value = lstChiTietSP.value.filter(item => item.trangThai == 3);
        return lstChiTietSP.value;
    } else {
        lstChiTietSP.value = lstChiTietSP.value.filter(item => item.tenKM != null);
        return lstChiTietSP.value;
    }
    
}
</script>
<template>
    <Button icon="pi pi-box" severity="secondary" class="p-button-rounded  mr-2" @click="editProduct()" />
    <Dialog v-model:visible="productDialog" :style="{ width: '1050px' }" header="Xem Chi Tiết Lô Sản Phẩm" :modal="true"
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

                    <div class="field col-12 md:col-12" style="margin-bottom: 30px">
                        <label for="address">Trạng thái</label>
                        <div class="flex flex-wrap gap-3">
                            <div class="flex align-items-center">
                                <RadioButton v-model="TrangThai" inputId="ingredient1" name="pizza" value="1"
                                    :class="{ 'p-invalid': TrangThaiSacError }" disabled />
                                <label for="ingredient1" class="ml-2">Sẵn sàng để bán</label>
                            </div>
                            <div class="flex align-items-center">
                                <RadioButton v-model="TrangThai" inputId="ingredient2" name="pizza" value="3"
                                    :class="{ 'p-invalid': TrangThaiSacError }" disabled />
                                <label for="ingredient2" class="ml-2">tồn kho</label>
                            </div>

                        </div>
                        <small class="p-error">{{ TrangThaiSacError }}</small>
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

                <div class="Field col-12 md:col-12" style="margin-bottom: 30px">
                    <DataTable ref="dt" :value="lstChiTietSP" v-model:selection="selectedProducts" dataKey="id"
                    v-if="visibledatatable"
                        :paginator="true" :rows="5" :filters="filters"
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        :rowsPerPageOptions="[5, 10, 25]"
                        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
                        responsiveLayout="scroll" showGridlines>
                        <template #header>

                            <div class="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
                                
                                <div style="display: flex;">
                                    <h5 class="m-0" style="margin-right: 0px;">Chi Tiết Lô Sản Phẩm </h5>
                                </div>

                                <span class="block mt-2 md:mt-0 p-input-icon-left" style="width: 200px; left: 50px;">
                                    <i class="pi pi-search" />
                                    <InputText v-model="filters['global'].value" placeholder="Search..." />

                                </span>
                                <Dropdown v-model="trangThaiselect" :options="dataTrangThai" optionLabel="label"
                                    :optionLabel="(option) => option.label" placeholder="Tất cả" class="w-full md:w-14rem"
                                    style="margin-left: 20px" @change="loadDataTrangThai()">

                                </Dropdown>

                            </div>

                        </template>

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
                        <Column field="stt" header="STT" :sortable="true" style="width: 1px; padding: 5px;">
                            <template #body="slotProps">
                                <span class="p-column-title">STT</span>
                                {{ lstChiTietSP.indexOf(slotProps.data) + 1 }}
                            </template>
                        </Column>
                        <Column field="anh" header="Ảnh" :sortable="true" headerStyle="width:14%; min-width:5rem;">
                            <template #body="slotProps">
                                <span class="p-column-title">Ảnh</span>
                                <img :src="slotProps.data.anh" :alt="i" class="shadow-2" width="50" />
                            </template>
                        </Column>
                        <Column field="maLo" header="Mã lô" :sortable="true" headerStyle="width:14%; min-width:5rem;">
                            <template #body="slotProps">
                                <span class="p-column-title">Mã lô</span>
                                {{ slotProps.data.maLo }}
                            </template>
                        </Column>
                        <Column field="tenLo" header="Tên lô" :sortable="true" headerStyle="width:14%; min-width:5rem;">
                            <template #body="slotProps">
                                <span class="p-column-title">Tên lô</span>
                                {{ slotProps.data.tenLo }}
                            </template>
                        </Column>

                        <Column field="ngayHetHan" header="ngày hết hạn" :sortable="true" headerStyle="width:14%; min-width:5rem;">
                            <template #body="slotProps">
                                <span class="p-column-title">ngày hết hạn</span>
                                {{ slotProps.data.ngayHetHan }}
                            </template>
                        </Column>
                      
                        <Column field="soLuongTon" header="Số Lượng" :sortable="true"
                            headerStyle="width:14%; min-width:8rem;">
                            <template #body="slotProps">
                                <span class="p-column-title">Số Lượng</span>
                                {{ slotProps.data.soLuongTon }}
                            </template>
                        </Column>
                        
                        <Column field="giaBan" header="Giá Bán" :sortable="true" headerStyle="width:14%; min-width:8rem;">
                            <template #body="slotProps">
                                <span class="p-column-title">Số Lượng</span>
                                {{ formatCurrency(slotProps.data.giaBan) }}
                            </template>
                        </Column>
                        <Column v-for="(col, index) of selectedColumns" :field="col.field" :header="col.header"
                            :key="col.field + '_' + index" :sortable="true" headerStyle="width:8%; min-width:5rem;">
                        </Column>
                        <Column field="trongLuong" header="Trọng Lượng" :sortable="true"
                            headerStyle="width:14%; min-width:8rem;">
                            <template #body="slotProps">
                                <span class="p-column-title">Số Lượng</span>
                                {{ slotProps.data.trongLuong }}
                            </template>
                        </Column>
                        <Column field="trangThai" header="Trạng Thái" sortable headerStyle="width: 4%; min-width: 5rem;">
                            <template #body="slotProps">
                                <Tag :value="getStatusLabel(slotProps.data.trangThai).text"
                                    v-if="slotProps.data.tenKM === null || slotProps.data.tenKM == ''"
                                    :severity="getStatusLabel(slotProps.data.trangThai).severity" />
                                <div v-else>
                                    <Tag :value="getStatusLabelKhuyenMai(slotProps.data.tenKM).text"
                                        :severity="getStatusLabelKhuyenMai(slotProps.data.tenKM).severity" />
                                </div>
                            </template>
                        </Column>
                        <Column header="Action" headerStyle="min-width:10rem;">
                            <template #body="slotProps">
                                <ChooseLoSanPham :my-prop="slotProps.data"></ChooseLoSanPham>
                              
                            </template>
                        </Column>
                    </DataTable>
                </div>
                <div style="width: 1000px; text-align: center;">
                    <Button class="p-button-outlined" outlined severity="secondary"
                        style="width: 200px; height: auto; margin: 10px;" @click="hideDialog()" label="Đóng"></Button>
                </div>

            </div>
        </form>
    </Dialog>
</template>