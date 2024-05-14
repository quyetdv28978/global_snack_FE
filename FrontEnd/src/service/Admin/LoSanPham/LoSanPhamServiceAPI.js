import { defineStore } from 'pinia';
import axios from '@/service/Authentication/http.js';

const apiLoSanPham = `${import.meta.env.VITE_BASE_API_ENDPOINT}/admin/lo-san-pham`;

export const useLoSanPhamService = defineStore('lo-san-pham', {
    state: () => ({
        data: [],
        dataByStatus1: [],
        check: 0,
        dataLoSanPhamByMount: [],
        dataCheckLoSanPham: []
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
        }, async fetchData2() {
            try {
               
                const response = await axios.get("http://localhost:8080/ncc").then(data => {
                    console.log(data.data._embedded);
                    return data.data._embedded
                });
                this.data = response;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        //load data lo san pham theo san pham chi tiet hoặc lô rổng
        async fetchDataBySPCT(status) {
            this.check = 0;
            try {
                const response = await axios.get(apiLoSanPham + '/by-san-pham-ct/' + status);
                this.dataByStatus1 = response.data;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        // load data lo san pham theo san pham chi tiet
        async fetchDataBySPCTNotNull(status) {
            this.check = 0;
            try {
                const response = await axios.get(apiLoSanPham + '/by-san-pham-ct-not-null/' + status);
                this.data = response.data;

            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        async showLoSanPhamByMount(mount) {
            this.check = 0;
            try {
                const response = await axios.get(apiLoSanPham + `/lo-san-pham-het-han-by-mount/${mount}`);
                this.dataLoSanPhamByMount = response.data;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        async chooseLoSanPham(idctsp) {
            const response = await axios.get(`${apiLoSanPham}/choose-lo/${idctsp}`);
            this.dataCheckLoSanPham = response.data;
        }
        ,
        async showLoSanPhamSapHethan(idctsp) {
            const response = await axios.get(`${apiLoSanPham}/sap_het_han`);
            this.data = response.data;
        },
        async createMauSac(form) {
            axios.post(apiLoSanPham + '/add-lo', form).then((response) => {
                // if (this.check == 0) {
                    // this.data.unshift(response.data);
                    console.log(this.dataByStatus1);
                    this.dataByStatus1.unshift(response.data);
                    console.log(this.dataByStatus1);

                // } else {
                //     if (this.data[0].trangThai == response.data.trangThai) this.data.unshift(response.data);
                //     if (response.data.trangThai == 1) this.dataByStatus1.unshift(response.data);
                // }
            });
        },
        async updateLoSanPham(idlsp, idctsp) {
            axios.put(apiLoSanPham + `/update-lo-san-pham-by-idctsp/${idlsp}/${idctsp}`).then((response) => {
                const index = this.dataByStatus1.findIndex(product => product.id === idctsp);
                if (index !== -1) {
                    let newProductData = this.dataByStatus1[index];
                    newProductData = response.dataByStatus1;
                    this.dataByStatus1[index] = newProductData;
                }
            });
        }
        ,
        async updateLoSanPhamTT(maLo) {
            this.check = 0;
            try {
                const response = await axios.put(apiLoSanPham + `/update-lo-san-pham/${maLo}`);
                this.data = response.data;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        }

    }
});
