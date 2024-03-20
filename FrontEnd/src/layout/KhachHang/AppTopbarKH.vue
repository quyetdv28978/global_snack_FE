<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue';
import { useLayout } from '@/layout/composables/layout';
import { useRouter } from 'vue-router';
import { userStore } from '@/service/Admin/User/UserService.js';
import { gioHangStore } from '@/service/KhachHang/Giohang/GiohangCTService.js';
import tokenService from '@/service/Authentication/TokenService.js';
import userKHService from '@/service/KhachHang/UserService.js';

import { HDKHStore } from '../../service/KhachHang/HoaDonKHService';
// import tokenService from '../../service/Authentication/TokenService.js';
import { KHThongBaoStore } from '../../service/KhachHang/ThongBaoService';
// import { HDStore } from from '../../../service/KhachHang/HoaDonKHService';

import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const useHD = HDKHStore();

const thongBaoStore = KHThongBaoStore();
const userService = userStore();
const { layoutConfig, onMenuToggle } = useLayout();
const outsideClickListener = ref(null);
const topbarMenuActive = ref(false);
const router = useRouter();
const selectedCustomer = ref(null);
const gioHangService = gioHangStore();
const maHD = ref(null);
const slGH = ref(localStorage.getItem('soLuongGH') || 0);
const dataHDCT = ref([]);

onMounted(() => {
    bindOutsideClickListener();
    getAllTB();
    getDem();
    soLuongGH();
    openSocketConnection();
});

// const cartItems = store.getCartItems;

const stompClient = ref(null);

const openSocketConnection = () => {
    stompClient.value = new Client({
        brokerURL: `${import.meta.env.VITE_BASE_WEBSOCKET_ENDPOINT}/ws`,
        onConnect: async () => {
            // console.log('Đã kết nối');
            // findUserByToken();
            const token = localStorage.getItem('token');
            if (token == '' || token == null) {
                return;
            } else {
                const respone = await tokenService.findByToken(token);
                stompClient.value.subscribe('/topic/hoa-don/' + respone.id, (message) => {
                    getAllTB();
                    getDem();
                    loadData();
                    loadDataByTrangThai(2);
                    loadDataByTrangThai(5);
                    loadDataByTrangThai(3);
                    loadDataByTrangThai(0);
                    loadDataByTrangThai(2);
                    loadDataTra();

                    loadDataHDCT(maHD.value);

                    // loadDataSpChiTiet();
                    // loadDataHD();
                });
            }
        }
    });

    stompClient.value.activate();
};

const loadDataHD = async () => {
    await useHD.findHdByIdHd();
    //   console.log(dataHD.value)
};

const loadDataSpChiTiet = async () => {
    await useHD.findHdctByIdHd();
};
const loadData = async () => {
    const token = localStorage.getItem('token');
    if (token.length > 0 || token != null) {
        await useHD.fetchData(token);
    }
};

const loadDataByTrangThai = async (status) => {
    const token = localStorage.getItem('token');
    if (token.length > 0 || token != null) {
        await useHD.fetchDataByStatus(token, status, '', '', '');
    }
};

const loadDataTra = async () => {
    const token = localStorage.getItem('token');
    if (token.length > 0 || token != null) {
        await useHD.findHdct(token);
    }
};

const data = ref([]);
const getAllTB = async () => {
    const token = localStorage.getItem('token');
    tokenCheck.value = token;
    if (token == '' || token == null) {
        return;
    } else {
        await thongBaoStore.fetchData(token);
        data.value = thongBaoStore.data;
    }
};

const tokenCheck = ref();

const dem = ref(0);
const getDem = async () => {
    const token = localStorage.getItem('token');
    if (token == '' || token == null) {
        return;
    } else {
        dem.value = await thongBaoStore.fetchdem(token);
        if (dem.value == undefined) {
            dem.value = 0;
            return;
        }
    }
};

const daXem = async (id) => {
    await thongBaoStore.daXem(id);
    getAllTB();
    getDem();
    router.push({ name: 'lich-su-san-pham' });
};

onBeforeUnmount(() => {
    unbindOutsideClickListener();
});

const logoUrl = computed(() => {
    return `https://nontrum.vn/wp-content/uploads/2021/12/NonTrum-logo-2022png.png`;
});

const onTopBarMenuButton = () => {
    topbarMenuActive.value = !topbarMenuActive.value;
};

const selectedUserId = computed(() => {
    return selectedCustomer.value ? selectedCustomer.value.id : null;
});

const onSettingsClick = (event) => {
    topbarMenuActive.value = false;
    if (event.item.label === 'Hồ sơ cá nhân') {
        router.push({ name: 'ho-so' });
    } else if (event.item.label === 'Lịch sử mua hàng') {
        router.push({ name: 'lich-su-san-pham' });
    } else if (event.item.label === 'Địa chỉ') {
        router.push({ name: 'dia-chi' });
    } else {
        // Xử lý trường hợp khác nếu cần
    }
};

const thongTinCaNhan = async () => {
    await router.push({ name: 'ho-so' });
};

const lichSuMuaHang = async () => {
    await router.push({ name: 'lich-su-san-pham' });
};

const diaChi = async () => {
    await router.push({ name: 'dia-chi' });
};

const dangXuat = async () => {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUserInformation');

    const arraySPDaXem = JSON.parse(localStorage.getItem('spDaXem'));
    const arraySauKhiDangXuat = [];
    if (Array.isArray(arraySPDaXem)) {
        for (let i = 0; i < arraySPDaXem.length; i++) {
            if (arraySPDaXem[i].idUser != -1) {
                arraySauKhiDangXuat.push(arraySPDaXem[i]);
            }
        }
        localStorage.setItem('spDaXem', JSON.stringify(arraySauKhiDangXuat));
    }

    await router.push({ name: 'logout' });
};

const dangNhap = async () => {
    await router.push({ name: 'login' });
};

const topbarMenuClasses = computed(() => {
    return {
        'layout-topbar-menu-mobile-active': topbarMenuActive.value
    };
});

const bindOutsideClickListener = () => {
    if (!outsideClickListener.value) {
        outsideClickListener.value = (event) => {
            if (isOutsideClicked(event)) {
                topbarMenuActive.value = false;
            }
        };
        document.addEventListener('click', outsideClickListener.value);
    }
};
const unbindOutsideClickListener = () => {
    if (outsideClickListener.value) {
        document.removeEventListener('click', outsideClickListener);
        outsideClickListener.value = null;
    }
};
const isOutsideClicked = (event) => {
    if (!topbarMenuActive.value) return;

    const sidebarEl = document.querySelector('.layout-topbar-menu');
    const topbarEl = document.querySelector('.layout-topbar-menu-button');

    return !(sidebarEl.isSameNode(event.target) || sidebarEl.contains(event.target) || topbarEl.isSameNode(event.target) || topbarEl.contains(event.target));
};

const selectedKH = ref(null);
const khachHang = ref([]);

// const fetchData = async () => {
//     try {
//         if(token !== null || token !== "undefined"){
//             await userService.getAllUser();
//         khachHang.value = userService.data;
//         }

//         //   console.log(khachHang.value);
//     } catch (error) {
//         // Xử lý lỗi ở đây nếu cần
//     }
// };

const isTokenValid = async (token) => {
    if (token) {
        try {
            // Gọi API `/validate-token` để kiểm tra tính hợp lệ của token
            const response = await tokenService.validatetoken(token);

            if (response.status === 200) {
                // Nếu API trả về mã 200 (OK), token hợp lệ
                return true;
            }
        } catch (error) {
            // Xử lý lỗi nếu có lỗi khi gọi API
            console.error('Error while validating token:', error);
        }
    }
};

// dùng để lưu thông tin khách hàng khi được chọn CBB.
// nếu muốn dùng thông tin khách hàng khi đặt hàng thì dùng selectedCustomer.value

// hàm gọi sự thay đổi thông tin của khách hàng khi click vào CBB

const soLuong = ref(0);

const soLuongGH = async () => {
    const token = localStorage.getItem('token');
    await gioHangService.countGHCT(token);
    soLuong.value = gioHangService.soLuong;
};

const menu = ref();
const items = ref([
    {
        label: 'Profiles',
        command: onSettingsClick
    },
    {
        label: 'Address',
        command: onSettingsClick
    },
    {
        label: 'History',
        command: onSettingsClick
    }
]);

const op2 = ref();
const toggle = (event) => {
    op2.value.toggle(event);
};

const op = ref();
const toggle2 = (event) => {
    op.value.toggle(event);
};

const loadDataHDCT = async (idHD) => {
    const respone = await useHD.findHdByIdHd(idHD);
    dataHDCT.value = respone;
};

const members = ref([
    { name: 'Amy Elsner', image: 'amyelsner.png', email: 'amy@email.com', role: 'Owner' },
    { name: 'Bernardo Dominic', image: 'bernardodominic.png', email: 'bernardo@email.com', role: 'Editor' },
    { name: 'Ioni Bowcher', image: 'ionibowcher.png', email: 'ioni@email.com', role: 'Viewer' }
]);
</script>

<template>
    <div class="layout-topbar" style="padding: 1rem 5rem">
        <router-link :to="{ name: 'trang-chu' }">
            <img src="/src/assets/images/logo.jpg" alt="logo" style="height: 70px" />
        </router-link>

        <button class="p-link layout-topbar-menu-button layout-topbar-button" @click="onTopBarMenuButton()">
            <i class="pi pi-ellipsis-v"></i>
        </button>

        <Toast />
        <div style="display: flex; gap: 1rem; margin-left: 3rem">
            <router-link :to="{ name: 'trang-chu' }">
                <p class="nav-links">Home</p>
            </router-link>
            <router-link :to="{ name: 'san-pham' }">
                <p class="nav-links">Products</p>
            </router-link>
            <router-link :to="{ name: 'gioi-thieu' }">
                <p class="nav-links">Abouts</p>
            </router-link>
            <router-link :to="{ name: 'gioi-thieu' }">
                <p class="nav-links">Contacts</p>
            </router-link>
        </div>
        <div class="layout-topbar-menu">
            <router-link :to="{ name: 'gio-hang' }" style="width: 5%; margin-right: 3px">
                <i class="pi pi-shopping-cart p-text-secondary p-overlay-badge" style="font-size: 1.5rem" v-badge.danger="gioHangService.soLuong"></i>
            </router-link>
            <div class="flex justify-content-center" style="margin-right: 10px; margin-left: 20px">
                <button class="p-link" @click="toggle" aria-haspopup="true" aria-controls="overlay_tmenu">
                    <i class="pi pi-user" style="font-size: 1.5rem" />
                </button>
                <OverlayPanel ref="op2" style="display: block; width: 150px">
                    <button v-if="tokenCheck != null" class="p-link a" aria-haspopup="true" aria-controls="overlay_tmenu" @click="thongTinCaNhan">
                        <div class="flex align-items-center" style="height: 20px; margin-bottom: 10px; width: 120px">Profile</div>
                    </button>
                    <button v-if="tokenCheck != null" class="p-link a" aria-haspopup="true" aria-controls="overlay_tmenu" @click="diaChi">
                        <div class="flex align-items-center" style="height: 20px; margin-bottom: 10px; width: 120px">Address</div>
                    </button>
                    <button v-if="tokenCheck != null" class="p-link a" aria-haspopup="true" aria-controls="overlay_tmenu" @click="lichSuMuaHang">
                        <div class="flex align-items-center" style="height: 20px; margin-bottom: 10px; width: 120px">History Order</div>
                    </button>
                    <button v-if="tokenCheck == null" class="p-link a" aria-haspopup="true" aria-controls="overlay_tmenu" @click="dangNhap">
                        <div class="flex align-items-center" style="height: 20px; margin-bottom: 10px; width: 120px">Login</div>
                    </button>

                    <button v-if="tokenCheck != null" class="p-link a" aria-haspopup="true" aria-controls="overlay_tmenu" @click="dangXuat">
                        <div class="flex align-items-center" style="height: 20px; margin-bottom: 10px; width: 120px">Logout</div>
                    </button>
                </OverlayPanel>
            </div>
            <div class="flex justify-content-center gap-4">
                <button class="p-link" @click="toggle2" aria-haspopup="true" aria-controls="overlay_tmenu">
                    <i v-badge.danger="dem" class="pi pi-bell p-overlay-badge" style="font-size: 1.5rem" />
                </button>

                <OverlayPanel ref="op">
                    <div class="">
                        <div>
                            <span class="notify-title">Notification</span>
                            <br />
                        </div>
                    </div>

                    <div v-if="!data || data.length === 0">
                        <h5 style="text-align: center">Chưa có Thông báo !</h5>
                    </div>
                    <div v-for="(o, index) in data">
                        <button class="p-link" aria-haspopup="true" aria-controls="overlay_tmenu">
                            <div class="flex align-items-center" style="height: 50px; margin-bottom: 10px; width: 240px" @click="daXem(o.id)">
                                <div style="display: flex">
                                    <div style="margin-right: 10px; width: 180px; margin-bottom: -30px">
                                        <p style="margin-bottom: 30px">{{ o.content }}</p>
                                    </div>
                                    <div style="">
                                        <span style="font-size: 10px; margin-top: 0px">{{ o.trangThai == 0 ? 'đã xem' : 'chưa xem' }}</span>
                                    </div>
                                </div>
                            </div>
                        </button>
                    </div>
                </OverlayPanel>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
button.p-link:hover {
    background-color: rgb(248, 239, 239);
    /* Thay #f00 bằng màu bạn muốn */
}

.notify-container {
    display: flex;
    flex-direction: column;
    width: 24rem;
    gap: 0.75rem;
}

.nav-links {
    font-size: 16px;
    font-family: 'Poppins', sans-serif;
    font-size: 18px;
    color: #000;
}

.nav-links:hover {
    color: red;
}

.notify-title {
    font-family: 'Poppins', sans-serif;
    font-size: 18px;
    display: block;
}
h1,
h2,
h3,
h4,
h5,
span,
p {
    font-family: 'Poppins', sans-serif;
}
</style>
