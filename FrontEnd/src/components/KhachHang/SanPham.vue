<script setup>
import Carousel from 'primevue/carousel';
import { computed, onMounted, ref } from 'vue';
import { SanPhamStore } from '@/service/KhachHang/SanPhamService';
import { useRouter } from 'vue-router';
const SanPhamService = SanPhamStore();
const home = ref({
    icon: 'pi pi-home',
    to: { name: 'trang-chu' }
});

const router = useRouter();

const goToProductDetail = (productId) => {
    router.push({ name: 'chi-tiet-san-pham', params: { id: productId } });
};

const dataSP = ref([]);
const loadData = async () => {
    await SanPhamService.fetchAll();
    dataSP.value = SanPhamService.dataSP;
    // console.log(SanPhamService.dataSP);
    // console.log(dataSP.value);
};

const uniqueTenLoai = computed(() => {
    const tenLoaiSet = new Set(['Chọn loại sản phẩm', ...dataSP.value.map((item) => item.tenLoai)]);
    return Array.from(tenLoaiSet);
});

onMounted(() => {
    loadData();
});

const layout = ref('grid');

const items = ref([{ label: 'Tất cả sản phẩm', to: { name: 'san-pham' } }]);

const formatCurrency = (value) => {
    return parseInt(value).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
};

const priceRanges = [
    { label: 'Lọc theo giá', min: 0, max: Infinity },
    { label: 'Dưới 500.000', min: 0, max: 500000 },
    { label: '500.000 - 1 triệu', min: 500000, max: 1000000 },
    { label: 'Trên 1 triệu', min: 1000000, max: Infinity }
];
const selectedSortOption = ref(null);

const sapXep = ref([{ label: 'Sắp xếp' }, { label: 'Giá cao - > thấp' }, { label: 'Giá thấp - > cao' }, { label: 'Mới nhất' }]);

const searchTerm = ref('');
const selectedPriceRange = ref(null);

const sortProducts = (products, sortOption) => {
    switch (sortOption.label) {
        case 'Giá cao - > thấp':
            return products.sort((a, b) => b.giaBanMax - a.giaBanMax);
        case 'Giá thấp - > cao':
            return products.sort((a, b) => a.giaBanMin - b.giaBanMin);
        case 'Mới nhất':
            return products.sort((a, b) => new Date(b.ngayTao) - new Date(a.ngayTao));
        default:
            return products;
    }
};

const selectedTenLoai = ref(null);
const selectedTenThuongHieu = ref(null);

const uniqueTenThuongHieu = computed(() => {
    const tenThuongHieuSet = new Set(['Chọn thương hiệu', ...dataSP.value.map((item) => item.tenThuongHieu)]);
    return Array.from(tenThuongHieuSet);
});

const filteredAndSortedProducts = computed(() => {
    let filteredProducts = dataSP.value;

    // Filter by name
    if (searchTerm.value) {
        const searchTermValue = searchTerm.value.toLowerCase();
        filteredProducts = filteredProducts.filter((product) => {
            return product.tenSP.toLowerCase().includes(searchTermValue) || product.tenLoai.toLowerCase().includes(searchTermValue) || product.tenThuongHieu.toLowerCase().includes(searchTermValue);
        });
    }
    // Filter by price range
    if (selectedPriceRange.value) {
        const { min, max } = selectedPriceRange.value;
        filteredProducts = filteredProducts.filter((product) => {
            const productPrice = (parseInt(product.giaBanMin) + parseInt(product.giaBanMax)) / 2;
            return productPrice >= min && productPrice <= max;
        });
    }

    if (selectedTenLoai.value && selectedTenLoai.value !== 'Chọn loại sản phẩm') {
        filteredProducts = filteredProducts.filter((product) => {
            return product.tenLoai === selectedTenLoai.value;
        });
    }

    // Filter by 'tenThuongHieu'
    if (selectedTenThuongHieu.value && selectedTenThuongHieu.value !== 'Chọn thương hiệu') {
        filteredProducts = filteredProducts.filter((product) => {
            return product.tenThuongHieu === selectedTenThuongHieu.value;
        });
    }

    // Sort the filtered products
    if (selectedSortOption.value) {
        return sortProducts(filteredProducts, selectedSortOption.value);
    }

    return filteredProducts;
});
</script>

<template>
    <div class="card">
        <div class="container">
            <h1>Sản phẩm</h1>

            <div class="flex">
                <Dropdown v-model="selectedSortOption" :options="sapXep" optionLabel="label" placeholder="Sắp xếp" class="w-full md:w-14rem" />
                <Dropdown v-model="selectedPriceRange" :options="priceRanges" optionLabel="label" placeholder="Lọc theo giá" class="w-full md:w-14rem" style="margin-left: 20px" />
                <Dropdown v-model="selectedTenLoai" :options="uniqueTenLoai" placeholder="Chọn loại sản phẩm" class="w-full md:w-14rem" style="margin-left: 20px" />
                <Dropdown v-model="selectedTenThuongHieu" :options="uniqueTenThuongHieu" placeholder="Chọn thương hiệu" class="w-full md:w-14rem" style="margin-left: 20px" />
                <span class="block mt-2 md:mt-0 p-input-icon-left" style="margin-left: 150px">
                    <i class="pi pi-search" />
                    <InputText v-model="searchTerm" placeholder="Search..." />
                </span>
            </div>
            <br />
            <DataView
                :value="filteredAndSortedProducts"
                :layout="layout"
                dataKey="id"
                :paginator="true"
                :rows="12"
                :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                :rowsPerPageOptions="[12, 18, 27]"
                currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
                responsiveLayout="scroll"
            >
                <template #grid="slotProps">
                    <div class="grid grid-nogutter">
                        <div v-for="(sp, index) in slotProps.items" :key="index" class="col-12 sm:col-6 lg:col-12 xl:col-3 p-2">
                            <div class="p-4 border-1 surface-border surface-card border-round">
                                <div class="flex flex-column gap-3">
                                    <img class="product-image" :src="sp.anh" :alt="sp.tenSP" />
                                    <div class="text-xl font-medium product-name">{{ sp.tenSP }}</div>
                                    <div class="category-product">{{ sp.tenLoai }}</div>
                                </div>
                                <div class="flex sm:flex-column gap-3 sm:gap-2">
                                    <p class="text-lg text-red-600 font-semibold" v-if="sp.giaBanMin == sp.giaBanMax">{{ formatCurrency(sp.giaBanMax) }}</p>
                                    <p class="text-lg text-red-600 font-semibold" v-else-if="sp.giaSauGiamMax != null && sp.giaSauGiamMin != null && sp.giaSauGiamMax != sp.giaSauGiamMin">
                                        {{ formatCurrency(sp.giaSauGiamMin) }} - {{ formatCurrency(sp.giaSauGiamMax) }}
                                    </p>
                                    <p class="text-lg text-red-600 font-semibold" v-else-if="sp.giaSauGiamMax == null && sp.giaSauGiamMin == null">{{ formatCurrency(sp.giaBanMin) }} - {{ formatCurrency(sp.giaBanMax) }}</p>
                                    <p class="text-lg text-red-600 font-semibold" v-else-if="sp.giaSauGiamMax == sp.giaSauGiamMin">{{ formatCurrency(sp.giaSauGiamMax) }}</p>
                                    <p class="text-lg text-red-600 font-semibold" v-else>{{ formatCurrency(sp.giaBanMin) }} - {{ formatCurrency(sp.giaBanMax) }}</p>

                                    <Button icon="pi pi-shopping-cart" class="bg-red-600 border-none hover:bg-red-800" rounded @click="goToProductDetail(sp.idSP)"></Button>
                                </div>
                                <div class="flex align-items-center justify-content-between"></div>
                            </div>
                        </div>
                    </div>
                </template>
            </DataView>
        </div>
    </div>
</template>

<style scoped>
.card {
    margin-top: 45px;
    display: flex;
    justify-content: center;
}
.custom-dropdown {
    max-width: 200px; /* Đặt giá trị kích thước tối đa cho dropdown */
}
.flex-container {
    display: flex;
    justify-content: space-between; /* Chia đều khoảng trống giữa các cột */
}

.flex-item {
    flex: 1; /* Cung cấp độ rộng đều cho mỗi cột */
    border: 1px solid #ccc;
    padding: 20px;
    margin-right: 10px; /* Khoảng cách giữa các cột */
}

/* Loại bỏ margin cuối cùng của cột cuối cùng */
.flex-item:last-child {
    margin-right: 0;
}

.container {
    width: 1100px;
    margin: 0 auto;
    margin-top: 2rem;
}

.product-name {
    font-size: 17px;
    color: black;
    overflow: hidden;
    width: 100%;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    margin-top: 1.5rem;
    font-weight: 600;
}

.category-product {
    margin: 0.5rem 0;
    font-size: 16px;
    font-weight: 500;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
}

.product-image {
    max-width: 100%;
    height: 200px;
    display: block;
    border-radius: 10px;
}
/* Định dạng cho mỗi phần tử trong danh sách */
</style>
