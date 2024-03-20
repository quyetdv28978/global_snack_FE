<script setup>
import Breadcrumb from 'primevue/breadcrumb';
import { ref, onMounted, computed, watch } from 'vue';
import TabView from 'primevue/tabview';
import { useToast } from 'primevue/usetoast';
import TabPanel from 'primevue/tabpanel';
import { useDetailProductStore } from '@/service/KhachHang/DetailService'; // Đường dẫn đến store của bạn
import { useRoute } from 'vue-router';
import { gioHangStore } from '@/service/KhachHang/Giohang/GiohangCTService.js';
import { commentStore } from '@/service/KhachHang/CommentService.js';
import { useRouter } from 'vue-router';
import { soLuongGh } from '@/service/KhachHang/Giohang/cart.js';

const router = useRouter();
const gioHangService = gioHangStore();
const productStore = useDetailProductStore();
const commentService = commentStore();
const route = useRoute();
const idProduct = parseInt(route.params.id);
const store = soLuongGh();
const dataSanPham = ref({});

const quantity = ref(1);
const dataMauSac = ref([]);
console.log('dataMauSac', dataMauSac);
const dataSize = ref([]);
const loadImage = ref([]);
const products = ref([]);
const getSize = ref(null);
const dataListSPCT = ref([]);
const idMau = ref(null);
const soLuongTon = ref('');
const selectedMauSac = ref('');
let prevDataSizeLength = ref(null);
let prevDataMauLength = ref(null);
const noiDung = ref(null);
const traLoi = ref(null);

let expiryTime = 60 * 60 * 1000; // 1 giờ
onMounted(async () => {
    await loadData();
    await loaddataListSPCT();
    await loadImg();
    await loadDataSize();
    await loadDataMauSac();
    loadComment();
    loadPhanHoi();
    prevDataSizeLength.value = dataSize.value.length;
    prevDataMauLength.value = dataMauSac.value.length;

    setTimeout(function () {
        localStorage.removeItem('cart');
    }, expiryTime);
});

if (!localStorage.getItem('cart')) {
    let array = [];
    localStorage.setItem('cart', JSON.stringify(array));
}

const loadImg = async () => {
    await productStore.fetchAllImage(idProduct);
    loadImage.value = productStore.images;
};
const loadDataMauSac = async () => {
    await productStore.fetchAllMauSac(idProduct);
    dataMauSac.value = productStore.mauSacs;
    console.log(productStore.mauSacs);
};

const loadDataSize = async () => {
    await productStore.fetchAllSize(idProduct);
    dataSize.value = productStore.sizes;
};

const idSanPhamChiTiet = ref(null);
watch([getSize, idMau], async ([newGetSize, newIdMau]) => {

console.log("Day la quyet");
    if (getSize.value == null) {
        await productStore.fetchIdSPCT(idProduct, idMau.value);
        dataListSPCT.value = productStore.products;
        idSanPhamChiTiet.value = dataListSPCT.value.id;
    } else {
        await productStore.fetchIdSPCT(idProduct, idMau.value);
        dataListSPCT.value = productStore.products;
        idSanPhamChiTiet.value = dataListSPCT.value.id;
        quantity.value = 1;
        // await productStore.getMauSacBySize(idProduct, getSize.value);
        // dataMauSac.value = productStore.mauSacs;
    }
});

let isFirstRun = true;

watch([dataSize, dataMauSac], async ([newDataSize, newDataMau]) => {
    if (newDataSize.length !== prevDataSizeLength.value || newDataMau.length !== prevDataMauLength.value) {
        if (isFirstRun && newDataSize.length > 0 && newDataMau.length > 0) {
            selectedMauSac.value = newDataMau[0];
            idMau.value = selectedMauSac.value.id;
            getSize.value = newDataSize[0].id;
            isFirstRun = false;
        } else if (newDataSize.length === 0 && newDataMau.length > 0) {
            selectedMauSac.value = newDataMau[0];
            idMau.value = selectedMauSac.value.id;
        }
        quantity.value = 1;
        prevDataSizeLength.value = newDataSize.length;
        prevDataMauLength.value = newDataMau.length;
    }
});

const loaddataListSPCT = async () => {
    await productStore.fetchSPCTByIdSP(idProduct);
    dataListSPCT.value = productStore.products;
    console.log(dataListSPCT.value);
};

const getSLTonTong = async (idctsp) => {
    await productStore.getAllSLTon(idctsp);
    slTon.value = productStore.slTon;
};

const loadData = async () => {
    await productStore.fetchProductById(idProduct);
    console.log('productStore.product', productStore.product);
    dataSanPham.value = productStore.product;

    // loadImage.value = dataSanPham.value.images;
};

// const loadProducts = async () => {
//     await productStore.fetchAll();
//     products.value = productStore.products;
//     soLuongTon.value = productStore.slTon;
// };

const increment = () => {
    quantity.value += 1;
};

const decrement = () => {
    if (quantity.value > 1) {
        quantity.value -= 1;
    }
};

const responsiveOptions = ref([
    {
        breakpoint: '991px',
        numVisible: 4
    },
    {
        breakpoint: '767px',
        numVisible: 3
    },
    {
        breakpoint: '575px',
        numVisible: 1
    }
]);
const home = ref({
    icon: 'pi pi-home',
    to: { name: 'trang-chu' }
});
const items = ref([{ label: 'Sản phẩm', to: { name: 'san-pham' } }, { label: 'Sản phẩm chi tiết' }]);

const formatCurrency = (value) => {
    if (!value) return '';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const hasTenKH = ref(dataSanPham.value.tenKH !== null && dataSanPham.value.tenKH !== undefined);

const soLuongGH = ref(0);

const toast = useToast();
const dataSessoin = ref([]);
const dataGHCT = ref([]);

const addToCart = async () => {
    const cartItem = {
        soLuong: quantity.value,
        sanPhamChiTiet: idSanPhamChiTiet.value
    };

    const token = localStorage.getItem('token');
    if (quantity.value > dataListSPCT.value.soLuongTon) {
        toast.add({ severity: 'warn', summary: '', detail: 'Số lượng tồn không đủ', life: 5000 });
        return;
    }

    if (token == '' || token == null) {
        await gioHangService.addToCartSesion(cartItem, token);
        dataSessoin.value = gioHangService.dataSessions;
        // Lấy mảng từ sessionStorage và chuyển đổi nó trở lại thành mảng

        let array = JSON.parse(localStorage.getItem('cart')); // Phân tích chuỗi JSON thành mảng

        let found = false;
        for (let i = 0; i < array.length; i++) {
            if (array[i].idCTSP == dataSessoin.value.idCTSP) {
                // Cập nhật phần tử trong mảng
                if (array[i].soLuong + dataSessoin.value.soLuong > dataListSPCT.value.soLuongTon) {
                    toast.add({ severity: 'warn', summary: '', detail: 'Số lượng bạn chọn đã đạt mức tối đa của sản phẩm', life: 5000 });
                    return;
                }
                array[i].soLuong = array[i].soLuong + dataSessoin.value.soLuong;
                found = true;
                break;
            }
        }
        // Nếu không tìm thấy phần tử trong mảng, thêm phần tử mới
        if (!found) {
            array.push(dataSessoin.value);
        }
        // Lưu lại mảng vào sessionStorage
        localStorage.setItem('cart', JSON.stringify(array));
        demSLGH(token);
        toast.add({ severity: 'success', summary: 'Successful', detail: 'Thêm vào giỏ hàng thành công', life: 3000 });
        // router.push({ name: 'gio-hang' });
    } else {
        await gioHangService.getGHCTByIdctsp(token, dataListSPCT.value.id);
        dataGHCT.value = gioHangService.data;

        soLuongGH.value = parseInt(quantity.value) + parseInt(dataGHCT.value.soLuong);

        if (soLuongGH.value > dataListSPCT.value.soLuongTon) {
            toast.add({ severity: 'warn', summary: '', detail: 'Số lượng bạn chọn đã đạt mức tối đa của sản phẩm', life: 5000 });
            return;
        }

        await gioHangService.addToCart(cartItem, token);

        demSLGH(token);

        toast.add({ severity: 'success', summary: 'Successful', detail: 'Thêm vào giỏ hàng thành công', life: 3000 });
        //    router.push({ name: 'gio-hang' });
    }
};

const demSLGH = async (token) => {
    await gioHangService.countGHCT(token);
};

const muaNgay = async () => {
    const cartItem = {
        soLuong: quantity.value,
        sanPhamChiTiet: idSanPhamChiTiet.value
    };
    const token = localStorage.getItem('token');
    if (quantity.value > dataListSPCT.value.soLuongTon) {
        toast.add({ severity: 'warn', summary: '', detail: 'Số lượng tồn không đủ', life: 5000 });
        return;
    }

    if (token == '' || token == null) {
        await gioHangService.addToCartSesion(cartItem, token);
        dataSessoin.value = gioHangService.dataSessions;

        // Lấy mảng từ sessionStorage và chuyển đổi nó trở lại thành mảng

        let array = JSON.parse(localStorage.getItem('cart')); // Phân tích chuỗi JSON thành mảng

        let found = false;
        for (let i = 0; i < array.length; i++) {
            if (array[i].idCTSP == dataSessoin.value.idCTSP) {
                if (array[i].soLuong + dataSessoin.value.soLuong > dataListSPCT.value.soLuongTon) {
                    toast.add({ severity: 'warn', summary: '', detail: 'Số lượng bạn chọn đã đạt mức tối đa của sản phẩm', life: 5000 });
                    return;
                }
                array[i].soLuong = array[i].soLuong + dataSessoin.value.soLuong;
                found = true;
                break;
            }
        }
        // Nếu không tìm thấy phần tử trong mảng, thêm phần tử mới
        if (!found) {
            array.push(dataSessoin.value);
        }
        // Lưu lại mảng vào sessionStorage
        localStorage.setItem('cart', JSON.stringify(array));
        demSLGH(token);
        toast.add({ severity: 'success', summary: 'Successful', detail: 'Thêm vào giỏ hàng thành công', life: 3000 });
        router.push({ name: 'gio-hang' });
    } else {
        await gioHangService.getGHCTByIdctsp(token, dataListSPCT.value.id);
        dataGHCT.value = gioHangService.data;

        soLuongGH.value = parseInt(quantity.value) + parseInt(dataGHCT.value.soLuong);

        if (soLuongGH.value > dataListSPCT.value.soLuongTon) {
            toast.add({ severity: 'warn', summary: '', detail: 'Số lượng bạn chọn đã đạt mức tối đa của sản phẩm', life: 5000 });
            return;
        }
        const res = await gioHangService.addToCart(cartItem, token);
        demSLGH(token);
        toast.add({ severity: 'success', summary: 'Successful', detail: 'Thêm vào giỏ hàng thành công', life: 3000 });
        router.push({ name: 'gio-hang' });
    }
};

const anh = ref(false);
const selectMauSac = async (mauSacs) => {
    // Loại bỏ viền đỏ của màu sắc đã chọn trước đó (nếu có)

    anh.value = true;

    checkAnh.value = false;
    if (selectedMauSac.value) {
        selectedMauSac.value.selected = false;
    }

    // Nếu màu sắc đã được chọn thì hủy chọn
    if (mauSacs === selectedMauSac.value) {
        selectedMauSac.value = null;
    } else {
        // Nếu màu sắc chưa được chọn, thêm viền đỏ và đánh dấu đã chọn
        selectedMauSac.value = mauSacs;
        selectedMauSac.value.selected = true;
        idMau.value = selectedMauSac.value.id;

        //  await getSizeByMauSac(mauSacs.id);
    }
};

const isMauSacSelected = (mauSacs) => {
    return mauSacs === selectedMauSac.value;
};

const isMauSacDisbled = (size) => {
    return size === null;
};

const getMauSacBySize = async (idsizect) => {
    await productStore.getMauSacBySize(idProduct, idsizect);
    dataMauSac.value = productStore.mauSacs;
};

const check = ref(false);

const validateQuantity = () => {
    quantity.value = quantity.value.replace(/[^0-9]/g, '');
    // Kiểm tra nếu giá trị nhập vào là số âm thì đặt lại giá trị thành 0
    if (quantity.value < 0) {
        quantity.value = 1;
    }
};

const setDefaultQuantity = () => {
    // Kiểm tra nếu giá trị là rỗng, đặt giá trị mặc định là 1
    if (!quantity.value) {
        quantity.value = 1;
    }
};

const comment = ref('');

const phanhoi = ref('');

const comments = ref([]);

const phanHois = ref([]);

const resetNoiDung = () => {
    noiDung.value = '';
};

const resetPhanHoi = () => {
    traLoi.value = '';
};

const addComment = async () => {
    const form = {
        noiDung: noiDung.value,
        sanPham: idProduct
    };

    const token = localStorage.getItem('token');

    if (token == '' || token == null) {
        toast.add({ severity: 'warn', summary: '', detail: 'Bạn cần đăng nhập để bình luận', life: 5000 });
        return;
    }

    await commentService.addComment(form, token);
    comment.value = commentService.data;

    resetNoiDung();
};

const loadComment = async () => {
    const token = localStorage.getItem('token');

    if (token) {
        await commentService.getListComment(idProduct);
        comments.value = commentService.data;
    }
};

const loadPhanHoi = async () => {
    const token = localStorage.getItem('token');

    if (token) {
        await commentService.getListCommentByIdPhanHoi();
        phanHois.value = commentService.dataPhanHoi;
    }
};
const user = ref(null);

const deleteDialog = ref(false);
const idcomment = ref(null);
const xoa = (comment) => {
    idcomment.value = comment;
    deleteDialog.value = true;
};

const deleteComment = async () => {
    const token = localStorage.getItem('token');

    await commentService.findByToken(token);
    user.value = commentService.user;

    if (token) {
        // if(comment.user.id !== user.id )
        if (idcomment.value.user.id !== user.value.id) {
            toast.add({ severity: 'warn', summary: '', detail: 'Bạn không thể xoá bình luận của người khác', life: 5000 });
            return;
        }

        await commentService.xoaComment(idcomment.value.id);
        loadComment();
        toast.add({ severity: 'warn', summary: '', detail: 'Xoá thành công', life: 3000 });
        deleteDialog.value = false;
    }
    // VoucherService.deleteVoucher(voucher.value, voucher.value.id);
    // toast.add({ severity: 'warn', summary: '', detail: 'Xoá thành công', life: 3000 });
    // deleteDialog.value = false;
};
const phanHoiDialog = ref(false);
const commentId = ref(null);
const phanHoi = (id) => {
    phanHoiDialog.value = true;
    commentId.value = id;
};

const xoaPhanHoi = async (phanhoi) => {
    const token = localStorage.getItem('token');

    await commentService.findByToken(token);
    user.value = commentService.user;

    if (token) {
        // if(comment.user.id !== user.id )
        if (phanhoi.user.id !== user.value.id) {
            toast.add({ severity: 'warn', summary: '', detail: 'Bạn không thể xoá bình luận của người khác', life: 5000 });
            return;
        }

        await commentService.xoaComment(phanhoi.id);
        loadPhanHoi();
        toast.add({ severity: 'warn', summary: '', detail: 'Xoá thành công', life: 3000 });
    }
};

const phanHoiComment = async () => {
    const form = {
        idPhanHoi: commentId.value,
        noiDung: traLoi.value,
        sanPham: idProduct
    };

    const token = localStorage.getItem('token');

    if (token == '' || token == null) {
        toast.add({ severity: 'warn', summary: '', detail: 'Bạn cần đăng nhập để bình luận', life: 5000 });
        return;
    }

    await commentService.addPhanHoi(form, token);
    // comment.value = commentService.data;
    phanHoiDialog.value = false;
    resetPhanHoi();
};

const checkAnh = ref(true);
const isAnh = () => {
    checkAnh.value = true;
    anh.value = false;
};

const value = ref(3);

const menu = ref();
</script>

<template>
    <div class="card">
        <div class="grid">
            <div class="container">
                <h2>Sản Phẩm Chi Tiết</h2>
                <!-- Cột phải -->
                <div class="card md:flex md:justify-content-center" style="margin-top: 0px">
                    <div class="flex">
                        <div class="col-5">
                            <Galleria :value="loadImage" :responsiveOptions="responsiveOptions" :numVisible="5" containerStyle="max-width: 450px">
                                <template #item="anh" v-if="anh">
                                    <img :src="selectedMauSac.anh" :alt="slotProps.item" style="width: 100%" />
                                </template>

                                <template #item="slotProps">
                                    <img v-if="checkAnh" :src="slotProps.item.anh" :alt="slotProps.item" style="width: 100%" />
                                    <img v-if="anh" :src="selectedMauSac.anh" :alt="slotProps.item" style="width: 100%" />
                                </template>
                                <template #thumbnail="slotProps">
                                    <img :src="slotProps.item.anh" :alt="slotProps.item" @click="isAnh()" style="width: 100%" />
                                </template>
                            </Galleria>
                        </div>
                        <div class="col-7">
                            <h1 class="text-4xl text-gray-800">{{ dataSanPham.ten }}</h1>

                            <div class="flex gap-4">
                                <div class="flex">
                                    <span class="text-xl font-normal">Rate : </span>
                                    <Rating v-model="value" readonly :cancel="false" />
                                </div>
                                <div class="flex items-center gap-1">
                                    <span class="text-xl font-normal text-gray-400">90.6k </span>
                                    <span class="text-xl font-normal text-gray-950">Đánh Giá </span>
                                </div>
                                <div class="flex items-center gap-1">
                                    <span class="text-xl font-normal text-gray-400">200 </span>
                                    <span class="text-xl font-normal text-gray-950">Đã bán</span>
                                </div>
                            </div>

                            <div class="gb" style="display: flex">
                                <h2 s v-if="dataListSPCT.giaBan > 0 || dataListSPCT.giaBan == null">
                                <h2 s v-if="dataListSPCT.giaBan > 0 || dataListSPCT.giaBan == null">
                                    <!-- <p tyle="color: rgb(0, 0, 0)">{{ formatCurrency(dataListSPCT.giaBan) !== '' ? formatCurrency(dataListSPCT.giaBan) : 'Hết hàng' }}</p> -->
                                    <p class="text-red-500" s tyle="text-decoration: line-through" v-if="dataListSPCT.tenKM !== null && dataListSPCT.tenKM !== undefined">{{ formatCurrency(dataListSPCT.giaBan) }}</p>
                                    <p class="text-red-500" v-else-if="formatCurrency(dataListSPCT.giaBan) !== ''">{{ formatCurrency(dataListSPCT.giaBan) }}</p>
                                    <p class="text-red-500" v-else>Hết hàng</p>
                                </h2>
                                <p style="color: red; font-size: 25px" v-else>Hết hàng</p>
                                <div>
                                    <h2 style="color: red" v-if="dataListSPCT.tenKM !== null && dataListSPCT.tenKM !== undefined">{{ formatCurrency(dataListSPCT.giaSauGiam) }}</h2>
                                    <Tag v-if="dataListSPCT.tenKM !== null && dataListSPCT.tenKM !== undefined" severity="danger" style="width: 70px; height: 20px; margin-left: 5px; margin-bottom: 10px">Giảm {{ dataListSPCT.giaTriGiam }}%</Tag>
                                </div>
                            </div>

                            <label class="text-xl font-normal text-gray-950">Trọng lượng</label>
                            <br />
                            <div class="flex gap-4">
                                <div
                                    v-for="(mauSacs, index) in dataMauSac"
                                    :key="index"
                                    class="items-center justify-center px-7 py-2 inline-block border-1 border-solid border-gray-300 rounded-2xl text-xl text-black cursor-pointer mt-2 hover:border-red-500 hover:text-red-500"
                                    @click="selectMauSac(mauSacs)"
                                    :class="{ selected: isMauSacSelected(mauSacs), disabled: isMauSacDisbled(mauSacs) }"
                                >
                                    <div>{{ mauSacs.value }} {{ mauSacs.donVi }}</div>
                                </div>
                            </div>
                            <!-- <div class="flex gap-4">
                                <div class="items-center justify-center px-7 py-2 inline-block border-1 border-solid border-gray-300 rounded-2xl text-xl text-black cursor-pointer mt-2 hover:border-red-500 hover:text-red-500">100g</div>
                                <div class="items-center justify-center px-7 py-2 inline-block border-1 border-solid border-gray-300 rounded-2xl text-xl text-black cursor-pointer mt-2 hover:border-red-500 hover:text-red-500">200g</div>
                            </div> -->

                            <!-- <label class="ms">Size</label>
                                <br />
                                <div class="rounded-content-list">
                                    <div v-for="(size, index) in dataSize" :key="index" style="margin-right: 10px">
                                        <RadioButton v-model="getSize" inputId="ingredient2" name="pizza" :value="size.id" style="margin-right: 10px; color: white" />
                                        <label>{{ size.ten }} </label>
                                    </div>
                                </div> -->

                            <div class="mt-3 flex gap-4">
                                <Button
                                    class="cursor-pointer"
                                    severity="danger"
                                    text
                                    raised
                                    label="Thêm vào giỏ hàng"
                                    @click="addToCart"
                                    icon="pi pi-shopping-cart"
                                    :disabled="dataListSPCT.giaBan == null || dataListSPCT.soLuongTon <= 0 || quantity <= 0"
                                />
                                <Button class="cursor-pointer" severity="danger" label="Mua ngay" @click="muaNgay" :disabled="dataListSPCT.giaBan == null || dataListSPCT.soLuongTon <= 0 || quantity <= 0" />
                            </div>
                            <hr />
                            <div class="text-gray-600 text-xl">Trả hàng miễn phi trong 30 ngày</div>
                        </div>
                    </div>

                    <div class="tab-view-container">
                        <TabView>
                            <TabPanel header="Mô tả sản phẩm">
                                <label for="" class="texl-xl font-medium my-5"> {{ dataSanPham.moTa }}</label>
                                <div class="text-3xl font-medium">Chi tiết sản phẩm</div>
                                <div class="flex flex-column gap-4">
                                    <div class="item mt-3">
                                        <label class="text-xl text-gray-500">Loại sản phẩm: </label>
                                        <span class="text-xl"> {{ dataSanPham.loai }} </span>
                                    </div>
                                    <div class="item">
                                        <label class="text-xl text-gray-500">Vật Liệu: </label>
                                        <span class="text-xl"> {{ dataSanPham.vatLieu }} </span>
                                    </div>
                                    <div class="item">
                                        <label class="text-xl text-gray-500">Trọng lượng: </label>
                                        <span class="text-xl"> {{ dataSanPham.dictionary }} </span>
                                    </div>

                                    <div class="item">
                                        <label class="text-xl text-gray-500">Đạt chuẩn: </label>
                                        <a class="text-xl text-black">Quốc Gia</a>
                                    </div>
                                    <div class="item">
                                        <label class="text-xl text-gray-500">Sản xuất tại: </label>
                                        <span class="text-xl">Việt Nam </span>
                                    </div>
                                    <div class="item">
                                        <label class="text-xl text-gray-500">Thương hiệu: </label>
                                        <span class="text-xl text-black"> {{ dataSanPham.thuongHieu }} </span>
                                    </div>
                                    <div class="item">
                                        <label class="text-xl text-gray-500">Gửi Từ </label>
                                        <span class="text-xl text-black"> Hà Nội</span>
                                    </div>
                                </div>

                                <div class="mt-5">
                                    <div class="text-3xl font-medium">Mô tả sản phẩm</div>
                                    <div class="text-xl text-gray-700 mb-2 mt-2">CHÍNH SÁCH ĐỔI TRẢ:</div>
                                    <div class="text-xl text-gray-700 mb-2 mt-2">Đổi trả hàng trong 03 ngày với các lý do sau</div>
                                    <div class="text-xl text-gray-700 mb-2 mt-2">- 1 đổi 1 nếu không ưng ý sản phẩm vì bất cứ lý do gì</div>
                                    <div class="text-xl text-gray-700 mb-2 mt-2">- 1 đổi 1 hoặc hoàn tiền 100% khi có lỗi từ NBH</div>
                                    <div class="text-xl text-gray-700 mb-2 mt-2">Tiger Food rất mong được phục vụ quý khách !</div>
                                    <div class="text-xl text-gray-700 mb-2 mt-2">
                                        ăn vặt, ăn vặt ngon, ăn vặt việt nam, ăn vặt bánh tráng, bánh tráng, bánh tráng phơi sương, bánh tráng trộn, bánh tráng bơ, bánh tráng xi ke, bánh trang long an, bánh tráng phô mai, bánh tráng tây ninh, bánh
                                        tráng tỏi ớt, bánh tráng sate, bánh tráng me, bánh tráng cuộn, bánh tráng tiger food
                                    </div>
                                    <div class="text-xl text-gray-700 mb-2 mt-2">
                                        #setbanhtrang #banhtrangphoisuong #combobanhtrangphoisuong #anvat#anvatngon#anvatvietnam#anvatbanhtrang#banhtrang#banhtrangphoisuong#banhtrangtron#banhtrangbo#banhtrangxike#banhtranglo #banhtranglongan
                                        #banhtrangladua #banhtrangxanh #doanvat #banhtrangsate #banhtranglongan #banhtrangcuontom #banhtranglongan #banhtrangbo #banhtranganlien
                                    </div>
                                </div>
                            </TabPanel>
                            <TabPanel header="Đánh giá">
                                <br />
                                <div class="flex">
                                    <h6 style="margin-right: 10px"><span>1 </span> bình luận</h6>
                                    <label style="margin-left: 675px" for=""></label>
                                    <Dropdown :options="dataTrangThai" optionLabel="label" placeholder="Tất cả bình luận" class="w-full md:w-14rem" style="margin-left: 20px" />
                                </div>
                                <div class="">
                                    <Avatar icon="pi pi-user" class="" size="xlarge" />
                                    <span class="p-float-label">
                                        <Textarea v-model="noiDung" rows="5" cols="145" />
                                    </span>
                                    <Toast />

                                    <div class="flex flex-wrap justify-content-between align-items-center gap-3 mt-3" style="margin-left: 900px">
                                        <Button type="submit" label="Đăng" @click="addComment" />
                                    </div>
                                </div>
                                <div class="flex" v-for="(cm, index) in comments" :key="index" style="margin-bottom: 10px">
                                    <div>
                                        <Avatar label="P" class="mr-2" size="xlarge" />
                                    </div>
                                    <div>
                                        <h6>
                                            <a href="">{{ cm.user.ten }}</a>
                                        </h6>
                                        <span>{{ cm.noiDung }}</span>

                                        <div class="flex flex-wrap align-items-center gap-3" style="margin-left: 700px">
                                            <a @click="phanHoi(cm.id)" style="color: blue; margin-right: 5px">Phản hồi</a>
                                            <a @click="xoa(cm)" style="color: red">Xoá</a>
                                        </div>

                                        <!-- Phản hồi -->
                                        <div v-for="(ph, index) in phanHois.filter((item) => item.idPhanHoi === cm.id)" :key="index" style="margin-top: 15px; margin-left: 30px" class="flex">
                                            <div>
                                                <Avatar label="P" class="mr-2" size="xlarge" />
                                            </div>
                                            <div>
                                                <h6>
                                                    <a href="">{{ ph.user.ten }}</a>
                                                </h6>
                                                <span>{{ ph.noiDung }}</span>
                                            </div>
                                            <!-- Nút phản hồi và xoá (bạn có thể di chuyển nó lên nếu muốn) -->
                                            <div class="flex flex-wrap justify-content-between align-items-center gap-3 mt-3" style="margin-left: 550px">
                                                <a @click="phanHoi(cm.id)" style="color: blue">Phản hồi</a>
                                                <a @click="xoaPhanHoi(ph)" style="color: red">Xoá</a>
                                            </div>
                                        </div>
                                        <div class="flex flex-wrap justify-content-between align-items-center gap-3 mt-3" style="margin-left: 660px">
                                            <!-- <Button type="submit" label="Phản hồi" @click="phanHoi(cm.id)" class="ph-button" />
                                            <Button severity="danger" label="Xoá" @click="xoa(cm)" class="small-button" /> -->

                                            <Dialog v-model:visible="phanHoiDialog" modal header="Phản hồi" :style="{ width: '50rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
                                                <Textarea v-model="traLoi" rows="5" cols="90" />
                                                <template #footer>
                                                    <Button label="Ok" icon="pi pi-check" @click="phanHoiComment" autofocus />
                                                </template>
                                            </Dialog>

                                            <Dialog v-model:visible="deleteDialog" :style="{ width: '450px' }" header="Confirm" :modal="true">
                                                <div class="flex align-items-center justify-content-center">
                                                    <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                                                    <span>Bạn có muốn xoá bình luận ?</span>
                                                </div>
                                                <template #footer>
                                                    <Button label="No" icon="pi pi-times" class="p-button-text" @click="deleteDialog = false" />
                                                    <Button label="Yes" icon="pi pi-check" class="p-button-text" @click="deleteComment" />
                                                </template>
                                            </Dialog>
                                        </div>
                                    </div>
                                </div>
                            </TabPanel>
                        </TabView>
                    </div>
                    <Divider />
                </div>
            </div>
        </div>
    </div>
    <!-- cột trái -->
</template>

<style scoped>
.phan-hoi-container {
    margin-top: 5px;
    margin-left: 55px;
}
div.selected {
    border: 2px solid red;
}

.small-button {
    width: 80px; /* Đặt chiều rộng mong muốn */
    height: 30px; /* Đặt chiều cao mong muốn */
    font-size: 14px; /* Đặt kích thước chữ mong muốn */
}
.ph-button {
    width: 100px; /* Đặt chiều rộng mong muốn */
    height: 30px; /* Đặt chiều cao mong muốn */
    font-size: 14px; /* Đặt kích thước chữ mong muốn */
}

.strikethrough {
    text-decoration: line-through;
    color: rgb(5, 5, 5);
    /* Màu chữ cho giá bán gạch ngang (tuỳ chọn) */
}

.grid {
    margin-top: 5rem;
    display: flex;
    justify-content: center;
}

.ms {
    color: #5a6069;
    font-weight: bold;
    /* Làm đậm chữ */
}

div.disabled {
    pointer-events: none;
    opacity: 0.6;
}

.masp {
    -webkit-text-size-adjust: none;
    color: rgba(0, 0, 0, 0.8);
    font-family: 'Helvetica Neue', Helvetica, Arial, 文泉驛正黑, 'WenQuanYi Zen Hei', 'Hiragino Sans GB', '儷黑 Pro', 'LiHei Pro', 'Heiti TC', 微軟正黑體, 'Microsoft JhengHei UI', 'Microsoft JhengHei', sans-serif;
    visibility: visible;
    --brand-primary-color: #d0011b;
    --brand-primary-light-color: rgba(208, 1, 27, 0.08);
    font-size: 26px;
    font-weight: 600;
    line-height: 24px;
    overflow-wrap: break-word;
    /* Làm đậm chữ */
}

.rounded-content-list {
    display: flex;
    flex-wrap: wrap;
}

.rounded-content {
    display: flex;
    align-items: center;
    padding: 1px;
    border: 1px solid #ccc;
    border-radius: 6px;
    cursor: pointer;
    margin: 4px;
    flex-basis: calc(25% - 8px);
    /* Đặt kích thước ban đầu của mỗi ô, chừa khoảng cách 8px giữa các ô */
    max-width: calc(20% - 8px);
    /* Đặt giới hạn kích thước tối đa của mỗi ô */
    box-sizing: border-box;
}

.rounded-content-size {
    display: flex;
    align-items: center;
    padding: 3px;
    border: 1px solid #ccc;
    border-radius: 6px;
    cursor: pointer;
    margin: 4px;
    width: 80px;
    height: 40px;
    /* flex-basis: calc(25% - 8px); 
    max-width: calc(25% - 8px);  */
    box-sizing: border-box;
}

.rounded-image {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    margin-right: 8px;
}

.rounded-text {
    font-size: 12px;
    color: #333;
}

.sl {
    display: flex;
    /* Sử dụng flexbox để căn chỉnh các phần tử theo chiều ngang */
    align-items: center;
    /* Căn phần tử theo chiều dọc để chúng đều nhau */
    gap: 10px;
    /* Khoảng cách giữa các phần tử */
}

/* CSS cho nút tăng giảm số lượng (phiên bản nhỏ hơn) */
.quantity {
    display: flex;
    align-items: center;
}

.minus,
.plus {
    cursor: pointer;
    font-size: 15px;
    /* Kích thước chữ nhỏ hơn */
    background-color: #ffffff;
    color: black;
    padding: 10px 16px;
    /* Kích thước padding nhỏ hơn */
    margin: 0;
    border-top-left-radius: 20px;
    /* Bo tròn góc trên trái của nút giảm số lượng */
    border-bottom-left-radius: 20px;
    /* Bo tròn góc dưới trái của nút giảm số lượng */
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    transition: background-color 0.3s ease;
    border: 1px solid #ccc;
}

.plus {
    border-top-right-radius: 20px;
    /* Bo tròn góc trên phải của nút tăng số lượng */
    border-bottom-right-radius: 20px;
    /* Bo tròn góc dưới phải của nút tăng số lượng */
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
}

/* CSS cho ô nhập số lượng */
.qty {
    font-size: 15px;
    /* Kích thước chữ nhỏ hơn */
    text-align: center;
    padding: 10px 12px;
    /* Kích thước padding nhỏ hơn */
    border: 1px solid #ccc;
    border-radius: 0;
    width: 50px;
    /* Kích thước width nhỏ hơn */
    margin: 0;
}

.ctn {
    display: flex;
    flex-direction: column;
    /* Xếp theo cột */
    align-items: flex-start;
    /* Căn các phần tử theo trục dọc (cột) */
}

.item {
    display: flex;
    align-items: center;
    /* Căn các phần tử label và span theo trục ngang */
    margin-bottom: 0px;
    /* Khoảng cách giữa các item */
}

.item label {
    width: 150px;
    /* Độ rộng cố định của label để tạo ra một cột cố định */
    text-align: left;
    /* Căn phải nếu bạn muốn label nằm ở bên phải */
    margin-right: 30px;
}

.container {
    width: 1100px;
}

.size-container {
    display: inline-block;
    /* hoặc sử dụng 'inline-flex' nếu bạn muốn chúng căng ra đều theo chiều ngang */
    margin-right: 10px;
    /* Khoảng cách giữa các phần tử */
}

.minus,
.plus {
    cursor: pointer;
    font-size: 15px;
    background-color: #ffffff;
    color: black;
    padding: 10px 12px;
    margin: 0;
    border-top-left-radius: 20px;
    border-bottom-left-radius: 20px;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    transition: background-color 0.3s ease;
    border: 1px solid #ccc;
}

.input-soluong {
    font-size: 17px;
    height: 44px;
    border: 1px solid #ccc;
    /* Đặt viền màu ghi và kích thước to */
    color: #333;
    /* Đặt màu chữ */
    /* padding: 0 12px;  */
    text-align: center;
    width: 30px;
    box-sizing: border-box;
}

.plus-phai {
    cursor: pointer;
    font-size: 15px;
    background-color: #ffffff;
    color: black;
    padding: 10px 12px;
    margin: 0;
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    border-top-right-radius: 20px;
    border-bottom-right-radius: 20px;
    transition: background-color 0.3s ease;
    border: 1px solid #ccc;
}

.selected {
    border-color: #e8bd72;
    /* Màu border khi được chọn */
}

.p-rating .p-rating-item.p-rating-item-active .p-rating-icon {
    color: yellow;
}
</style>
