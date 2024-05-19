<script setup>
import { ref } from 'vue';

const product = ref({});
const productDialog = ref(false);

const props = defineProps({
    myProp: {}
});

const editProduct = () => {
    product.value = { ...editProduct };
    productDialog.value = true;
};
</script>
<template>
    <Button icon="pi pi-eye" class="p-button-rounded p-button-success mr-2" @click="editProduct()" />

    <Dialog v-model:visible="productDialog" :style="{ width: '30%' }" header="Detail user" :modal="true"
        class="p-fluid">
        <div class="flex" style="jus">
            <!-- Cột phải -->
            <div class="p-col-12">
                <div class="col-12">
                <img :src="myProp.anh" alt="User Image" style="width: 100px;  height : 100px; border-radius : 50%" />
                </div>
                <div class="col-12">
                    <label>Mã: <span>{{ myProp.ma }}</span></label>
                </div>
                <div class="col-12">
                    <label>Role: <span>{{ myProp.role }}</span></label>
                </div>
                <div class="col-12">
                    <label>Tên: <span>{{ myProp.ten }}</span></label>
                </div>
                <div class="col-12">
                    <label>Username: <span>{{ myProp.userName }}</span></label>
                </div>
                <div class="col-12">
                    <label>Giới tính: <span>{{ myProp.gioiTinh == 1 ? 'Nam' : 'Nữ' }}</span></label>
                </div>
                <div class="col-12">
                    <label>Ngày sinh: <span>{{ myProp.ngaySinh }}</span></label>
                </div>
                <div class="col-12">
                    <label>Sdt: <span>{{ myProp.sdt }}</span></label>
                </div>
                <div class="col-12">
                    <label>Email: <span>{{ myProp.email }}</span></label>
                </div>
                <div class="col-12">
                    <label>
                        Số lượng hóa đơn: <span>{{ myProp.soLuongHoaDon }}</span></label>
                </div>
                <div class="col-12" style="display : flex">
                    <label>Địa chỉ: </label>
                        <span v-for="i in myProp.diaChi">
                                <span>{{ i.diaChi }}</span>
                        </span>
                </div>
                <!-- <div class="col-12">
                    <label>Địa chỉ: <span>Thôn Đông Mai, xã Trung Nghĩa, huyện Yên Phong, tỉnh Bắc Ninh.</span></label>
                </div> -->
                <div class="col-12">
                    <label>Trạng thái: <span>{{ myProp.trangThai == 1 ? 'Tài khoản bị khóa' : 'Đang hoạt động'
                            }}</span></label>
                </div>
            </div>
            <!-- cột trái -->
        
        </div>
    </Dialog>
    <!-- </div>
        </div>
    </div> -->
</template>

<style lang="scss" scoped>
    label {
        font-size : 18px;
        font-weight : 700;
    }

     span {
        font-size : 16px;
        font-weight : 600;
    }
</style>