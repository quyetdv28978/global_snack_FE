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
        // updateMauSac(id, form) {
        //     axios.put(apiMauSac + '/update/' + id, form).then((response) => {
        //         for (let i = 0; i < this.data.length; i++) {
        //             if (id == this.data[i].id) {
        //                 this.data[i].ten = form.ten;
        //                 this.data[i].moTa = form.moTa;
        //             }
        //         }
        //         for (let i = 0; i < this.dataByStatus1.length; i++) {
        //             if (id == this.dataByStatus1[i].id) {
        //                 this.dataByStatus1[i].ten = form.ten;
        //                 this.dataByStatus1[i].moTa = form.moTa;
        //             }
        //         }
        //     });
        // },
        // deleteMauSac(id) {
        //     axios.put(apiMauSac + '/delete/' + id).then((response) => {
        //         if (this.check == 0) {
        //             for (let i = 0; i < this.data.length; i++) {
        //                 if (id == this.data[i].id) {
        //                     this.data[i].trangThai = 0;
        //                 }
        //             }
        //             let index = -1;
        //             for (let i = 0; i < this.dataByStatus1.length; i++) {
        //                 if (id == this.dataByStatus1[i].id) {
        //                     index = i;
        //                 }
        //             }
        //             this.dataByStatus1.splice(index, 1);
        //         } else {
        //             if (this.data[0].trangThai != response.data.data.trangThai) {
        //                 let index = -1;
        //                 for (let i = 0; i < this.data.length; i++) {
        //                     if (id == this.data[i].id) {
        //                         index = i;
        //                     }
        //                 }
        //                 this.data.splice(index, 1);
        //                 let index2 = -1;
        //                 for (let i = 0; i < this.dataByStatus1.length; i++) {
        //                     if (id == this.dataByStatus1[i].id) {
        //                         index2 = i;
        //                     }
        //                 }
        //                 this.dataByStatus1.splice(index2, 1);
        //             }
        //         }
        //     });
        // }
    }
});
