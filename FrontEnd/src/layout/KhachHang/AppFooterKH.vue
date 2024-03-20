<script setup>
import { useLayout } from '@/layout/composables/layout';
import { computed } from 'vue';

const { layoutConfig } = useLayout();

const logoUrl = computed(() => {
    return `layout/images/${layoutConfig.darkTheme.value ? 'logo-white' : 'logo-dark'}.svg`;
});
</script>

<template>
    <!-- Remove the container if you want to extend the Footer to full width. -->

    <footer class="footer">
        <div class="container">
            <div class="row">
                <div class="footer-col">
                    <h4>company</h4>
                    <ul>
                        <li><a href="#">về chúng tôi</a></li>
                        <li><a href="#">dịch vụ của chúng tôi</a></li>
                        <li><a href="#">chính sách bảo mật</a></li>
                        <li><a href="#">chương trình liên kết</a></li>
                    </ul>
                </div>
                <div class="footer-col">
                    <h4>get help</h4>
                    <ul>
                        <li><a href="#">FAQ</a></li>
                        <li><a href="#">giao hàng</a></li>
                        <li><a href="#">trả hàng</a></li>
                        <li><a href="#">trạng thái đơn hàng</a></li>
                        <li><a href="#">phương thức thanh toán</a></li>
                    </ul>
                </div>
                <div class="footer-col">
                    <h4>online shop</h4>
                    <ul>
                        <li><a href="#">Khoai Tây Chiên</a></li>
                        <li><a href="#">Bim Bim</a></li>
                        <li><a href="#">Khô gà </a></li>
                        <li><a href="#">Đùi gà rong biển </a></li>
                    </ul>
                </div>
                <div class="footer-col">
                    <h4>follow us</h4>
                    <div class="social-links">
                        <a href="#"><i class="pi pi-facebook"></i></a>
                        <a href="#"><i class="pi pi-twitter"></i></a>
                        <a href="#"><i class="pi pi-instagram"></i></a>
                        <a href="#"><i class="pi pi-linkedin"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!-- Footer -->
    <!-- End of .container -->
</template>
<style lang="scss" scoped>
.container {
    max-width: 1170px;
    margin: auto;
}
.row {
    display: flex;
    flex-wrap: wrap;
}
ul {
    list-style: none;
    padding: 0;
}
.footer {
    background-color: #24262b;
    padding: 70px 0;
}
.footer-col {
    width: 25%;
    padding: 0 15px;
}
.footer-col h4 {
    font-size: 18px;
    color: #ffffff;
    text-transform: capitalize;
    margin-bottom: 35px;
    font-weight: 500;
    position: relative;
}
.footer-col h4::before {
    content: '';
    position: absolute;
    left: 0;
    bottom: -10px;
    background-color: #e91e63;
    height: 2px;
    box-sizing: border-box;
    width: 50px;
}
.footer-col ul li:not(:last-child) {
    margin-bottom: 10px;
}
.footer-col ul li a {
    font-size: 16px;
    text-transform: capitalize;
    color: #ffffff;
    text-decoration: none;
    font-weight: 300;
    color: #bbbbbb;
    display: block;
    transition: all 0.3s ease;
}
.footer-col ul li a:hover {
    color: #ffffff;
    padding-left: 8px;
}
.footer-col .social-links a {
    display: inline-block;
    height: 40px;
    width: 40px;
    background-color: rgba(255, 255, 255, 0.2);
    margin: 0 10px 10px 0;
    text-align: center;
    line-height: 40px;
    border-radius: 50%;
    color: #ffffff;
    transition: all 0.5s ease;
}
.footer-col .social-links a:hover {
    color: #24262b;
    background-color: #ffffff;
}

/*responsive*/
@media (max-width: 767px) {
    .footer-col {
        width: 50%;
        margin-bottom: 30px;
    }
}
@media (max-width: 574px) {
    .footer-col {
        width: 100%;
    }
}
</style>
