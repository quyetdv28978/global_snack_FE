<script setup>
import * as yup from 'yup';
import { useForm, useField } from 'vee-validate';
import { ref, onMounted, onBeforeMount, defineEmits } from 'vue';
import { useToast } from 'primevue/usetoast';
import { ProductStore } from '@/service/Admin/product/product.api';
import { TrongLuongStore } from '@/service/Admin/TrongLuong/TrongLuong.api';
import { khuyenMaiStore } from '@/service/Admin/KhuyenMai/KhuyenMaiService.js';
import TableTrongLuong from './DataTableTrongLuong.vue';
import TableKhuyenMai from './DataTableKhuyenMai.vue';
import DataTableLoSanPham from './DataTableLoSanPham.vue';


// const loSanPhamStore = useLoSanPhamService();
const trongLuongStore = TrongLuongStore();
const khuyenmaiService = khuyenMaiStore();
const productStore = ProductStore();
const toast = useToast();
const product = ref({});
const submitted = ref(false);
const productDialog = ref(false);
const props = defineProps({
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
        // .when(['giaNhap'], (giaBan, schema) => {
        //     return schema.test({
        //         test: function (value) {
        //             const giaNhap = this.resolve(yup.ref('giaNhap'));
        //             if (value < giaNhap) {
        //                 toast.add({ severity: 'error', summary: 'Error ', detail: 'Giá Nhập không được nhỏ hơn giá bán', life: 3000 });
        //             }
        //             return true;
        //         },
        //         message: 'Giá bán phải nhỏ hơn giá nhập'
        //     });
        // }),
    // giaNhap: yup.number().required('Giá nhập không được để trống').min(50000, 'giá phải lớn hơn hoặc bằng 50.000 đ').max(10000000, 'Giá bán không lớn hơn  10.000.000 đ'),
    trongLuong: yup.string().required('vui lòng chọn trọng lượng sản phẩm'),
    // trangThai: yup.number().required('vui lòng chọn trạng thái '),
});

const { handleSubmit, resetForm } = useForm({
    validationSchema: schema
});

const { value: idSP, errorMessage: idError } = useField('idSP');
const { value: anh, errorMessage: anhError } = useField('anh');
const { value: soluong, errorMessage: soLuongError } = useField('soLuongTon');
const { value: GiaBan, errorMessage: giaBanError } = useField('giaBan');
// const { value: GiaNhap, errorMessage: giaNhapError } = useField('giaNhap');
const { value: TrongLuong, errorMessage: trongLuongError } = useField('trongLuong');
const { value: tenLo, errorMessage: loSanPhamError } = useField('tenLo');
const { value: idKhuyenMai, errorMessage: idKhuyenMaiError } = useField('idKhuyenMai');
const { value: TrangThai, errorMessage: TrangThaiSacError } = useField('trangThai');
//add

const emit = defineEmits(['add:myProp'])
const onSubmit = handleSubmit(async (values) => {
    submitted.value = true;
    if (values.idKhuyenMai == null || values.idKhuyenMai == '') {
        values.idKhuyenMai = '';
    }
    console.log(values);
    const form = {
        idSP: props.idProduct,
        anh: values.anh,
        soLuongTon: values.soLuongTon,
        giaBan: values.giaBan,
        // giaNhap: values.giaNhap,
        trongLuong: values.trongLuong,
        idKhuyenMai: values.idKhuyenMai,
        trangThai: values.trangThai,
        tenLo: values.tenLo
    }
    const check = await productStore.checkDuplicateSPCT(form.trongLuong, props.idProduct);
    // const check = false;
    if (check) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'sản phẩm đã tồn tại', life: 3000 });
    } else {
        console.log(form);
        const data = await productStore.addSPCT(form);
        emit('add:myProp', data)
        toast.add({ severity: 'success', summary: 'Success Message', detail: 'update thành công', life: 3000 });
        reset();
        productDialog.value = false;
    }
});

// const selectedLoSanPham = ref(null);
// const dataLoSanPham = ref([]);
// const loadDataLoSanPham = async () => {
//     await loSanPhamStore.fetchDataByStatus(1);
//     dataLoSanPham.value = loSanPhamStore.dataByStatus1;
// };
const reset = () => {
    resetForm();
    selectedKhuyenMai.value = null;
    selectedTrongLuong.value = null;
    // selectedLoSanPham.value = null;
}

// mở form
const openNew = () => {
    // console.log(props.idProduct)
    product.value = {};
    submitted.value = false;
    productDialog.value = true;
    anhs.value = 'https://cdn-icons-png.flaticon.com/512/2956/2956744.png';
};

const anhs = ref(null);
function onFileInputImage(event) {
    const files = event.target.files;
    // Lặp qua từng tệp trong mảng files
    for (const file of files) {
        const objectURL = URL.createObjectURL(file);
        anhs.value = objectURL;
        // Gán giá trị cho phần tử có id là 'imagesChinh' (thay đổi id nếu cần)
        const basePath = ""; // Đường dẫn cố định
        const fileName = basePath + file.name;
        anh.value = fileName;
    }
}

//đóng form
const hideDialog = () => {
    productDialog.value = false;
    submitted.value = false;
};


const selectedKhuyenMai = ref(null);
//save
const saveProduct = () => {
    addProductDialog.value = true;
};



onMounted(() => {
    loadDataTrongLuong();
    loadDataKhuyenmai();
    // loadDataLoSanPham();
});

const khuyenmais = ref([]);
const khuyenmai = ref([]);
const loadDataKhuyenmai = async () => {
    await khuyenmaiService.getKhuyenMai();
    khuyenmai.value = khuyenmaiService.data;
    khuyenmais.value = khuyenmai.value.filter(data => data.trangThai == 0 || data.trangThai == 2);

};

const selectedTrongLuong = ref(null);
const dataTrongLuong = ref([]);
const loadDataTrongLuong = async () => {
    await trongLuongStore.fetchDataByStatus(1);
    dataTrongLuong.value = trongLuongStore.dataByStatus1;
};

const onTrongLuongChange = () => {
    if (selectedTrongLuong.value) {
        TrongLuong.value = selectedTrongLuong.value.id;
        //    console.log(TrongLuong.value)
    } else {
        TrongLuong.value = null;
    }
};

// const onLoSanPhamChange = () => {
//     if (selectedLoSanPham.value) {
//         tenLo.value = selectedLoSanPham.value.id;
//         //    console.log(TrongLuong.value)
//     } else {
//         tenLo.value = null;
//     }
// };

const onTrongLuongKhuyenMai = () => {
    if (selectedKhuyenMai.value) {
        idKhuyenMai.value = selectedKhuyenMai.value.id;
        //    console.log(TrongLuong.value)
    } else {
        idKhuyenMai.value = null;
    }
};



const getStatusLabel = (trangThai) => {
    switch (trangThai) {
        case 1:
            return '- Còn hạn';

        case 0:
            return '- Hết hạn';

        case 2:
            return '- Chưa hoạt động';

        case 3:
            return '- Hết khuyến mại';

        default:
            return null;
    }
};
</script>
<template>
    <Button label="" icon="pi pi-plus" class="p-button-success mr-2" @click="openNew"
        style="width: 50px; height: 40px; margin-left: 20px" />

    <Dialog v-model:visible="productDialog" :style="{ width: '700px' }" header="Thêm sản phẩm chi tiết" :modal="true"
        class="p-fluid">
        <div class="card">
            <form @submit="onSubmit">
                <div class="p-fluid formgrid grid">
                    <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                    
                        <div class="Field col-12 md:col-12" style="margin-bottom: 30px">
                            <span class="p-float-label">
                                <InputNumber id="number-input" name="GiaBan" v-model="GiaBan"
                                    :class="{ 'p-invalid': giaBanError }"></InputNumber>
                                <label for="Field">Giá bán</label>
                            </span>
                            <small class="p-error">{{ giaBanError }}</small>
                        </div>
                    
                    </div>
                    <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                        <div class="Field col-12 md:col-12"
                            style="margin-bottom: 30px;margin-left: 0px; height: 200px; margin-top: 60px; display: inline-flex; justify-content: center; align-items: center;display: block">
                            <div class="t"
                                style="border: 1px solid black; border-radius: 10px; width: 230px; height: 190px; margin-top: -60px">
                                <img :src="anhs" alt=""
                                    style="width: 200px; height: 170px; top: 50%; left: 50%; transform: translate(4%, 2%)" />
                            </div>
                            <div class="buton" style="margin-top: 10px">
                                <FileUpload mode="basic" name="demo[]" accept="image/*" :maxFileSize="1000000"
                                    @input="onFileInputImage" style="display: flex; width: 200px; margin-left: 20px;" />
                            </div>
                            <small class="p-error">{{ anhError }}</small>
                        </div>
                    </div>
                    <div class="Field col-12 md:col-12" style="margin-bottom: 30px; margin-top: 20px;">
                        <div class="p-fluid formgrid grid">
                            <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                <div style="display: flex">
                                    <span class="p-float-label" style="width: 239px">
                                        <Dropdown id="dropdown" :options="dataTrongLuong" v-model="selectedTrongLuong"
                                            optionLabel="value" :class="{ 'p-invalid': trongLuongError }"
                                            @change="onTrongLuongChange">
                                        </Dropdown>
                                        <label for="dropdown">Trọng Lượng</label>
                                    </span>
                                    <TableTrongLuong :tableId="'TableTrongLuong'" :rightGhId="'right_ghTrongLuong'"
                                        :tableClass="'TableTrongLuong'" :rightGhClass="'right_ghTrongLuong'" />
                                </div>
                                <small class="p-error">{{ trongLuongError }}</small>
                            </div>
                           

              
                            <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                <div style="display: flex">
                                    <span class="p-float-label" style="width: 239px">
                                        <Dropdown id="dropdown" :options="khuyenmais" v-model="selectedKhuyenMai"
                                            :optionLabel="(option) => `${option.ten}  ${getStatusLabel(option.trangThai)}`"
                                            @change="onTrongLuongKhuyenMai">
                                        </Dropdown>
                                        <label for="dropdown">Khuyễn Mại</label>
                                    </span>
                                    <TableKhuyenMai :tableId="'TableTrongLuong'" :rightGhId="'right_ghTrongLuong'"
                                        :tableClass="'TableTrongLuong'" :rightGhClass="'right_ghTrongLuong'" />
                                </div>

                            </div>

                            <!-- <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
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

                            </div> -->
                        </div>
                    </div>

                    <div style="width: 1000px; text-align: center;display: flex; margin-left: 170px;">

                        <Button class="p-button-outlined" outlined severity="secondary"
                            style="width: 100px; height: auto; margin: 10px" @click="reset()" label="clear"></Button>
                        <Button type="submit" class="p-button-outlined" style="width: 100px; height: auto; margin: 10px"
                            label="Lưu"></Button>
                    </div>
                </div>

            </form>
        </div>

    </Dialog>
    <!-- </div>
        </div>
    </div> -->
</template>
