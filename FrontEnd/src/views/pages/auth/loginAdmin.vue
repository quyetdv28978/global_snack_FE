<script setup>
import * as Yup from 'yup';
import { useLayout } from '@/layout/composables/layout';
import { ref, computed } from 'vue';
import { useField, useForm } from 'vee-validate';
import AppConfig from '@/layout/AppConfig.vue';
import { decodeCredential } from 'vue3-google-login';
import tokenService from '@/service/Authentication/TokenService.js';
import { dangNhapStore } from '@/service/KhachHang/DangNhapService';
import { useToast } from 'primevue/usetoast';
import { useRouter } from 'vue-router';
import { gioHangStore } from '@/service/KhachHang/Giohang/GiohangCTService.js';
import { verifyJwt } from '@/service/common/JwtUtils';
import { useChatStore } from '@/service/Admin/Chat/ChatService';
import * as chatService from '@/service/chatkitty';

const router = useRouter();
const gioHangService = gioHangStore();
const dnService = dangNhapStore();
const chatStore = useChatStore();
const { layoutConfig } = useLayout();
const checked = ref(false);
const toast = useToast();

const gotoTrangChu = () => {
    router.push({ name: 'admin' });
};

const schema = Yup.object().shape({
    email: Yup.string().required('Email không được để trống').email('Email không đúng định dạng'),
    password: Yup.string().required('Mật khẩu không được để trống').min(5, 'Mật khẩu phải trên 5 ký tự')
});

const { handleSubmit, resetForm } = useForm({
    validationSchema: schema
});

const { value: email, errorMessage: emailError } = useField('email');
const { value: password, errorMessage: passwordError } = useField('password');
const dangNhapa = handleSubmit(async () => {
    const login = {
        usernameOrEmail: email.value,
        password: password.value
    };
    const data = await dnService.dangNhap(login);

    if (data == null || data === undefined || data.length <= 0) {
        toast.add({ severity: 'error', summary: 'Thông báo', detail: 'Sai tài khoản hoặc mật khẩu', life: 3000 });
    } else {
        localStorage.setItem('token', data.accessToken);
        localStorage.setItem(
            'currentUserInformation',
            JSON.stringify({
                id: data.userID,
                username: data.usernameOrEmail
            })
        );

        gotoTrangChu();
    }
});

// const dangNhapa = async () => {
//     const login = {
//         usernameOrEmail: email.value,
//         password: password.value
//     };
//     const token = await dnService.dangNhap(login);
//     localStorage.setItem('token', token);
//     gotoTrangChu();

//     // khi dang nhap thanh cong thi add sp gio hang vao db
//     if (localStorage.getItem('cart')) {
//         let array = JSON.parse(localStorage.getItem('cart'));
//         await gioHangService.addToCartWhenLogin(array, token);
//         localStorage.removeItem('cart');
//     }
// };

const logoUrl = computed(() => {
    return `layout/images/${layoutConfig.darkTheme.value ? 'logo-white' : 'logo-dark'}.svg`;
});
</script>

<template>
    <div class="surface-ground flex align-items-center justify-content-center min-h-screen min-w-screen overflow-hidden">
        <Toast />
        <div class="flex flex-column align-items-center justify-content-center">
            <img src="/src/assets/images/logo.jpg" alt="Sakai logo" class="mb-5 w-6rem flex-shrink-0" />
            <form class="form">
                <div class="flex-column">
                    <label>Email </label>
                </div>
                <div class="inputForm">
                    
                    <input type="text" class="input" placeholder="Email" v-model="email" />
                </div>
                <small class="p-error">{{ emailError }}</small>
                <div class="flex-column">
                    <label>Password</label>
                </div>
                <div class="inputForm">
                  
                    <input type="password" class="input" placeholder="Password" v-model="password" />
                    <svg viewBox="0 0 576 512" height="1em" xmlns="http://www.w3.org/2000/svg">
                        <path
                            d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"
                        ></path>
                    </svg>
                </div>
                <small class="p-error">{{ passwordError }}</small>
                <div class="flex-row">
                    <div>
                        <input type="checkbox" />
                        <label>Ghi nhớ đăng nhập</label>
                    </div>
                    <router-link :to="{ name: 'forgot-password' }">Quên mật khẩu?</router-link>
                </div>
                <button class="button-submit" @click="dangNhapa">Đăng Nhập</button>
            </form>
        </div>
    </div>
    <AppConfig simple />
</template>

<style scoped>
.pi-eye {
    transform: scale(1.6);
    margin-right: 1rem;
}

.pi-eye-slash {
    transform: scale(1.6);
    margin-right: 1rem;
}

.form {
    display: flex;
    flex-direction: column;
    gap: 10px;
    background-color: #ffffff;
    padding: 30px;
    width: 450px;
    border-radius: 20px;
}

.form button {
    align-self: flex-end;
}

.flex-column > label {
    color: #151717;
    font-weight: 600;
}

.inputForm {
    border: 1.5px solid #ecedec;
    border-radius: 10px;
    height: 50px;
    display: flex;
    align-items: center;
    padding-left: 10px;
    transition: 0.2s ease-in-out;
}

.input {
    margin-left: 10px;
    border-radius: 10px;
    border: none;
    width: 85%;
    height: 100%;
    outline : none
}

.input:focus {
    outline: none;
}

.inputForm:focus-within {
    border: 1.5px solid #2d79f3;
}

.flex-row {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 10px;
    justify-content: space-between;
}

.flex-row > div > label {
    font-size: 14px;
    color: black;
    font-weight: 400;
}

.span {
    font-size: 14px;
    margin-left: 5px;
    color: #2d79f3;
    font-weight: 500;
    cursor: pointer;
}

.button-submit {
    margin: 20px 0 10px 0;
    background-color: #151717;
    border: none;
    color: white;
    font-size: 15px;
    font-weight: 500;
    border-radius: 10px;
    height: 50px;
    width: 100%;
    cursor: pointer;
}

.button-submit:hover {
    background-color: #252727;
}

.p {
    text-align: center;
    color: black;
    font-size: 14px;
    margin: 5px 0;
}

.btn {
    margin-top: 10px;
    width: 100%;
    height: 50px;
    border-radius: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 500;
    gap: 10px;
    border: 1px solid #ededef;
    background-color: white;
    cursor: pointer;
    transition: 0.2s ease-in-out;
}

.btn:hover {
    border: 1px solid #2d79f3;
}
</style>
