<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useThongTin } from '@/service/KhachHang/ThongTinCaNhanService';
import * as yup from 'yup';
import { useForm, useField } from 'vee-validate';
import { useToast } from 'primevue/usetoast';
import { useDialog } from 'primevue/usedialog';
import DoiMatKhau from '@/components/KhachHang/DoiMatKhau.vue';

const toast = useToast();
const dialog = useDialog();
const product = ref({});
const submitted = ref(false);
const productDialog = ref(false);
const userService = useThongTin();
const route = useRoute();
const idUser = parseInt(route.params.id);
const dataUser = ref([]);

onMounted(() => {
    loadData();
});

const diaChiSelected = ref([]);

const loadData = async () => {
    const token = localStorage.getItem('token');
    // console.log(token);
    if (token && token.length > 0) {
        await userService.fetchData(token);
        dataUser.value = userService.data;
        ten.value = dataUser.value.ten;
        email.value = dataUser.value.email;
        sdt.value = dataUser.value.sdt;
        ngaySinh.value = dataUser.value.ngaySinh;
        gioiTinh.value = dataUser.value.gioiTinh.toString();
        image.value = dataUser.value.anh;
    }
};

const schema = yup.object().shape({
    ten: yup.string().required('Tên người dùng không được để trống').min(4, 'Tên phải có ít nhất 4 ký tự'),
    sdt: yup
        .string()
        .required('Số điện thoại không được để trống')
        .matches(/^[0-9]{10}$/, 'Số điện thoại phải chứa 10 số')
        .typeError('Số điện thoại phải là một số')
        .nullable(),
    gioiTinh: yup.string().required('Vui lòng chọn giới tính '),
    ngaySinh: yup.date().required(' vui lòng chọn ngày sinh '),
    email: yup
        .string()
        .matches(/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$/, 'Email không hợp lệ')
        .required(' Email không được để trống')
});
const { handleSubmit, resetForm } = useForm({
    validationSchema: schema
});
const { value: ten, errorMessage: tenError } = useField('ten');
const { value: email, errorMessage: emailError } = useField('email');
const { value: sdt, errorMessage: sdtError } = useField('sdt');
const { value: ngaySinh, errorMessage: ngaySinhError } = useField('ngaySinh');
const { value: gioiTinh, errorMessage: gioiTinhError } = useField('gioiTinh');
const { value: image, errorMessage: imageError } = useField('image');
const onSubmit = handleSubmit(async (values) => {
    try {
        //  console.log('Dữ liệu đã gửi:', values);
        // Sau khi xử lý, đặt lại biểu mẫu
        reset();
    } catch (error) {
        console.error('Lỗi xử lý dữ liệu:', error);
    }
});
const array = ref([]);
const reset = () => {
    resetForm();
    array.value = null;
};
// confirm sửa
const updateProductDialog = ref(false);
//nhận dữ liệu từ cha
const props = defineProps({
    myProp: {}
});

const updateProduct = () => {
    submitted.value = true;
    const form = {
        ten: ten.value,
        email: email.value,
        sdt: sdt.value,
        ngaySinh: ngaySinh.value,
        gioiTinh: gioiTinh.value,
        // diaChi: diaChi.value,
        image: image.value
    };

    schema
        .validate(form)
        .then(() => {
            // Dữ liệu hợp lệ, thực hiện thêm sản phẩm
            const update = userService.updateUser(dataUser.value.id, form);
            productDialog.value = false;
            toast.add({ severity: 'success', summary: 'Thông báo', detail: 'Sửa thành công', life: 3000 });
            reset();
            // loadData();
            // Cập nhật giá trị của các biến với dữ liệu mới
            ten.value = form.ten;
            email.value = form.email;
            sdt.value = form.sdt;
            ngaySinh.value = form.ngaySinh;
            gioiTinh.value = form.gioiTinh;
            image.value = form.image;
        })
        .catch((error) => {
            // Dữ liệu không hợp lệ, hiển thị thông báo lỗi bằng Toast
            toast.add({ severity: 'error', summary: 'Lỗi', detail: error.message, life: 3000 });
        });
    updateProductDialog.value = false;
};

// Hàm chuyển đổi tệp thành dạng Base64
// function convertFileToBase64(file) {
//     return new Promise((resolve, reject) => {
//         const reader = new FileReader();
//         reader.onloadend = () => {
//             resolve(reader.result);
//         };
//         reader.onerror = reject;
//         reader.readAsDataURL(file);
//     });
// }

function onFileInputImage(event) {
    const files = event.target.files;
    // Lặp qua từng tệp trong mảng files
    for (const file of files) {
        const objectURL = URL.createObjectURL(file);
        image.value = objectURL;
        // Gán giá trị cho phần tử có id là 'imagesChinh' (thay đổi id nếu cần)
        const basePath = "D:/images/"; // Đường dẫn cố định
        const fileName = basePath + file.name;
        image.value = fileName;
    }
}

const showDoiMatKhau = () => {
    const dialogRef = dialog.open(DoiMatKhau, {
        props: {
            header: 'Đổi mật khẩu',
            style: {
                width: '20vw'
            },
            modal: true
        },
        onClose: (options) => {}
    });
};
</script>

<template>
    <DynamicDialog />
    <div class="container" style="margin-top: 100px; margin-bottom: 100px">
        <div class="card">
            <h1 class="text-4xl text-gray-700">Hồ sơ của tôi</h1>
            <div class="flex justify-content-between">
                <label class="text-xl text-gray-500">Quản lý thông tin hồ sơ để bảo mật tài khoản</label>
            </div>
            <Button @click="showDoiMatKhau" label="Đổi mật khẩu" class="w-2 mt-2 text-lg font-thin border-none" />
            <Divider />

            <div class="ctn">
                <form @submit="onSubmit">
                    <div class="flex">
                        <div class="p-col-6">
                            <div class="col-12">
                                <div class="Field col-12 md:col-6">
                                    <div style="display: block">
                                        <div class="t" style="display: flex;align-items: center;gap: 1rem;">
                                            <div  style="width: 100px; height: 100px;border-radius : 50% ; border : 1px solid #e1e1e1 ; display : flex ;align-items: center">
                                            <img :src="image" alt="image" style="width: 100px; height: 100px;border-radius : 50%" />
                                            </div>
                                            <div class="buton" style="margin-top: 10px">
                                            <FileUpload mode="basic" name="demo[]" accept="image/*" :maxFileSize="1000000" @input="onFileInputImage" style="display: flex" />
                                        </div>
                                        </div>
                                        
                                        <small class="p-error">{{ imageError }}</small>
                                    </div>
                                </div>
                            </div>
                            <div class="item" style="margin-bottom: 30px">
                                <label class="text-lg font-medium text-gray-800">Tên đăng nhập: </label>
                                <span>{{ dataUser.userName }}</span>
                            </div>
                            <div class="item">
                                <div class="flex">
                                    <label class="text-lg font-medium text-gray-800">Tên: </label>
                                    <InputText id="ten" name="ten" type="text" v-model.trim="ten" :class="{ 'p-invalid': tenError }" required="true" autofocus />
                                    <small class="p-error">{{ tenError }}</small>
                                </div>
                            </div>
                            <div class="item">
                                <label class="text-lg font-medium text-gray-800">Email: </label>
                                <InputText id="email" v-model.trim="email" required="true" autofocus :class="{ 'p-invalid': emailError }" />
                                <small class="p-error">{{ emailError }}</small>
                            </div>
                            <div class="item">
                                <label class="text-lg font-medium text-gray-800">Số điện thoại: </label>
                                <InputText id="sdt" v-model.trim="sdt" required="true" autofocus :class="{ 'p-invalid': sdtError }" />
                                <small class="p-error">{{ sdtError }}</small>
                            </div>
                            <div class="s" style="display: flex">
                                <label class="text-lg font-medium text-gray-800">Giới tính: </label>
                                <div class="flex flex-wrap gap-3" style="margin-left: 120px; margin-bottom: 20px">
                                    <div class="flex align-items-center">
                                        <RadioButton id="nam" value="1" v-model.trim="gioiTinh" />
                                        <label for="nam" class="ml-2">Nam</label>
                                    </div>
                                    <div class="flex align-items-center">
                                        <RadioButton id="nu" value="0" v-model.trim="gioiTinh" />
                                        <label for="nu" class="ml-2">Nữ</label>
                                    </div>
                                </div>
                            </div>
                            <div class="item">
                                <label class="text-lg font-medium text-gray-800">Ngày sinh: </label>
                                <InputText type="date" v-model.trim="ngaySinh" :class="{ 'p-invalid': ngaySinhError }" />
                                <small class="p-error">{{ ngaySinhError }}</small>
                            </div>
                            <!-- <div class="item">
                  <label>Địa chỉ: </label>
                  <Dropdown v-model="selectedDiaChi" :options="diaChiSelected" />
                  <Button icon="pi pi-pencil" aria-label="Submit" style="margin-left: 15px" />
              </div> -->
                        </div>
                        
                    </div>
                    <div>
                        <Button class="px-3 bg-red-600 border-none" label="Sửa" icon="pi pi-check" size="small" @click="updateProduct" />
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped>
.ctn {
    display: flex;
    flex-direction: column; /* Xếp theo cột */
    align-items: flex-start; /* Căn các phần tử theo trục dọc (cột) */
}

.card {
    margin-top: 70px;
}

.item {
    display: flex;
    align-items: center; /* Căn các phần tử label và span theo trục ngang */
    margin-bottom: 20px; /* Khoảng cách giữa các item */
}

.item label {
    width: 150px; /* Độ rộng cố định của label để tạo ra một cột cố định */
    text-align: left; /* Căn phải nếu bạn muốn label nằm ở bên phải */
    margin-right: 30px;
}

.p-col-6 {
    min-width: 650px; /* Đặt khoảng cách giữa hai cột là tối thiểu 300px */
    margin-right: 20px; /* Khoảng cách giữa các cột (có thể điều chỉnh giá trị này theo ý muốn) */
}

/* Đảm bảo rằng không có khoảng cách bên phải của cột cuối cùng */
.p-col-6:last-child {
    padding-right: 0;
}

input {
    color: black;
    font-weight: 400;
    font-size: 16px;
    outline: none;
}
</style>
