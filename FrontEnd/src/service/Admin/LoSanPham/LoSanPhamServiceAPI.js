import { defineStore } from 'pinia';
import axios from '@/service/Authentication/http.js';

const apiLoSanPham = `${import.meta.env.VITE_BASE_API_ENDPOINT}/admin/lo-san-pham`;

export const useLoSanPhamService = defineStore('lo-san-pham', {
    state: () => ({
        data: [],
        dataByStatus1: [],
        check: 0
    }),
    actions: {
        //load tất cả data
        async fetchData() {
            this.check = 0;
            try {
                const response = await axios.get(apiLoSanPham);
                this.data = response.data;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        //load data size theo trạng thái
        async fetchDataByStatus(status) {
            this.check = 1;
            try {
                const response = await axios.get(apiLoSanPham + '/' + status);
                // console.log(response.data);
                if (status === 1) {
                    this.dataByStatus1 = response.data;
                }
                this.data = response.data;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        //load data lo san pham theo san pham chi tiet
        async fetchDataBySPCT(status) {
            this.check = 1;
            try {
                const response = await axios.get(apiLoSanPham + '/by-san-pham-ct/' + status);
                console.log(response.data);
                    this.dataByStatus1 = response.data;
                
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        createMauSac(form) {
            axios.post(apiLoSanPham + '/add-lo', form).then((response) => {
                if (this.check == 0) {
                    this.data.unshift(response.data);
                    if (response.data.trangThai == 1) this.dataByStatus1.unshift(response.data);
                } else {
                    if (this.data[0].trangThai == response.data.trangThai) this.data.unshift(response.data);
                    if (response.data.trangThai == 1) this.dataByStatus1.unshift(response.data);
                }
            });
        },
       
    }
});
