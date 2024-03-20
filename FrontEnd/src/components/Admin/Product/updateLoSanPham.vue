<script setup>
import { format } from 'date-fns';
import * as yup from 'yup';
import { useForm, useField } from 'vee-validate';
import { ref, onMounted, defineEmits, onBeforeMount } from 'vue';
import { useToast } from 'primevue/usetoast';
import { ProductStore } from '@/service/Admin/product/product.api';
import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';
import { TrongLuongStore } from '@/service/Admin/TrongLuong/TrongLuong.api';
import { khuyenMaiStore } from '@/service/Admin/KhuyenMai/KhuyenMaiService.js';
import TableTrongLuong from './DataTableTrongLuong.vue';
import TableKhuyenMai from './DataTableKhuyenMai.vue';
import DataTableLoSanPham from './DataTableLoSanPham.vue';


const loSanPhamStore = useLoSanPhamService();
const trongLuongStore = TrongLuongStore();
const khuyenmaiService = khuyenMaiStore();
const productStore = ProductStore();
const toast = useToast();
const product = ref({});
const submitted = ref(false);
const productDialog = ref(false);
const props = defineProps({
    myProp: {},
    idProduct: Number
});

// confirm thêm
const addProductDialog = ref(false);

//hiện confirm
const confirmAddProduct = () => {
    addProductDialog.value = true;
};
const schema = yup.object().shape({
    anh: yup.string().required('bạn cần chọn ảnh'),
    // soLuongTon: yup.number().required('số lượng không được để trống').typeError('Số lượng tồn phải là một số').min(1, 'Số lượng phải lớn hơn hoặc bằng 1').max(1000, 'số lượng quá lớn').nullable(),
    giaBan: yup
        .number()
        .required('Giá bán không được để trống')
        .min(50000, 'giá phải lớn hơn hoặc bằng 50.000 đ')
        .max(10000000, 'Giá bán không lớn hơn 10.000.000 đ'),
        //    trongLuong: yup.string().required('vui lòng chọn trọng lượng sản phẩm'),
    // trangThai: yup.number().required('vui lòng chọn trạng thái ')
});

const { handleSubmit, resetForm } = useForm({
    validationSchema: schema
});

const { value: idSP, errorMessage: idError } = useField('idSP');
const { value: anh, errorMessage: anhError } = useField('anh');
const { value: soluong, errorMessage: soLuongError } = useField('soLuongTon');
const { value: GiaBan, errorMessage: giaBanError } = useField('giaBan');
const { value: tenLo, errorMessage: loSanPhamError } = useField('tenLo');
const { value: TrongLuong, errorMessage: trongLuongError } = useField('trongLuong');
const { value: idKhuyenMai, errorMessage: idKhuyenMaiError } = useField('idKhuyenMai');


const emit = defineEmits(['update:myProp']);
const onSubmit = handleSubmit(async (values) => {
    submitted.value = true;
    if (values.idKhuyenMai == null || values.idKhuyenMai == '') {
        values.idKhuyenMai = '';
    }
    const form = {
        idSP: props.myProp.id,
        anh: values.anh,
        soLuongTon: values.soLuongTon,
        giaBan: values.giaBan,
        trongLuong: values.trongLuong,
        idKhuyenMai: values.idKhuyenMai,
        trangThai: values.trangThai,
        tenLo: values.tenLo
    };
    console.log(form);
    let data = await productStore.addLoSanPhamSPCT(form, props.myProp.id);
    emit('update:myProp', data);

    toast.add({ severity: 'success', summary: 'Success Message', detail: 'update thành công', life: 3000 });
    reset();
    productDialog.value = false;
});

const reset = () => {
    resetForm();
    anh.value = props.myProp.anh;
    selectedKhuyenMai.value = null;
    selectedTrongLuong.value = null;
    selectedLoSanPham.value = null;
};

const selectedKhuyenMai = ref(null);

const selectedLoSanPham = ref(null);
const dataLoSanPham = ref([]);
const loadDataLoSanPham = async () => {
    await loSanPhamStore.fetchDataBySPCT(props.myProp.id);
    dataLoSanPham.value = loSanPhamStore.dataByStatus1;
};
// mở form
const openNew = () => {
    product.value = {};
    submitted.value = false;
    productDialog.value = true;
    anh.value = props.myProp.anh;
    soluong.value = props.myProp.soLuongTon;
    GiaBan.value = props.myProp.giaBan;
  //  GiaNhap.value = props.myProp.giaNhap;
    TrangThai.value = props.myProp.trangThai.toString();
   
};

const anhs = ref(null);
function onFileInputImage(event) {
    const files = event.target.files;
    // Lặp qua từng tệp trong mảng files
    for (const file of files) {
        const objectURL = URL.createObjectURL(file);
        anhs.value = objectURL;
        // Gán giá trị cho phần tử có id là 'imagesChinh' (thay đổi id nếu cần)
        const basePath = 'D:\\imgDATN\\'; // Đường dẫn cố định
        const fileName = basePath + file.name;
        anh.value = fileName;
    }
}

//đóng form
const hideDialog = () => {
    productDialog.value = false;
    submitted.value = false;
};

//save
const saveProduct = () => {
    addProductDialog.value = true;
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
    loadDataTrongLuong();
    loadDataKhuyenmai();
    loadDataLoSanPham();
});

const khuyenmais = ref([]);
const khuyenmai = ref([]);
const loadDataKhuyenmai = async () => {
    await khuyenmaiService.getKhuyenMai();
    khuyenmai.value = khuyenmaiService.data;
    khuyenmais.value = khuyenmai.value.filter((data) => data.trangThai == 0 || data.trangThai == 2);
    khuyenmais.value.unshift({ ten: 'Không có', id: null, trangThai: '' });

    const selectedKhuyenMais = khuyenmais.value.find((item) => item.ten === props.myProp.tenKM);
    selectedKhuyenMai.value = selectedKhuyenMais;
    if (selectedKhuyenMai.value) {
        idKhuyenMai.value = selectedKhuyenMai.value.id;
    } else {
        idKhuyenMai.value = null;
    }
};

const selectedTrongLuong = ref(null);
const dataTrongLuong = ref([]);
const loadDataTrongLuong = async () => {
    await trongLuongStore.fetchDataByStatus(1);
    dataTrongLuong.value = trongLuongStore.dataByStatus1;

    const selectedTrongLuongs = dataTrongLuong.value.find((item) => item.value === Number(props.myProp.trongLuong));
    selectedTrongLuong.value = selectedTrongLuongs;
    if (selectedTrongLuong.value) {
        TrongLuong.value = selectedTrongLuong.value.id;
    } else {
        TrongLuong.value = null;
    }
};

const onTrongLuongKhuyenMai = () => {
    if (selectedKhuyenMai.value) {
        idKhuyenMai.value = selectedKhuyenMai.value.id;
    } else {
        idKhuyenMai.value = null;
    }
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
            return '- Hết hàng  '; 
             case 4:
            return '- Hết hạn sử dụng  ';

        default:
            return '';
    }
};
const formatDate = (dateTime) => {
    if (dateTime == null || dateTime.length <= 0) {
        return null;
    } else {
        return format(new Date(dateTime), 'yyyy/MM/dd HH:mm:ss');
    }
};
</script>
<template>
    <Button icon="pi pi-pencil" class="p-button-rounded p-button-success mr-2" @click="openNew" />

    <Dialog v-model:visible="productDialog" :style="{ width: '700px' }" header="Thêm lô sản phẩm" :modal="true" class="p-fluid">
        <div class="card">
            <form @submit="onSubmit">
                <div class="p-fluid formgrid grid">
                    <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                        <div class="Field col-12 md:col-12" style="margin-bottom: 30px">
                            <span class="p-float-label">
                                <InputNumber id="soluong" name = "soluong" v-model="soluong"  :class="{ 'p-invalid': soLuongError }"> </InputNumber>
                                <label for="SoLuongTon">Số lượng tồn</label>
                            </span>
                            <small class="p-error">{{ soLuongError }}</small>
                        </div>

                    
                        <div class="Field col-12 md:col-12" style="margin-bottom: 30px">
                            <span class="p-float-label">
                                <InputNumber id="number-input" name="GiaBan" v-model="GiaBan" :class="{ 'p-invalid': giaBanError }" readonly></InputNumber>
                                <label for="Field">Giá bán</label>
                            </span>
                            <small class="p-error">{{ giaBanError }}</small>
                        </div>
                       
                    </div>
                    <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                        <div class="Field col-12 md:col-12" style="margin-bottom: 30px; margin-left: 0px; height: 200px; margin-top: 60px; display: inline-flex; justify-content: center; align-items: center; display: block">
                            <div class="t" style="border: 1px solid black; border-radius: 10px; width: 230px; height: 190px; margin-top: -60px">
                                <img :src="anhs == null ? anh : anhs" alt="" style="width: 200px; height: 170px; top: 50%; left: 50%; transform: translate(4%, 2%)" />
                            </div>
                            <div class="buton" style="margin-top: 10px">
                                <FileUpload mode="basic" name="demo[]" accept="image/*" :maxFileSize="1000000" @input="onFileInputImage" style="display: flex; width: 200px; margin-left: 20px" />
                            </div>
                            <small class="p-error">{{ anhError }}</small>
                        </div>
                    </div>
                    <div class="Field col-12 md:col-12" style="margin-bottom: 30px; margin-top: 20px">
                        <div class="p-fluid formgrid grid">
                            <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                <div style="display: flex">
                                    <span class="p-float-label" style="width: 239px">
                                        <Dropdown id="dropdown" :options="dataLoSanPham" v-model="selectedLoSanPham"
                                        :optionLabel="(option) => `${option.tenLo} - ${option.ngayHetHan}  ${getStatusLabel(option.trangThai)}`"                                          :class="{ 'p-invalid': trongLuongError }" @change="onLoSanPhamChange"> </Dropdown>
                                        <label for="dropdown">Lô Sản phẩm</label>
                                    </span>
                                    <DataTableLoSanPham :tableId="'TableLoSanPham'" :rightGhId="'right_ghLoSanPham'" :tableClass="'TableLoSanPham'" :rightGhClass="'right_ghLoSanPham'" />
                                </div>
                                <small class="p-error">{{ LoSanPhamError }}</small>
                            </div>
                          
                            <!-- <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                <div style="display: flex">
                                    <span class="p-float-label" style="width: 239px">
                                        <Dropdown id="dropdown" :options="khuyenmais" v-model="selectedKhuyenMai" :optionLabel="(option) => `${option.ten}  ${getStatusLabel(option.trangThai)}`" @change="onTrongLuongKhuyenMai"> </Dropdown>
                                        <label for="dropdown">Khuyến Mại</label>
                                    </span>
                                    <TableKhuyenMai :tableId="'TableTrongLuong'" :rightGhId="'right_ghTrongLuong'" :tableClass="'TableTrongLuong'" :rightGhClass="'right_ghTrongLuong'" />
                                </div>
                            </div> -->
                        </div>
                    </div>

                    <div style="width: 1000px; text-align: center; display: flex; margin-left: 170px">
                        <Button type="submit" class="p-button-outlined" style="width: 100px; height: auto; margin: 10px 60px 10px 60px" label="Lưu"></Button>
                    </div>
                </div>
            </form>
        </div>
    </Dialog>
    <!-- </div>
        </div>
    </div> -->
</template>
