<script setup>
import { useForm, useField, defineRule } from 'vee-validate';
import * as yup from 'yup';
import { reactive, ref, computed, onMounted, watch } from 'vue';
import TableLoai from './DataTableLoai.vue';
import TableSize from './DataTableSize.vue';
import TableThuongHieu from './DataTableThuongHieu.vue';
import TableMauSac from './DataTableMauSac.vue';
import TablevatLieu from './DataTableVatLieu.vue';
import TableTrongLuong from './DataTableTrongLuong.vue';
import TableLoSanPham from './DataTableLoSanPham.vue';
import { ProductStore } from '@/service/Admin/product/product.api';
import { useToast } from 'primevue/usetoast';
import { useCounterStore } from '@/service/Admin/ThuongHieu/ThuongHieuService.js';
import { SizeStore } from '@/service/Admin/Size/SizeService';
import { useLoaiService } from '@/service/Admin/Loai/LoaiService';
import { TrongLuongStore } from '@/service/Admin/TrongLuong/TrongLuong.api';
import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';
import { VatLieuStore } from '@/service/Admin/VatLieu/VatLieu.api';
// import DataTableLoSanPham from './DataTableLoSanPham.vue';



const toast = useToast();
const productStore = ProductStore();
const thuongHieuService = useCounterStore();
const sizeStore = SizeStore();
// const mauSacStore = TrongLuongStore();
const loaiStore = useLoaiService();
const trongLuongStore = TrongLuongStore();
const vatLieuStore = VatLieuStore();
const loSanPhamStore = useLoSanPhamService();

const schema = yup.object().shape({
      ten: yup
        .string()
        .required('Tên sản phẩm không được để trống')
        .min(4, 'Tên sản phẩm phải có ít nhất 4 ký tự')
        .matches(/^[a-zA-Z0-9đĐáÁàÀảẢãÃạẠăĂắẮằẰẳẲẵẴặẶâÂấẤầẦẩẨẫẪậẬêÊếẾềỀểỂễỄệỆôÔốỐồỒổỔỗỖộỘơƠớỚờỜởỞỡỠợỢùÙúÚụỤủỦũŨưỨỨửỬữỮựỰýÝỳỲỷỶỹỸỵỴ\s\-]*$/, 'Tên không được chứa kí tự đặc biệt!')
        .test('no-spaces', 'Tên không được chứa khoảng trắng', value => value && !/\s/.test(value)),
 
    loai: yup.number().required('loại sản phẩm không được để trống'),
    thuongHieu: yup.number().required('vui lòng chọn Thương hiệu sản phẩm '),
    vatLieu: yup.number().required(' vui lòng chọn Vật liệu sản phẩm '),
     moTa: yup.string().required('Vui lòng điền mô tả sản phẩm').min(10, 'Mô tả sản phẩm phải có ít nhất 10 ký tự')
    .test('no-spaces', 'Tên không được chứa khoảng trắng', value => value && !/\s/.test(value)),
    anh: yup.string().required('vui lòng chọn ảnh chính cho sản phẩm')
});

const { handleSubmit, resetForm } = useForm({
    validationSchema: schema
});

const { value: name, errorMessage: nameError } = useField('ten');
const { value: GiaBan, errorMessage: giaBanError } = useField('giaBan');
const { value: Loai, errorMessage: loaiError } = useField('loai');
const { value: ThuongHieu, errorMessage: thuongHieuError } = useField('thuongHieu');
const { value: vatLieu, errorMessage: vatLieuError } = useField('vatLieu');
const { value: loSanPham, errorMessage: loSanPhamError } = useField('loSanPham');
const { value: SoLuongSize, errorMessage: soLuongSizeError } = useField('soLuongSize');
const { value: TrongLuong, errorMessage: trongLuongError } = useField('trongLuong');
const { value: imgMauSac, errorMessage: ImgMauSacError } = useField('imgMauSac');
const { value: TrangThai, errorMessage: TrangThaiSacError } = useField('trangThai');
const { value: MoTa, errorMessage: MoTaSacError } = useField('moTa');
const { value: imagesProduct, errorMessage: imagesProductError } = useField('imagesProduct');
const { value: imagesChinh, errorMessage: imagestError } = useField('anh');
// const { value: soLuongMauSac, errorMessage: soLuongMauError } = useField('soLuongMauSac');

const isOpen = ref(true);

const onSubmit = handleSubmit(async (values) => {
    console.log(values);
    try {
        values.anh = values.anh.substring(values.anh.lastIndexOf("\\") + 1)
        // values.imgMauSac =  values.imgMauSac.map(value => value.substring(value.lastIndexOf("\\") + 1))
        // Kiểm tra trùng lặp trước khi thêm sản phẩm
        const isDuplicate = await productStore.checkDuplicateName(values); // Sử dụng `values.name` thay vì `name.value`
        if (isDuplicate) {
            // Hiển thị thông báo lỗi hoặc xử lý theo nhu cầu của bạn
            toast.add({ severity: 'error', summary: 'Error', detail: 'sản phẩm đã tồn tại', life: 3000 });
        } else {
            // for (let i = 0; i < values.giaBan.length; i++) {
            //     let giaBan = values.giaBan[i];
            //     //   let giaNhap = values.giaNhap[i];
            //     let soLuong = values.soLuongSize[i];
            //     // if (soLuong <= 0) {
            //     //     toast.add({ severity: 'error', summary: 'Error', detail: 'Số lượng sản phẩm phải lớn hơn 0', life: 3000 });
            //     //     return;
            //     // }
            //     // if (isNaN(soLuong)) {
            //     //     toast.add({ severity: 'error', summary: 'Error', detail: 'Giá trị phải là một số', life: 3000 });
            //     //     return;
            //     // }
            //     // if (giaBan <= 0) {
            //     //     toast.add({ severity: 'error', summary: 'Error', detail: 'Giá bán sản phẩm phải lớn hơn 0', life: 3000 });
            //     //     return;
            //     // }
            //     // if (isNaN(giaBan)) {
            //     //     toast.add({ severity: 'error', summary: 'Error', detail: 'Giá bán phải là một số', life: 3000 });
            //     //     return;
            //     // }
            //     // if (Number(giaBan) < 50000) {
            //     //     toast.add({ severity: 'error', summary: 'Error', detail: 'giá bán phải lớn hơn 50.000 đ' + giaBan, life: 3000 });
            //     //     return;
            //     // }
            //     // if (giaBan > 10000000) {
            //     //     toast.add({ severity: 'error', summary: 'Error', detail: 'Giá bán không lớn hơn 10.000.000 đ', life: 3000 });
            //     //     return;
            //     // }

            // }
            console.log(values);
            await productStore.add(values);
            toast.add({ severity: 'success', summary: 'Success Message', detail: 'Thêm thành công', life: 3000 });
            productDialog.value = false;
            reset();
        }
    } catch (error) {
        console.error('Lỗi xử lý dữ liệu:', error);
    }
});


const error = ref(false);

const formatPrice = (value) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const selectedCity = ref(null);
const selectedLoai = ref(null);
const selectedvatLieu = ref(null);

const array = ref([]);
const arrayGiaNhap = ref([]);
const arrayGiaBan = ref([]);
const arrayMauSac = ref([]);

const dataThuongHieu = ref([]);
//load data thương hiệu tất cả
const loadDataThuongHieu = async () => {
    await thuongHieuService.fetchDataByStatus(1);
    dataThuongHieu.value = thuongHieuService.dataByStatus1;
};

const dataLoai = ref([]);
const loadDataLoai = async () => {
    await loaiStore.fetchDataByStatus(1);
    dataLoai.value = loaiStore.dataByStatus1;
    //  console.log( loaiStore.data)
};

const dataTrongLuong = ref([]);
const loadDataTrongLuong = async () => {
    await trongLuongStore.fetchDataByStatus(1);
    dataTrongLuong.value = trongLuongStore.dataByStatus1;
};

const dataVatLieu = ref([]);
const loadDataVatLieu = async () => {
    await vatLieuStore.fetchDataByStatus(1);
    dataVatLieu.value = vatLieuStore.dataByStatus1;
};
onMounted(() => {
    loadDataThuongHieu();
    loadDataLoai();
    loadDataTrongLuong();
    loadDataVatLieu();
});

const reset = () => {
    resetForm();
    imagesProduct.value = [];
    array.value = [];
    arrayGiaBan.value = [];
    arrayGiaNhap.value = [];
    selectedLoai.value = null;
    selectedCity.value = null;
    selectedvatLieu.value = null;
    ImagesProduct.value = [];
    imageUrls.value = [];
    anh.value = 'https://cdn-icons-png.flaticon.com/512/2956/2956744.png';
};

const handleInputChange = (sizeId) => {
    if (array.value.length > 0) {
        SoLuongSize.value = array.value.join(',').replace(/^,/, '').split(',').map(Number);
    } else {
        SoLuongSize.value = null;
    }
};

const handleInputChangeGiaBan = (sizeId) => {
    if (arrayGiaBan.value.length > 0) {
        GiaBan.value = arrayGiaBan.value.join(',').replace(/^,/, '').split(',').map(Number);
        //  console.log(GiaBan.value);
    } else {
        GiaBan.value = null;
    }
};

const handleInputChangeGiaNhap = (sizeId) => {
    if (arrayGiaNhap.value.length > 0) {
        GiaNhap.value = arrayGiaNhap.value.join(',').replace(/^,/, '').split(',').map(Number);
        //    console.log(GiaNhap.value);
    } else {
        GiaNhap.value = null;
    }
};


const onCityChange = () => {
    if (selectedCity.value) {
        ThuongHieu.value = selectedCity.value.id;
    } else {
        ThuongHieu.value = null;
    }
};

const onLoSanPhamChange = () => {
    if (selectedLoSanPham.value) {
        loSanPham.value = selectedLoSanPham.value.tenLo;
        //    console.log(TrongLuong.value)
    } else {
        TrongLuong.value = null;
    }
};

const onloaiChange = () => {
    if (selectedLoai.value) {
        Loai.value = selectedLoai.value.id;
    } else {
        Loai.value = null;
    }
};

const onvatLieuChange = () => {
    if (selectedvatLieu.value) {
        vatLieu.value = selectedvatLieu.value.id;
    } else {
        vatLieu.value = null;
    }
};

const arrayImgMauSac = ref([]);
function onFileInputImageMauSac(event) {
    const files = event.target.files;
    for (let i = 0; i < files.length; i++) {
    
        const file = files[i];
        const objectURL = URL.createObjectURL(file);
        const basePath = ""; // Đường dẫn cố định
        const fileName = basePath + file.name;
        arrayImgMauSac.value.push(fileName);
    }
    imgMauSac.value = arrayImgMauSac.value.join(',').replace(/^,/, '').split(',');
}


const anh = ref('https://cdn-icons-png.flaticon.com/512/2956/2956744.png');

function onFileInputImage(event) {
    const files = event.target.files;
    // Lặp qua từng tệp trong mảng files
    for (const file of files) {
        const objectURL = URL.createObjectURL(file);
        // Gán giá trị cho phần tử có id là 'imagesChinh' (thay đổi id nếu cần)
        const basePath = ""; // Đường dẫn cố định
        const fileName = basePath + file.name;
        imagesChinh.value = fileName;
        anh.value = objectURL;
        // console.log('anh chinh: ', imagesChinh.value)
    }
}

const ImagesProduct = ref([]);
const imageUrls = ref([]);
function onFileInputImageProduct(event) {
    const files = event.target.files;
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const objectURL = URL.createObjectURL(file);
        const basePath = ""; // Đường dẫn cố định
        const fileName = basePath + file.name;
        imageUrls.value.push(fileName);

    }
    ImagesProduct.value = imageUrls.value;
    imagesProduct.value = ImagesProduct.value.join(',').replace(/^,/, '').split(',');
}

const product = ref({});
const submitted = ref(false);
const productDialog = ref(false);
const openNew = () => {
    product.value = {};
    submitted.value = false;
    productDialog.value = true;
};
</script>

<template>
    <Button label="Thêm mới" icon="pi pi-plus" class="p-button-success mr-2" @click="openNew" />
    <div>
        <Dialog v-model:visible="productDialog" :style="{ width: '1100px' }" header="Thêm Mới Sản Phẩm" :modal="true"
            class="p-fluid">
            <div class="col-12">

                <div class="card">
                    <form @submit="onSubmit">
                        <div class="p-fluid formgrid grid">
                            <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                <div class="Field col-12 md:col-12" style="margin-bottom: 30px">
                                    <span class="p-float-label">
                                        <InputText id="name" name="name" type="text" v-model="name"
                                            :class="{ 'p-invalid': nameError }"></InputText>
                                        <label for="username">Tên sản phẩm</label>
                                    </span>
                                    <small class="p-error">{{ nameError }}</small>
                                </div>
   
                                <div class="p-fluid formgrid grid">
                                    <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                        <div style="display: flex">
                                            <span class="p-float-label" style="width: 239px">
                                                <Dropdown id="dropdown" :options="dataLoai" v-model="selectedLoai"
                                                    optionLabel="ten" :class="{ 'p-invalid': loaiError }"
                                                    @change="onloaiChange"> </Dropdown>
                                                <label for="dropdown">Loại</label>
                                            </span>
                                            <TableLoai :tableId="'tableLoai'" :rightGhId="'right_ghLoai'"
                                                :tableClass="'tableLoai'" :rightGhClass="'right_ghLoai'" />
                                        </div>

                                        <small class="p-error">{{ loaiError }}</small>
                                    </div>
                                    <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                        <div style="display: flex">
                                            <span class="p-float-label" style="width: 239px">
                                                <Dropdown id="dropdown" :options="dataThuongHieu" v-model="selectedCity"
                                                    optionLabel="ten" @change="onCityChange"
                                                    :class="{ 'p-invalid': thuongHieuError }"></Dropdown>
                                                <label for="dropdown">Thương Hiệu</label>
                                            </span>
                                            <TableThuongHieu :tableId="'TableThuongHieu'" :rightGhId="'right_ghThuongHieu'"
                                                :tableClass="'TableThuongHieu'" :rightGhClass="'right_ghThuongHieu'" />
                                        </div>

                                        <small class="p-error">{{ thuongHieuError }}</small>
                                    </div>
                                  
                                    <div class="Field col-12 md:col-6" style="margin-bottom: 30px">
                                        <div style="display: flex">
                                            <span class="p-float-label" style="width: 239px">
                                                <Dropdown id="dropdown" :options="dataVatLieu" v-model="selectedvatLieu"
                                                    :class="{ 'p-invalid': vatLieuError }" optionLabel="ten"
                                                    @change="onvatLieuChange"> </Dropdown>
                                                <label for="dropdown">Vật liệu</label>
                                            </span>
                                            <TablevatLieu :tableId="'TablevatLieu'" :rightGhId="'right_ghvatLieu'"
                                                :tableClass="'TablevatLieu'" :rightGhClass="'right_ghvatLieu'" />
                                        </div>
                                        <small class="p-error">{{ vatLieuError }}</small>
                                    </div>

                                </div>
                                <div class="field col-12 md:col-12" style="margin-bottom: 30px">
                                    <label for="address">Mô tả</label>
                                    <Textarea id="address" rows="4" v-model="MoTa"
                                        :class="{ 'p-invalid': MoTaSacError }"></Textarea>
                                    <small class="p-error">{{ MoTaSacError }}</small>
                                </div>
                                <div class="field col-12 md:col-12">
                                    <file-upload :upload-url="uploadUrl" :pt="{buttonbar: {class: 'flex-nowrap'}}" :multiple="true" :maxFileSize="2000000"
                                        @input="onFileInputImageProduct"
                                        :class="{ 'p-invalid': imagesProductError }"></file-upload>
                                    <small class="p-error">{{ imagesProductError }}</small>
                                    
                                </div>

                            </div>

                            <div class="Field col-12 md:col-6">
                                <div class="Field col-12 md:col-12"
                                    style="margin-bottom: 30px;margin-left: 60px; height: 300px; margin-top: 60px; display: inline-flex; justify-content: center; align-items: center;display: block">
                                    <div class="t"
                                        style="border: 1px solid black; border-radius: 10px; width: 300px; height: 240px; margin-top: -60px">
                                        <img :src="anh" alt=""
                                            style="width: 275px; height: 230px; top: 50%; left: 50%; transform: translate(4%, 2%)" />
                                    </div>
                                    <div class="buton" style="margin-top: 10px">
                                        <FileUpload mode="basic" name="demo[]" accept="image/*" :maxFileSize="1000000"
                                            @input="onFileInputImage"
                                            style="display: flex; width: 200px; margin-left: 50px;" />
                                    </div>
                                    <small class="p-error">{{ imagestError }}</small>
                                </div>
                                <div class="field col-12 md:col-12">

                                    <small class="p-error">{{ ImgMauSacError }}</small>
                                </div>

                            </div>

                            <div style="width: 1000px; text-align: center">

                                <Button class="p-button-outlined" outlined severity="secondary"
                                    style="width: 200px; height: auto; margin: 10px" @click="reset()"
                                    label="clear"></Button>
                                <Button type="submit" class="p-button-outlined"
                                    style="width: 200px; height: auto; margin: 10px" label="Lưu"></Button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </Dialog>
    </div>
</template>

<style scoped></style>
