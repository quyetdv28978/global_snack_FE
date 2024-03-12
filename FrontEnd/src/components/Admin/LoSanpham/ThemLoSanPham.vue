<script setup>
import { useToast } from 'primevue/usetoast';
import { useForm, useField } from 'vee-validate';
import { ref } from 'vue';
import * as yup from 'yup';
import { useLoSanPhamService } from '@/service/Admin/LoSanPham/LoSanPhamServiceAPI';

const loSanPhamService = useLoSanPhamService();
const product = ref({});
const toast = useToast();
const submitted = ref(false);
const productDialog = ref(false);
// confirm thêm
const addProductDialog = ref(false);

const schema = yup.object().shape({
    ten: yup
        .string()
        .required('Tên không được để trống!')
        .max(30, 'Tên giới hạn 30 ký tự')
        .matches(/^[a-zA-Z0-9đĐáÁàÀảẢãÃạẠăĂắẮằẰẳẲẵẴặẶâÂấẤầẦẩẨẫẪậẬêÊếẾềỀểỂễỄệỆôÔốỐồỒổỔỗỖộỘỏỎóÓòÒõÕọỌẻẺéÉèÈẽẼẹẸỉỈíÍìÌĩĨịỊơƠớỚờỜởỞỡỠợỢùÙúÚụỤủỦũŨưỨỨửỬữỮựỰýÝỳỲỷỶỹỸỵỴ\s]*$/, 'Tên không được chứa kí tự đặc biệt!'),
});
const { handleSubmit, resetForm } = useForm({
    validationSchema: schema
});
const { value: ten, errorMessage: tenError } = useField('ten');
const { value: ma, errorMessage: maError } = useField('ma');
const { value: ngayHetHan, errorMessage: ngayHetHanError } = useField('ngayHetHan');
const onSubmit = handleSubmit(async (values) => {
    try {
    } catch (error) {
        console.error('Lỗi xử lý dữ liệu:', error);
    }
});
//add
const containsSpecialCharacters = (ten) => {
    return /[!@#$%^&*(),.?":{}|<>]/.test(ten);
};
const isTenTooLong = (ten) => {
    return ten.length > 30;
};
const addProduct = () => {
    submitted.value = true;
    const form = {
        tenLo: ten.value,
        maLo: ma.value,
        ngayHetHan: ngayHetHan.value,
    };
    console.log(form);

    // Kiểm tra trường "ten" có trống (null hoặc chuỗi rỗng) hoặc có chứa ký tự đặc biệt không
    if (form.tenLo == null || form.tenLo.length <=0 ) {
        ten.value = '';
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    } else if (containsSpecialCharacters(form.tenLo)) {
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    } else if (isTenTooLong(form.tenLo)) {
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    }
    else if (form.maLo == null || form.maLo.length <=0 ) {
        ma.value = '';
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    } else if (containsSpecialCharacters(form.mmaLoa)) {
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    } else if (isTenTooLong(form.maLo)) {
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    }
    else if (form.ngayHetHan == null || form.ngayHetHan.length <=0 ) {
        ngayHetHan.value = '';
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    } else if (containsSpecialCharacters(form.ngayHetHan)) {
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    } else if (isTenTooLong(form.ngayHetHan)) {
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Thêm thất bại', life: 3000 });
    } else {
        const add = loSanPhamService.createMauSac(form);
        productDialog.value = false;
        toast.add({ severity: 'success', summary: 'Thông báo', detail: 'Thêm thành công', life: 3000 });
        clearForm();
    }
    addProductDialog.value = false;
    loadDataLoSanPham

};

const loadDataLoSanPham = async () => {
    await loSanPhamStore.fetchDataBySPCT(props.myProp.id);
    dataLoSanPham.value = loSanPhamStore.dataByStatus1;
};
const clearForm = () => {
    ten.value = '';
};
const array = ref([]);
const reset = () => {
    resetForm();
    array.value = null;
};

// mở form
const openNew = () => {
    product.value = {};
    submitted.value = false;
    productDialog.value = true;
};

//đóng form
const hideDialog = () => {
    productDialog.value = false;
    submitted.value = false;
};

//save
const saveProduct = () => {
    addProductDialog.value = true;
};
</script>
<template>
    <Button label="Thêm mới" icon="pi pi-plus" class="p-button-success mr-2" @click="openNew" />
    <Dialog v-model:visible="productDialog" :style="{ width: '600px' }" header="Thêm lô sản phẩm" :modal="true" class="p-fluid">
        <div class="card">
            <form @submit="onSubmit">
                <div class="p-fluid formgrid grid">
                    <div class="Field col-12" style="margin-bottom: 30px">
                        <span class="p-float-label">
                            <InputText id="ma" name="ma" type="text" v-model.trim="ma" :class="{ 'p-invalid': maError }" required="true" autofocus />
                            <label for="username">Mã lô</label>
                        </span>
                        <small class="p-error">{{ maError }}</small>
                    </div>
                </div>

                <div class="p-fluid formgrid grid">
                    <div class="Field col-12" style="margin-bottom: 30px">
                        <span class="p-float-label">
                            <InputText id="ten" name="ten" type="text" v-model.trim="ten" :class="{ 'p-invalid': tenError }" required="true" autofocus />
                            <label for="username">Tên lô</label>
                        </span>
                        <small class="p-error">{{ tenError }}</small>
                    </div>
                </div>

                <div class="p-fluid formgrid grid">
                    <div class="Field col-12" style="margin-bottom: 30px">
                        <label >Ngày hết hạn</label>
                        <span class="p-float-label">
                            <InputText id="ngayHetHan" name="ngayHetHan" type="date" v-model.trim="ngayHetHan" :class="{ 'p-invalid': ngayHetHanError }" required="true" autofocus />
                        </span>
                        <small class="p-error">{{ ngayHetHanError }}</small>
                    </div>
                </div>
             
            </form>
        </div>
        <Dialog v-model:visible="addProductDialog" :style="{ width: '450px' }" header="Confirm" :modal="true">
            <div class="flex align-items-center justify-content-center">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span v-if="product">Bạn có chắc chắn muốn thêm không ?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" class="p-button-text" @click="addProductDialog = false" />
                <Button label="Yes" icon="pi pi-check" class="p-button-text" @click="addProduct" />
            </template>
        </Dialog>
        <template #footer>
            <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="hideDialog" />
            <Button label="Save" icon="pi pi-check" class="p-button-text" @click="saveProduct" />
        </template>
    </Dialog>
    <!-- </div>
        </div>
    </div> -->
</template>
